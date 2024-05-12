package io.github.srdjanv.endreforked.common.tiles;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.PosGuiData;
import io.github.srdjanv.endreforked.api.entropy.EntropyRange;
import io.github.srdjanv.endreforked.common.entropy.chunks.EntropyChunkDataWrapper;
import io.github.srdjanv.endreforked.common.entropy.chunks.EntropyChunkInducer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class FluidEntropyTile extends TileEntity implements ITickable {
    private final EntropyChunkDataWrapper<TileEntity> tileWrapper;
    private final EntropyChunkInducer<TileEntity> inducer;

    public FluidEntropyTile() {
        this.tileWrapper = new EntropyChunkDataWrapper.TileEntity(EntropyRange.TWO);
        this.inducer = new EntropyChunkInducer<>(this, tileWrapper, 5 * 20, 25);
    }

    @Override public void update() {
        inducer.induce();
    }
}
