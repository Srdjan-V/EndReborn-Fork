package io.github.srdjanv.endreforked.api.entropy;

import io.github.srdjanv.endreforked.api.base.Recipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Function;

public class ChamberRecipe<IN, OUT> extends Recipe<IN, OUT> {
    public ChamberRecipe(IN input, int ticksToComplete, Function<IN, OUT> recipeFunction) {
        super(input, ticksToComplete, recipeFunction);
    }

    public static class Fluid extends ChamberRecipe<FluidStack, FluidStack> {
        public Fluid(FluidStack input, int ticksToComplete, Function<FluidStack, FluidStack> recipeFunction) {
            super(input, ticksToComplete, recipeFunction);
        }
    }

    public static class Item extends ChamberRecipe<ItemStack, ItemStack> {
        public Item(ItemStack input, int ticksToComplete, Function<ItemStack, ItemStack> recipeFunction) {
            super(input, ticksToComplete, recipeFunction);
        }
    }
}
