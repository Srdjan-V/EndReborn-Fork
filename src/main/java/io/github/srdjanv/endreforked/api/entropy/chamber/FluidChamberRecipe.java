package io.github.srdjanv.endreforked.api.entropy.chamber;

import net.minecraftforge.fluids.FluidStack;

import java.util.function.Function;

public class FluidChamberRecipe extends ChamberRecipe<FluidStack, FluidStack> {
    public FluidChamberRecipe(FluidStack input, int ticksToComplete, int entropyCost, Function<FluidStack, FluidStack> recipeFunction) {
        super(input, ticksToComplete, entropyCost, recipeFunction);
    }
}
