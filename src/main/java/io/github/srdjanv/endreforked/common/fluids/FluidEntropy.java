package io.github.srdjanv.endreforked.common.fluids;

import io.github.srdjanv.endreforked.common.fluids.base.BaseFluid;
import io.github.srdjanv.endreforked.common.fluids.base.FluidTextures;

public class FluidEntropy extends BaseFluid {
    public FluidEntropy() {
        super("entropy",
                FluidTextures.STILL,
                FluidTextures.FLOWING,
                0xff321782);
    }
}
