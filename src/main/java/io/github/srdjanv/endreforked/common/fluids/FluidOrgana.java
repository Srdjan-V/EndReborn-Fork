package io.github.srdjanv.endreforked.common.fluids;

import io.github.srdjanv.endreforked.common.fluids.base.FluidTextures;
import net.minecraftforge.fluids.Fluid;

public class FluidOrgana extends Fluid {
    public static final int COLOUR = 0xffe580ff;

    public FluidOrgana() {
        super("organa",
                FluidTextures.STILL,
                FluidTextures.FLOWING,
                COLOUR);
    }
}
