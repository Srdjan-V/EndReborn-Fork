package io.github.srdjanv.endreforked.api.fluids.entropy;

import io.github.srdjanv.endreforked.api.fluids.base.FluidAnyStateCollisionHandler;

public class EntropyFluidAnyStateCollisionHandler extends FluidAnyStateCollisionHandler {
    public static final EntropyFluidAnyStateCollisionHandler INSTANCE = new EntropyFluidAnyStateCollisionHandler();

    private EntropyFluidAnyStateCollisionHandler() {}
}
