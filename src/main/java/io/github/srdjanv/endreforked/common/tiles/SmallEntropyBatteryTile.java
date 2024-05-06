package io.github.srdjanv.endreforked.common.tiles;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import io.github.srdjanv.endreforked.common.entropy.chunks.EntropyChunkDataWrapper;
import io.github.srdjanv.endreforked.common.entropy.storage.DefaultEntropyStorage;
import io.github.srdjanv.endreforked.common.widgets.BasicTextWidget;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class SmallEntropyBatteryTile extends TileEntity implements IGuiHolder<PosGuiData>, ITickable {
    private final EntropyChunkDataWrapper<TileEntity> wrapper;
    private final DefaultEntropyStorage storage;
    private boolean linkDirt = false;
    private boolean linked;

    private int chunkEntropy;

    public SmallEntropyBatteryTile() {
        wrapper = new EntropyChunkDataWrapper.TileEntity();
        storage = new DefaultEntropyStorage(250);
    }

    @Override public void update() {
        if (!linkDirt) {
            var data = wrapper.getCenterEntropy(this);
            if (data != null) data.setEntropyStorageReference(storage, true);
            linkDirt = true;
        }
    }

    @Override public ModularPanel buildUI(PosGuiData data, GuiSyncManager syncManager) {
        ModularPanel panel = ModularPanel.defaultPanel("small_entropy_battery_gui").bindPlayerInventory();
        syncManager.syncValue("link_dirt", new BooleanSyncValue(() -> linkDirt, linkDirt -> this.linkDirt = linkDirt));
        syncManager.syncValue("linked", new BooleanSyncValue(() -> linked, forced -> this.linked = forced));
        syncManager.syncValue("chunk_entropy", new IntSyncValue(
                () -> wrapper.getEntropyView(this).getMaxEntropy(),
                chunkEntropy -> this.chunkEntropy = chunkEntropy));

        if (data.isClient()) {
            var chunkEntropyTest = new BasicTextWidget().top(10).size(60, 16);
            chunkEntropyTest.setKey(() -> String.valueOf(chunkEntropy));
            panel.child(chunkEntropyTest);

            var linkedTextBox = new BasicTextWidget().top(26).size(60, 16);
            linkedTextBox.setKey(() -> String.valueOf(linked));
            panel.child(linkedTextBox);

            var linkButton = new ButtonWidget<>().size(16).top(10).right(10);
            linkButton.onMousePressed(mouseButton -> {
                if (mouseButton != 0) return false;
                linkDirt = !linkDirt;
                linked = !linked;
                return true;
            });
            panel.child(linkButton);

 /*           var itemInput = new ItemSlot().slot(new ModularSlot(inputInventory, 0)).left(50).top(45);
            itemInput.tooltip().setAutoUpdate(true).tooltipBuilder(tooltip -> {
                tooltip.addLine(biRecipeProcessor.getHandlerRegistry().translateHashStrategy());
            });
            panel.child(itemInput);

            var itemInput2 = new ItemSlot().slot(new ModularSlot(inputInventory, 1)).left(70).top(45);
            itemInput2.tooltip().setAutoUpdate(true).tooltipBuilder(tooltip -> {
                if (!biRecipeProcessor.hasRecipeGroupingRecipe()) return;
                tooltip.addLine(biRecipeProcessor.getRecipeGrouping().translateHashStrategy());
            });
            panel.child(itemInput2);*/
        }
        return panel;
    }

    @Override public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        linked = compound.getBoolean("force");
    }

    @Override public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("forced", linked);
        return super.writeToNBT(compound);
    }
}
