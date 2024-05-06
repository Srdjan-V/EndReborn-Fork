package io.github.srdjanv.endreforked.common.tiles;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widgets.FluidSlot;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import io.github.srdjanv.endreforked.api.base.processors.RecipeProcessor;
import io.github.srdjanv.endreforked.api.entropy.chamber.*;
import io.github.srdjanv.endreforked.common.entropy.EntropyRange;
import io.github.srdjanv.endreforked.common.entropy.chunks.EntropyChunkDataWrapper;
import io.github.srdjanv.endreforked.api.entropy.IEntropyDataProvider;
import io.github.srdjanv.endreforked.common.tiles.base.BaseTileEntity;
import io.github.srdjanv.endreforked.common.tiles.base.TileStatus;
import io.github.srdjanv.endreforked.common.widgets.BasicTextWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import java.io.IOException;

public class EntropyChamberTile extends BaseTileEntity implements ITickable, IEntropyDataProvider, IGuiHolder<PosGuiData> {
    private final EntropyChunkDataWrapper<TileEntity> reader;

    private TileStatus itemStatus = TileStatus.Idle;
    private double itemProgress = 0;
    private int itemsTicksRun;
    private final ItemStackHandler itemIn = new InternalItemStackHandler(1);
    private final ItemStackHandler itemOut = new InternalItemStackHandler(1);
    private final RecipeProcessor<ItemStack, ItemStack, ItemChamberRecipe> itemProcessor = new RecipeProcessor<>(EntropyItemChamberHandler.INSTANCE);

    private TileStatus fluidStatus = TileStatus.Idle;
    private double fluidProgress = 0;
    private int fluidsTicksRun;
    private final FluidTank fluidIn = new InternalFluidTank(20_000);
    private final FluidTank fluidOut = new InternalFluidTank(20_000);
    private final RecipeProcessor<FluidStack, FluidStack, FluidChamberRecipe> fluidProcessor = new RecipeProcessor<>(EntropyFluidChamberHandler.INSTANCE);

    public EntropyChamberTile() {
        reader = new EntropyChunkDataWrapper.TileEntity(EntropyRange.TWO);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("itemIn"))
            itemIn.deserializeNBT(compound.getCompoundTag("itemIn"));
        if (compound.hasKey("itemOut"))
            itemOut.deserializeNBT(compound.getCompoundTag("itemOut"));

