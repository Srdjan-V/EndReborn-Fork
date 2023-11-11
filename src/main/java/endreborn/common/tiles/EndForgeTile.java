package endreborn.common.tiles;

import java.util.Objects;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
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
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.FluidSlot;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import endreborn.EndReborn;
import endreborn.api.base.groupings.FluidToItemGrouping;
import endreborn.api.base.processors.FluidItemRecipeProcessor;
import endreborn.api.endforge.EndForgeHandler;
import endreborn.api.endforge.EndForgeRecipe;

public class EndForgeTile extends TileEntity implements ITickable, IGuiHolder {

    private final ItemStackHandler intput = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };

    private final ItemStackHandler processingInventory = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };

    private final ItemStackHandler output = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
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

    private final FluidTank inputTank = new FluidTank(1600) {

        @Override
        protected void onContentsChanged() {
            markDirty();
        }
    };
    private final FluidTank processingFluid = new FluidTank(1600) {

        @Override
        protected void onContentsChanged() {
            markDirty();
        }
    };

    private Status status = Status.Idle;
    private final FluidItemRecipeProcessor<ItemStack, FluidToItemGrouping<EndForgeRecipe>, EndForgeRecipe> recipeProcessor;
    private int ticksRun;

    public EndForgeTile() {
        recipeProcessor = new FluidItemRecipeProcessor<>(EndForgeHandler.getInstance());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        ticksRun = compound.getInteger("ticksRun");

        if (compound.hasKey("inputInv"))
            intput.deserializeNBT(compound.getCompoundTag("inputInv"));
        if (compound.hasKey("outInventory"))
            output.deserializeNBT(compound.getCompoundTag("outInv"));
        if (compound.hasKey("processInv"))
            processingInventory.deserializeNBT(compound.getCompoundTag("processInv"));

        if (compound.hasKey("inputFluid")) {
            var stack = FluidStack.loadFluidStackFromNBT(compound.getCompoundTag("inputFluid"));
            if (Objects.nonNull(stack)) inputTank.setFluid(stack);
        }
        if (compound.hasKey("processFluid")) {
            var stack = FluidStack.loadFluidStackFromNBT(compound.getCompoundTag("processFluid"));
            if (Objects.nonNull(stack)) processingFluid.setFluid(stack);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("ticksRun", ticksRun);
        compound.setTag("inputInv", intput.serializeNBT());
        compound.setTag("outInventory", output.serializeNBT());
        compound.setTag("processInv", processingInventory.serializeNBT());

        if (Objects.nonNull(inputTank.getFluid()))
            compound.setTag("inputFluid", inputTank.getFluid().writeToNBT(new NBTTagCompound()));
        if (Objects.nonNull(processingFluid.getFluid()))
            compound.setTag("processFluid", processingFluid.getFluid().writeToNBT(new NBTTagCompound()));
        return super.writeToNBT(compound);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (prepare()) progress();
        }
    }

    private boolean prepare() {
        var processingItemStack = processingInventory.getStackInSlot(0);
        var processingFluidStack = this.processingFluid.getFluid();
        boolean validProcessingRecipe = true;
        if (processingItemStack.isEmpty() || Objects.isNull(processingFluidStack)) {
            if (!(processingItemStack.isEmpty() && Objects.isNull(processingFluidStack)))
                EndReborn.LOGGER.warn("Clearing processing items in {} at {}, items {}, {}",
                        this.getClass(), this.pos, processingItemStack, processingFluidStack);
            updateStatus(Status.Idle);
            resetProcessingInvAndFluid();
            validProcessingRecipe = false;
        }

        if (validProcessingRecipe) {
            if (!recipeProcessor.validateGrouping(processingFluidStack)) return false;
            if (!recipeProcessor.validateRecipe(processingItemStack)) return false;

        } else {
            boolean invalid = false;

            var inputFluidStack = inputTank.getFluid();
            if (!recipeProcessor.validateGrouping(inputFluidStack)) invalid = true;

            var inputItem = intput.getStackInSlot(0);
            if (!recipeProcessor.validateRecipe(inputItem)) invalid = true;

            if (invalid) {
                if ((Objects.nonNull(inputFluidStack) && inputFluidStack.amount == 0) && inputItem.isEmpty()) {
                    updateStatus(Status.Idle);
                } else updateStatus(Status.Invalid);
                return false;
            }

            var fluidStackToMatch = recipeProcessor.getRecipeGrouping().getGrouping();
            var drainedFluidStack = inputTank.drain(fluidStackToMatch, false);

            if (!intput.extractItem(0, recipeProcessor.getRecipe().getInput().getCount(), true).isEmpty() &&
                    Objects.nonNull(drainedFluidStack) && drainedFluidStack.amount == fluidStackToMatch.amount) {
                processingInventory.insertItem(0,
                        intput.extractItem(0, recipeProcessor.getRecipe().getInput().getCount(), false),
                        false);

                processingFluid.fill(inputTank.drain(fluidStackToMatch, true), true);
            } else {
                updateStatus(Status.Invalid);
                return false;
            }
        }
        return true;
    }

    private void progress() {
        // finished
        if (ticksRun >= recipeProcessor.getRecipe().getTicksToComplete()) {
            var outputStack = recipeProcessor.getRecipe().getRecipeFunction().apply(
                    processingFluid.getFluid(),
                    processingInventory.getStackInSlot(0));
            if (output.insertItem(-1, outputStack, true).isEmpty()) {
                output.insertItem(-1, outputStack, false);
                world.playSound(null, pos, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                resetProcessingInvAndFluid();
                reset();
            } else updateStatus(Status.OutFull);
        } else {
            updateStatus(Status.Running);
            ticksRun++;
        }
    }

    private void resetProcessingInvAndFluid() {
        processingInventory.setStackInSlot(0, ItemStack.EMPTY);
        processingFluid.setFluid(null);
    }

    private void reset() {
        ticksRun = 0;
        markDirty();
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
                var newStatus = (String) getValue().getValue();
                var trueStatus = Status.valueOf(newStatus);;
                IKey.lang(trueStatus.langKey).drawAtZero(context, getArea());
            }
        }

        var textBox = new BasicTextWidget().left(20).top(3).right(20).background(GuiTextures.BACKGROUND);
        textBox.setValue(new StringSyncValue(() -> status.name(), null));
        panel.child(textBox);

        class BasicTextSyncingWidget extends Widget<BasicTextSyncingWidget> {

            @Override
            public void setValue(IValue<?> value) {
                super.setValue(value);
            }
        }

        // TODO: 28/10/2023 center
        panel.child(new FluidSlot().syncHandler(new FluidSlotSyncHandler(inputTank)).left(25).top(45));
        panel.child(new ItemSlot().slot(new ModularSlot(intput, 0)).left(50).top(45));
        panel.child(new ItemSlot().slot(new ModularSlot(output, 0).accessibility(false, true)).left(120).top(45));

        var process = new ProgressWidget()
                .size(18).left(95).top(45)
                .texture(GuiTextures.PROGRESS_ARROW, 25)
                .value(new DoubleSyncValue(() -> {
                    if (recipeProcessor.getRecipe() != null)
                        return (double) ticksRun / recipeProcessor.getRecipe().getTicksToComplete();
                    return 0;
                }, null));
        panel.child(process);

        return panel;
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null) {
            switch (facing) {
                case NORTH, SOUTH, WEST, EAST, DOWN -> {
                    return true;
                }
                case UP -> {
                    return false;
                }
            }
        }

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
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null) {
            switch (facing) {
                case NORTH, SOUTH, WEST, EAST, DOWN -> {
                    return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(inputTank);
                }
            }
        }

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != null) {
            switch (facing) {
                case NORTH, SOUTH, WEST, EAST -> {
                    return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(intput);
                }
                case DOWN -> {
                    return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(output);
                }
            }
        }

        return super.getCapability(capability, facing);
    }

    private void updateStatus(Status status) {
        if (this.status != status) {
            this.status = status;
        }
    }

    enum Status {

        Idle("tile.materializer.gui.idle"),
        OutFull("tile.materializer.gui.output_full"),
        Failed("tile.materializer.gui.failed"),
        Invalid("tile.materializer.gui.invalid_recipe"),

        Running("tile.materializer.gui.running");

        private final String langKey;

        Status(String langKey) {
            this.langKey = langKey;
        }
    }
}
