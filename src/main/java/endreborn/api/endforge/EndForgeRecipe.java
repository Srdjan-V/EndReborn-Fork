package endreborn.api.endforge;

import java.util.function.BiFunction;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import endreborn.api.base.Recipe;

public class EndForgeRecipe extends Recipe<FluidStack, ItemStack, ItemStack> {

    public EndForgeRecipe(ItemStack input, int ticksToComplete,
                          BiFunction<FluidStack, ItemStack, ItemStack> recipeFunction) {
        super(input, ticksToComplete, recipeFunction);
    }
}
