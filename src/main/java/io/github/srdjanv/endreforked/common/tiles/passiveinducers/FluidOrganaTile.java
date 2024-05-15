package io.github.srdjanv.endreforked.common.tiles.passiveinducers;

import io.github.srdjanv.endreforked.api.entropy.EntropyRadius;
import io.github.srdjanv.endreforked.common.fluids.FluidOrgana;
import io.github.srdjanv.endreforked.common.tiles.base.BasePassiveInducer;
import net.minecraft.util.EnumParticleTypes;

public class FluidOrganaTile extends BasePassiveInducer {

    public FluidOrganaTile() {
        super(EntropyRadius.TWO, 5 * 20, 30, 60);
    }

    @Override protected void particles() {
        spawnParticles(FluidOrgana.COLOUR, EnumParticleTypes.SPELL_MOB);
    }
}
