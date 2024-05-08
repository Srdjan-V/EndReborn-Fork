package io.github.srdjanv.endreforked.common.tiles;

import io.github.srdjanv.endreforked.common.entropy.chunks.EntropyChunkDataWrapper;
import io.github.srdjanv.endreforked.common.entropy.chunks.EntropyChunkInducer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class OrganaWeedTile extends TileEntity implements ITickable {
    private final EntropyChunkDataWrapper.TileEntity tileWrapper;
    private final EntropyChunkInducer<TileEntity> inducer;

    public OrganaWeedTile() {
        this.tileWrapper = new EntropyChunkDataWrapper.TileEntity();
        this.inducer = new EntropyChunkInducer<>(this, tileWrapper, 2 * 20, 0);
    }

    @Override public void update() {
        inducer.induce();
    }
}
