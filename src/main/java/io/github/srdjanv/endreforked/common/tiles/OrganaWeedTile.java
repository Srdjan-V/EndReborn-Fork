package io.github.srdjanv.endreforked.common.tiles;

import io.github.srdjanv.endreforked.common.capabilities.entropy.EntropyInducer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class OrganaWeedTile extends TileEntity implements ITickable {
    private final EntropyInducer inducer;

    public OrganaWeedTile() {
        this.inducer = new EntropyInducer(2 * 20);
    }

    @Override public void update() {
        inducer.induceEntropy(world, pos, 2);
    }
}