        if (compound.hasKey("fluidIn"))
            fluidIn.readFromNBT(compound.getCompoundTag("fluidIn"));
        if (compound.hasKey("fluidOut"))
            fluidOut.readFromNBT(compound.getCompoundTag("fluidOut"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("itemIn", itemIn.serializeNBT());
        compound.setTag("itemOut", itemOut.serializeNBT());

        compound.setTag("fluidIn", fluidIn.writeToNBT(new NBTTagCompound()));
        compound.setTag("fluidOut", fluidOut.writeToNBT(new NBTTagCompound()));

        return super.writeToNBT(compound);
    }

    @Override public int getPassiveEntropyCost() {
        return 10;
    }

    @Override public boolean hasActiveEntropyCost() {
        return itemProcessor.hasRecipe() || fluidProcessor.hasRecipe();
    }

    @Override public int getActiveEntropyCost() {
        int cost = 0;
        if (itemProcessor.hasRecipe()) cost = itemProcessor.getRecipe().getEntropyCost();
        if (fluidProcessor.hasRecipe()) cost += fluidProcessor.getRecipe().getEntropyCost();
        return cost;
    }

    @Override public ModularPanel buildUI(PosGuiData data, GuiSyncManager syncManager) {
        ModularPanel panel = ModularPanel.defaultPanel("entropy_chamber_gui").bindPlayerInventory();

        syncManager.syncValue("invSync", new SyncHandler() {

            @Override
            public void readOnClient(int id, PacketBuffer buf) throws IOException {
                switch (id) {
                    case 0 -> itemIn.setStackInSlot(0, buf.readItemStack());
                    case 1 -> itemOut.setStackInSlot(0, buf.readItemStack());
                }
            }

            @Override
            public void readOnServer(int id, PacketBuffer buf) {}

            private ItemStack itemStackIn = ItemStack.EMPTY;
            private boolean valid;

            @Override
            public void detectAndSendChanges(boolean init) {
                if (init || invChanged()) {
                    sync(0, buffer -> buffer.writeItemStack(itemIn.getStackInSlot(0)));
                    sync(1, buffer -> buffer.writeItemStack(itemOut.getStackInSlot(0)));
                }
            }

            private boolean invChanged() {
                var changed = false;
                var valid = itemProcessor.hasRecipe();
                if (this.valid != valid) {
                    this.valid = valid;
                    changed = true;
                }
                if (valid) {
                    if (!itemProcessor.getHandlerRegistry().getHashStrategy()
                            .equals(itemIn.getStackInSlot(0), itemStackIn)) {
                        itemStackIn = itemIn.getStackInSlot(0);
                        changed = true;
                    }
                }
                return changed;
            }
        });
        syncManager.syncValue("fluidSync", new SyncHandler() {

            @Override
            public void readOnClient(int id, PacketBuffer buf) throws IOException {
                switch (id) {
                    case 0 -> fluidIn.readFromNBT(buf.readCompoundTag());
                    case 1 -> fluidOut.readFromNBT(buf.readCompoundTag());
                }
            }

            @Override
            public void readOnServer(int id, PacketBuffer buf) {}

            private FluidStack fluidStack;
            private boolean valid;

            @Override
            public void detectAndSendChanges(boolean init) {
                if (init || tanksChanged()) {
                    sync(0, buffer -> buffer.writeCompoundTag(fluidIn.writeToNBT(new NBTTagCompound())));
                    sync(1, buffer -> buffer.writeCompoundTag(fluidOut.writeToNBT(new NBTTagCompound())));
                }
            }

            private boolean tanksChanged() {
                var changed = false;
                var valid = fluidProcessor.hasRecipe();
                if (this.valid != valid) {
                    this.valid = valid;
                    changed = true;
                }
                if (valid) {
                    if (!fluidProcessor.getHandlerRegistry().getHashStrategy().equals(fluidIn.getFluid(), fluidStack)) {
                        fluidStack = fluidIn.getFluid();
                        changed = true;
                    }
                }
                return changed;
            }
        });

        syncManager.syncValue("itemStatus",
                SyncHandlers.enumValue(TileStatus.class, () -> itemStatus, status -> this.itemStatus = status));
        syncManager.syncValue("fluidStatus",
                SyncHandlers.enumValue(TileStatus.class, () -> fluidStatus, status -> this.fluidStatus = status));


        if (data.isClient()) {
            var itemTextBox = new BasicTextWidget().left(20).top(3).right(20).background(GuiTextures.MC_BACKGROUND);
            itemTextBox.setKey(() -> itemStatus.getLangKey());
            panel.child(itemTextBox);

            var fluidTextBox = new BasicTextWidget().left(20).top(20).right(20).background(GuiTextures.MC_BACKGROUND);
            fluidTextBox.setKey(() -> fluidStatus.getLangKey());
            panel.child(fluidTextBox);

            panel.child(new ProgressWidget()
                    .size(18).left(95).top(45)
                    .texture(GuiTextures.PROGRESS_ARROW, 25)
                    .progress(() -> {
                        if (itemProcessor.hasRecipe())
                            return (double) itemsTicksRun / itemProcessor.getRecipe().getTicksToComplete();
                        return 0;
                    }));

            panel.child(new ProgressWidget()
                    .size(18).left(95).top(30)
                    .texture(GuiTextures.PROGRESS_ARROW, 25)
                    .progress(() -> {
                        if (fluidProcessor.hasRecipe())
                            return (double) itemsTicksRun / fluidProcessor.getRecipe().getTicksToComplete();
                        return 0;
                    }));
        }

        // TODO: 28/10/2023 center
        var itemIn = new ItemSlot().slot(new ModularSlot(this.itemIn, 0)).left(50).top(45);
        itemIn.tooltip().setAutoUpdate(true).tooltipBuilder(tooltip -> {
            if (itemProcessor.hasRecipe())
                tooltip.addLine(itemProcessor.getHandlerRegistry().translateHashStrategy());
        });
        panel.child(itemIn);

        var itemOut = new ItemSlot().slot(new ModularSlot(this.itemOut, 1)).left(70).top(45);
        panel.child(itemOut);

        var fluidIn = new FluidSlot().syncHandler(new FluidSlotSyncHandler(this.fluidIn)).left(50).top(45);
        fluidIn.tooltip().setAutoUpdate(true).tooltipBuilder(tooltip -> {
            if (fluidProcessor.hasRecipe())
                tooltip.addLine(fluidProcessor.getHandlerRegistry().translateHashStrategy());
        });
        panel.child(itemIn);

        var fluidOut = new FluidSlot().syncHandler(new FluidSlotSyncHandler(this.fluidOut)).left(70).top(45);
        panel.child(fluidOut);

        return panel;
    }

    @Override public void update() {
        if (!world.isRemote) {
            if (prepareItems()) updateItems();
            if (prepareFluids()) updateFluids();
        }
    }

    private boolean prepareItems() {
        var processingItem = itemIn.getStackInSlot(0);
        boolean valid = false;
        if (itemProcessor.validateRecipe(processingItem)) {
            var data = reader.getCenterEntropy(this);
            if (data != null
                    && data.getCurrentEntropy() > itemProcessor.getRecipe().getEntropyCost())
                valid = true;
        }

        if (valid) {
            updateItemStatus(TileStatus.Running);
        } else updateItemStatus(TileStatus.Invalid);
        return valid;
    }

    private void updateItems() {
        // finished
        if (itemsTicksRun >= itemProcessor.getRecipe().getTicksToComplete()) {
            if (!drainEntropy(reader, itemProcessor)) {
                updateItemStatus(TileStatus.NotEnoughEntropy);
                return;
            }

            var outputStack = itemProcessor.getRecipe().getRecipeFunction().apply(itemOut.getStackInSlot(0));
            if (itemOut.insertItem(-1, outputStack, true).isEmpty()) {
                itemOut.insertItem(-1, outputStack, false);
            } else updateItemStatus(TileStatus.OutFull);
        } else {
            itemsTicksRun++;
        }
    }

    private void updateItemStatus(TileStatus status) {
        if (this.itemStatus != status) {
            this.itemStatus = status;
        }
    }

    private boolean prepareFluids() {
        var processingFluid = fluidIn.getFluid();
        boolean valid = false;
        if (fluidProcessor.validateRecipe(processingFluid)) {
            var data = reader.getCenterEntropy(this);
            if (data != null
                    && data.getCurrentEntropy() > fluidProcessor.getRecipe().getEntropyCost())
                valid = true;
        }

        if (valid) {
            updateItemStatus(TileStatus.Running);
        } else updateItemStatus(TileStatus.Invalid);
        return valid;

    }

    private void updateFluids() {
        // finished
        if (fluidsTicksRun >= fluidProcessor.getRecipe().getTicksToComplete()) {
            if (!drainEntropy(reader, fluidProcessor)) {
                updateItemStatus(TileStatus.NotEnoughEntropy);
                return;
            }

            var outputFluid = fluidProcessor.getRecipe().getRecipeFunction().apply(fluidOut.getFluid());
            if (fluidOut.fill(outputFluid, true) == outputFluid.amount) {
                fluidOut.fill(outputFluid, false);
            } else updateFluidStatus(TileStatus.OutFull);
        } else {
            fluidsTicksRun++;
        }
    }

    private void updateFluidStatus(TileStatus status) {
        if (this.fluidStatus != status) {
            this.fluidStatus = status;
        }
    }

    protected <R extends ChamberRecipe<?, ?>> boolean drainEntropy(EntropyChunkDataWrapper<TileEntity> reader, RecipeProcessor<?, ?, R> processor) {
        var data = reader.getCenterEntropy(this);
        if (data == null) throw new IllegalStateException("This should never happen");
        if (data.drainEntropy(processor.getRecipe().getEntropyCost(), true)
                == processor.getRecipe().getEntropyCost()) {
            data.drainEntropy(processor.getRecipe().getEntropyCost(), false);
            return true;
        }
        return false;
    }
}
