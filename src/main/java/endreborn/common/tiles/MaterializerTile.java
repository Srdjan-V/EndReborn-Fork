package endreborn.common.tiles;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import org.jetbrains.annotations.Nullable;

import endreborn.api.materializer.Catalyst;
import endreborn.api.materializer.CriticalityEvent;
import endreborn.api.materializer.MaterializerHandler;
import endreborn.api.materializer.MaterializerRecipe;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class MaterializerTile extends TileEntity implements ITickable {

    private final ItemStackHandler inventory = new ItemStackHandler(3) {

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            // restart hard if catalyst or input change,
            // test may become a hotspot because of constant insertion
            reset(slot == 3);
        }
    };

    private final List<String> operationErrors = new ObjectArrayList<>();
    private Catalyst rawCatalyst;
    private MaterializerRecipe recipe;
    private ItemStack catalyst;
    private int ticksRun;
    private int lastTriggerCriticalityIndex;

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString("container.entropy_user");
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("inventory")) {
            inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            prepare();
            progress();
        } else {
            // particles

        }
    }

    private void prepare() {
        operationErrors.clear();
        {
            var stack = inventory.getStackInSlot(1);
            // find raw catalyst and init
            if (!stack.isEmpty() && catalyst == null) {
                rawCatalyst = MaterializerHandler.findCatalyst(stack);
                if (rawCatalyst == null) {
                    operationErrors.add("invalid catalyst");
                    return;
                }
                catalyst = rawCatalyst.getCatalyst();
            } else
                // update raw catalyst, can be call after tile has been deserialized
                if (catalyst != null) {
                    rawCatalyst = MaterializerHandler.findCatalyst(catalyst);
                }
        }
        var stack = inventory.getStackInSlot(0);
        // find recipe
        if (!stack.isEmpty()) recipe = MaterializerHandler.findRecipe(stack);
        if (recipe != null) {

        } else operationErrors.add("invalid recipe");
    }

    private void progress() {
        if (recipe == null || rawCatalyst == null) return;
        if (ticksRun == 0) {
            // if it can start the process
            if (recipe.minimumCriticality <= rawCatalyst.getCriticality()) {
                inventory.getStackInSlot(0).shrink(1);
                inventory.getStackInSlot(1).shrink(1);
                ticksRun++;
            } else operationErrors.add("catalyst not strong enough");
        } else {
            // finished
            if (ticksRun >= recipe.ticksToCompleate) {
                var stack = inventory.getStackInSlot(2);
                var out = recipe.output.apply(stack, rawCatalyst);
                if (stack.isEmpty()) {
                    inventory.setStackInSlot(2, out);
                    reset(false);
                } else {
                    if (ItemHandlerHelper.canItemStacksStack(out, stack)) {
                        if (out.getCount() < 64) {
                            out.grow(stack.getCount());
                            reset(false);
                        } else operationErrors.add("output full");
                    } else operationErrors.add("output full");
                }
            } else {
                final double ratio = (double) ticksRun / recipe.ticksToCompleate;
                final int percent = (int) (ratio * 100);
                for (Integer i : recipe.criticalityEvents.keySet()) {
                    if (percent >= i && (lastTriggerCriticalityIndex < i || lastTriggerCriticalityIndex == 0)) {
                        if (!triggerCriticality(i)) reset(true);
                        lastTriggerCriticalityIndex = i;
                    }
                }
                ticksRun++;
            }
        }
    }

    private boolean triggerCriticality(int index) {
        var chance = world.rand.nextInt(rawCatalyst.getCriticality());
        CriticalityEvent event = recipe.criticalityEvents.get(index);
        if (chance < event.getChance()) return true;

        int range = rawCatalyst.getWorldCoruptionRange();
        var targetBlock = Block.getBlockFromItem(event.getCriticalityItems().getKey());
        for (BlockPos pos : BlockPos.getAllInBox(this.getPos().add(range, range, range),
                this.getPos().add(-range, -range, -range))) {
            if (!world.isBlockLoaded(pos)) continue;
            if (world.getBlockState(pos).getBlock() == targetBlock) {
                var obj = event.getCriticalityItems().getValue();
                if (obj instanceof ItemBlock itemBlock) {
                    world.setBlockState(pos, itemBlock.getBlock().getDefaultState());
                } else {
                    final ItemStack stack;
                    if (obj instanceof ItemStack) {
                        stack = (ItemStack) obj;
                    } else if (obj instanceof Item item) {
                        stack = new ItemStack(item);
                    } else throw new IllegalArgumentException(String.valueOf(obj));

                    world.setBlockToAir(pos);
                    Block.spawnAsEntity(world, pos, stack);
                }
                return true;
            }
        }

        return false;
    }

    private void reset(boolean soft) {
        if (!soft) {
            rawCatalyst = null;
            catalyst = null;
            recipe = null;
        }
        ticksRun = 0;
        lastTriggerCriticalityIndex = 0;
        markDirty();
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this &&
                player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
                        (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) inventory;
        return super.getCapability(capability, facing);
    }
}
