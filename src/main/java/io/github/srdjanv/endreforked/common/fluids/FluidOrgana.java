package io.github.srdjanv.endreforked.common.fluids;

import io.github.srdjanv.endreforked.common.fluids.base.FluidTextures;
import net.minecraftforge.fluids.Fluid;

public class FluidOrgana extends Fluid {
    public FluidOrgana() {
        super("organa",
                FluidTextures.STILL,
                FluidTextures.FLOWING,
                0xffe580ff);
    }
}
