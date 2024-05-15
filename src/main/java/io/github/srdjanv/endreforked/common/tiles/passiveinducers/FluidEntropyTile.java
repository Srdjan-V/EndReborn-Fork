package io.github.srdjanv.endreforked.common.tiles.passiveinducers;

import io.github.srdjanv.endreforked.api.entropy.EntropyRadius;
import io.github.srdjanv.endreforked.common.fluids.FluidOrgana;
import io.github.srdjanv.endreforked.common.tiles.base.BasePassiveInducer;
import net.minecraft.util.EnumParticleTypes;

public class FluidEntropyTile extends BasePassiveInducer {
    public FluidEntropyTile() {
        super(EntropyRadius.TWO, 5 * 20, 25, 60);
    }

    @Override protected void particles() {
        spawnParticles(FluidOrgana.COLOUR, EnumParticleTypes.SPELL);
    }
}
