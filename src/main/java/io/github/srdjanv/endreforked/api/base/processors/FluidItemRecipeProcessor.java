package io.github.srdjanv.endreforked.api.base.processors;

import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import io.github.srdjanv.endreforked.api.base.HandlerGroupingRegistry;
import io.github.srdjanv.endreforked.api.base.BiRecipe;
import io.github.srdjanv.endreforked.api.base.groupings.RecipeGrouping;

public class FluidItemRecipeProcessor<OUT,
        RG extends RecipeGrouping<FluidStack, ItemStack, R>,
        R extends BiRecipe<FluidStack, ItemStack, OUT>>
                                     extends BiRecipeProcessor<FluidStack, ItemStack, OUT, RG, R> {

    public FluidItemRecipeProcessor(HandlerGroupingRegistry<FluidStack, ItemStack, OUT, RG, R> handlerGroupingRegistry) {
        super(handlerGroupingRegistry);
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
