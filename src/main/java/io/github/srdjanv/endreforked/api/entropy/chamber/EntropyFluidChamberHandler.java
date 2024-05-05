package io.github.srdjanv.endreforked.api.entropy.chamber;

import io.github.srdjanv.endreforked.api.util.FluidStackHashStrategy;
import it.unimi.dsi.fastutil.Hash;
import net.minecraftforge.fluids.FluidStack;

public class EntropyFluidChamberHandler extends EntropyChamberHandler<FluidStack, FluidChamberRecipe> {
    public static final EntropyFluidChamberHandler INSTANCE = new EntropyFluidChamberHandler();

    private EntropyFluidChamberHandler() {
    }

    @Override public Hash.Strategy<FluidStack> getHashStrategy() {
        return FluidStackHashStrategy.memorizedComparingAllButAmount();
    }
}
