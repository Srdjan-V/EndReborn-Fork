package io.github.srdjanv.endreforked.api.base.processors;

import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import io.github.srdjanv.endreforked.api.base.HandlerRegistry;
import io.github.srdjanv.endreforked.api.base.Recipe;
import io.github.srdjanv.endreforked.api.base.groupings.RecipeGrouping;

public class FluidItemRecipeProcessor<OUT,
        RG extends RecipeGrouping<FluidStack, ItemStack, R>,
        R extends Recipe<FluidStack, ItemStack, OUT>>
                                     extends RecipeProcessor<FluidStack, ItemStack, OUT, RG, R> {

    public FluidItemRecipeProcessor(HandlerRegistry<FluidStack, ItemStack, OUT, RG, R> handlerRegistry) {
        super(handlerRegistry);
    }

    @Override
    public boolean validateGrouping(FluidStack input) {
        if (Objects.isNull(input)) return false;
        if (input.amount == 0) return false;
        return super.validateGrouping(input);
    }

    @Override
    public boolean validateRecipe(ItemStack input) {
        if (Objects.isNull(input)) return false;
        if (input.isEmpty()) return false;
        return super.validateRecipe(input);
    }
}
