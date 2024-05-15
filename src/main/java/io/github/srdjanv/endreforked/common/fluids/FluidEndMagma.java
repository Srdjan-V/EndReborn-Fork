package io.github.srdjanv.endreforked.common.fluids;

import io.github.srdjanv.endreforked.common.fluids.base.FluidTextures;
import net.minecraftforge.fluids.Fluid;

public class FluidEndMagma extends Fluid {
    public FluidEndMagma() {
        super("end_magma",
                FluidTextures.MOLTEN_STILL,
                FluidTextures.MOLTEN_FLOWING,
                0xff321782);

        setLuminosity(15).setDensity(3500).setViscosity(4500).setTemperature(1600);
        //setBlock(ModBlocks.BLOCK_FLUID_END_MAGMA.get());
    }
}
