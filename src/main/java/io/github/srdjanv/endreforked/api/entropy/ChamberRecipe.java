package io.github.srdjanv.endreforked.api.entropy;

import io.github.srdjanv.endreforked.api.base.Recipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Function;

public class ChamberRecipe<IN, OUT> extends Recipe<IN, OUT> {
    private final int entropyCost;

    public ChamberRecipe(IN input, int ticksToComplete, int entropyCost, Function<IN, OUT> recipeFunction) {
        super(input, ticksToComplete, recipeFunction);
        this.entropyCost = entropyCost;
    }

    public int getEntropyCost() {
        return entropyCost;
    }

    public static class Fluid extends ChamberRecipe<FluidStack, FluidStack> {
        public Fluid(FluidStack input, int ticksToComplete, int entropyCost, Function<FluidStack, FluidStack> recipeFunction) {
            super(input, ticksToComplete, entropyCost, recipeFunction);
        }
    }

    public static class Item extends ChamberRecipe<ItemStack, ItemStack> {
        public Item(ItemStack input, int ticksToComplete, int entropyCost, Function<ItemStack, ItemStack> recipeFunction) {
            super(input, ticksToComplete, entropyCost, recipeFunction);
        }
    }
}
