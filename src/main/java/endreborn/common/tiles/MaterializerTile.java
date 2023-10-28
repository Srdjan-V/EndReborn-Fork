package endreborn.common.tiles;

import static net.minecraft.util.EnumParticleTypes.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.IValue;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.manager.GuiCreationContext;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.viewport.GuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.google.common.collect.Lists;

import endreborn.EndReborn;
import endreborn.api.materializer.Catalyst;
import endreborn.api.materializer.CriticalityEvent;
import endreborn.api.materializer.MaterializerHandler;
import endreborn.api.materializer.MaterializerRecipe;
import endreborn.common.ModBlocks;
import endreborn.common.blocks.BlockMaterializer;

public class MaterializerTile extends TileEntity implements ITickable, IGuiHolder {

    private final ItemStackHandler inputInventory = new ItemStackHandler(2) {

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markDirty();
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return switch (slot) {
                case 1 -> {
                    if (Objects.nonNull(MaterializerHandler.findCatalyst(stack))) {
                        yield super.insertItem(slot, stack, simulate);
                    }
                    yield stack;
                }
                case 2 -> stack;
                default -> super.insertItem(slot, stack, simulate);
            };
        }
    };
    private final ItemStackHandler outInventory = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markDirty();
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return switch (slot) {
                case 0 -> stack;
                case -1 -> super.insertItem(0, stack, simulate);
                default -> super.insertItem(slot, stack, simulate);
            };
        }
    };
    private final ItemStackHandler processingInventory = new ItemStackHandler(2) {

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markDirty();
        }
    };

    // Dynamic
    ////////////////////
    private Status status = Status.Idle;
    private Catalyst catalyst;
    private MaterializerRecipe recipe;
    ////////////////////
    private int ticksRun;
    private int failTick;
    private int lastTriggeredCriticalityIndex;
    private int blockStateChangeBuffer;// no need to serialize

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString("container.entropy_user");
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        ticksRun = compound.getInteger("ticksRun");
        failTick = compound.getInteger("failTick");
        lastTriggeredCriticalityIndex = compound.getInteger("lastTriggeredCriticalityIndex");
        if (compound.hasKey("inputInventory"))
            inputInventory.deserializeNBT(compound.getCompoundTag("inputInventory"));
        if (compound.hasKey("outInventory"))
            outInventory.deserializeNBT(compound.getCompoundTag("outInventory"));
        if (compound.hasKey("processingInventory"))
            processingInventory.deserializeNBT(compound.getCompoundTag("processingInventory"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("ticksRun", ticksRun);
        compound.setInteger("failTick", failTick);
        compound.setInteger("lastTriggeredCriticalityIndex", lastTriggeredCriticalityIndex);
        compound.setTag("inputInventory", inputInventory.serializeNBT());
        compound.setTag("outInventory", outInventory.serializeNBT());
        compound.setTag("processingInventory", processingInventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public ModularPanel buildUI(GuiCreationContext creationContext, GuiSyncManager syncManager, boolean isClient) {
        ModularPanel panel = ModularPanel.defaultPanel("materializer_gui").bindPlayerInventory();

        class BasicTextWidget extends Widget<BasicTextWidget> {

            @Override
            public void setValue(IValue<?> value) {
                super.setValue(value);
            }

            @Override
            public void draw(GuiContext context, WidgetTheme widgetTheme) {
                if (failTick > 0) {
                    IKey.lang(Status.Failed.langKey).drawAtZero(context, getArea());
                    if (failTick > 20 * 3) {
                        failTick = 0;
                    }
                    return;
                }

                var newStatus = (String) getValue().getValue();
                var trueStatus = Status.valueOf(newStatus);
                if (trueStatus == Status.Failed) failTick++;
                IKey.lang(trueStatus.langKey).drawAtZero(context, getArea());
            }
        }

        var textBox = new BasicTextWidget().left(20).top(3).right(20).background(GuiTextures.BACKGROUND);
        textBox.setValue(new StringSyncValue(() -> status.name(), null));

        panel.child(textBox);
        // TODO: 28/10/2023 center
        panel.child(new ItemSlot().slot(new ModularSlot(inputInventory, 0)).left(50).top(45));
        panel.child(new ItemSlot().slot(new ModularSlot(inputInventory, 1)).left(70).top(45));
        panel.child(new ItemSlot().slot(new ModularSlot(outInventory, 0).accessibility(false, true)).left(120).top(45));

        var process = new ProgressWidget()
                .size(18).left(95).top(45)
                .texture(GuiTextures.PROGRESS_ARROW, 25)
                .value(new DoubleSyncValue(() -> {
                    if (recipe != null) return (double) ticksRun / recipe.getTicksToComplete();
                    return 0;
                }, null));
        panel.child(process);

        return panel;
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            updateBlockState();
            if (prepare()) progress();
        } else if (failTick != 0) failTick++;
    }

    private boolean prepare() {
        var processingItem = processingInventory.getStackInSlot(0);
        var processingCatalyst = processingInventory.getStackInSlot(1);
        boolean validProcessingRecipe = true;
        if (processingItem.isEmpty() || processingCatalyst.isEmpty()) {
            if (!(processingItem.isEmpty() && processingCatalyst.isEmpty()))
                EndReborn.LOGGER.warn("Clearing processing items in {} at {}, items {}, {}",
                        this.getClass(), this.pos, processingItem, processingCatalyst);
            updateStatus(Status.Idle);
            resetProcessingInv();
            validProcessingRecipe = false;
        }

        if (validProcessingRecipe) {
            catalyst = MaterializerHandler.findCatalyst(processingCatalyst);
            if (Objects.isNull(catalyst)) return false;

            recipe = MaterializerHandler.findRecipe(catalyst, processingItem);
            if (Objects.isNull(recipe)) return false;

        } else {
            boolean invalid = false;

            var inputCatalyst = inputInventory.getStackInSlot(1);
            catalyst = MaterializerHandler.findCatalyst(inputCatalyst);
            if (Objects.isNull(catalyst)) invalid = true;

            var inputItem = inputInventory.getStackInSlot(0);
            recipe = MaterializerHandler.findRecipe(catalyst, inputItem);
            if (Objects.isNull(recipe)) invalid = true;

            if (invalid) {
                if (inputCatalyst.isEmpty() && inputItem.isEmpty()) {
                    updateStatus(Status.Idle);
                } else updateStatus(Status.Invalid);
                return false;
            }

            if (!inputInventory.extractItem(0, recipe.getInput().getCount(), true).isEmpty() &&
                    !inputInventory.extractItem(0, 1, true).isEmpty()) {
                processingInventory.insertItem(0, inputInventory.extractItem(0, recipe.getInput().getCount(), false),
                        false);
                processingInventory.insertItem(1, inputInventory.extractItem(1, 1, false), false);
            } else {
                updateStatus(Status.Invalid);
                return false;
            }
        }
        return true;
    }

    private void progress() {
        // finished
        if (ticksRun >= recipe.getTicksToComplete()) {
            var outputStack = recipe.getOutput().apply(processingInventory.getStackInSlot(0), catalyst);
            if (outInventory.insertItem(-1, outputStack, true).isEmpty()) {
                outInventory.insertItem(-1, outputStack, false);
                spawnSpawnSuccessfulCraft();
                resetProcessingInv();
                reset();
            } else updateStatus(Status.OutFull);
        } else {
            updateStatus(Status.Running);
            final double ratio = (double) ticksRun / recipe.getTicksToComplete();
            final int percent = (int) (ratio * 100);
            for (int i : recipe.getCriticalityEvents().keySet()) {
                if (percent >= i && (lastTriggeredCriticalityIndex < i || lastTriggeredCriticalityIndex == 0)) {
                    if (!triggerCriticality(i)) {
                        updateStatus(Status.Failed);
                        spawnFailedCraftParticles();
                        resetProcessingInv();
                        reset();
                    }
                    lastTriggeredCriticalityIndex = i;
                }
            }
            ticksRun++;
        }
    }

    private boolean triggerCriticality(int index) {
        var chance = world.rand.nextInt(100);
        CriticalityEvent event = recipe.getCriticalityEvents().get(index);
        if (chance > event.getChance()) return true;

        List<BlockPos> posList = Lists.newArrayList(BlockPos.getAllInBox(this.getPos().add(5, 5, 5),
                this.getPos().add(-5, -5, -5)));

        Collections.shuffle(posList);
        for (BlockPos blockPos : posList) {
            if (!world.isBlockLoaded(pos)) continue;
            if (blockPos.getX() == this.getPos().getX() &&
                    blockPos.getZ() == this.getPos().getZ())
                continue;

            var state = world.getBlockState(pos);
            if (event.getBlockChecker().test((WorldServer) world, state, pos)) {
                event.getBlockAction().particles((WorldServer) world, state, pos);
                event.getBlockAction().run((WorldServer) world, state, pos);
            }
        }

        return false;
    }

    private void updateStatus(Status status) {
        if (this.status != status) {
            this.status = status;
        }
    }

    private void updateBlockState() {
        if (++blockStateChangeBuffer % (20 * 2) == 0) {
            IBlockState state = world.getBlockState(pos);
            var working = state.getValue(BlockMaterializer.WORKING);
            if (status == Status.Running && !working) {
                world.playSound(null, pos, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 0.5F,
                        2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                world.setBlockState(pos, ModBlocks.MATERIALIZER.get().getDefaultState()
                        .withProperty(BlockMaterializer.FACING, state.getValue(BlockMaterializer.FACING))
                        .withProperty(BlockMaterializer.WORKING, true), 3);

            } else if (status != Status.Running && working) {
                world.setBlockState(pos, ModBlocks.MATERIALIZER.get().getDefaultState()
                        .withProperty(BlockMaterializer.FACING, state.getValue(BlockMaterializer.FACING))
                        .withProperty(BlockMaterializer.WORKING, false), 3);
            }
            blockStateChangeBuffer = 0;
        }
    }

    private void spawnFailedCraftParticles() {
        world.addWeatherEffect(new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false));
        spawnCircleOnBlock(getPos(), FLAME);
    }

    private void spawnSpawnSuccessfulCraft() {
        spawnCircleOnBlock(getPos(), PORTAL);
    }

    private void spawnCircleOnBlock(BlockPos pos, EnumParticleTypes type) {
        for (int i = 0; i < 360; i += 40) {
            double xSpeed = Math.cos(Math.toRadians(i)) * 0.2;
            double zSpeed = Math.sin(Math.toRadians(i)) * 0.2;
            ((WorldServer) world).spawnParticle(type, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                    1, xSpeed, 0, zSpeed, 0.2);
        }
    }

    private void resetProcessingInv() {
        processingInventory.setStackInSlot(0, ItemStack.EMPTY);
        processingInventory.setStackInSlot(1, ItemStack.EMPTY);
    }

    private void reset() {
        ticksRun = 0;
        lastTriggeredCriticalityIndex = 0;
        markDirty();
    }

    public void dopItems() {
        for (ItemStackHandler itemStackHandler : Arrays.asList(inputInventory, outInventory)) {
            for (int i = 0; i < itemStackHandler.getSlots(); ++i) {
                ItemStack itemstack = itemStackHandler.getStackInSlot(i);

                if (!itemstack.isEmpty()) {
                    InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
                }
            }
        }
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != null) {
            switch (facing) {
                case NORTH, SOUTH, WEST, EAST, DOWN -> {
                    return true;
                }
                case UP -> {
                    return false;
                }
            }
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != null) {
            switch (facing) {
                case NORTH, SOUTH, WEST, EAST -> {
                    return (T) inputInventory;
                }
                case DOWN -> {
                    return (T) outInventory;
                }
            }
        }
        return super.getCapability(capability, facing);
    }

    enum Status {

        Idle("tile.gui.idle"),
        OutFull("tile.gui.output_full"),
        Failed("tile.gui.failed"),
        Invalid("tile.gui.invalid_recipe"),

        Running("tile.gui.running");

        private final String langKey;

        Status(String langKey) {
            this.langKey = langKey;
        }
    }
}
