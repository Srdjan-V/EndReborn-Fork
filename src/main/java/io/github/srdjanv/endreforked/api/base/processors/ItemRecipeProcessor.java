package io.github.srdjanv.endreforked.api.base.processors;

import java.util.Objects;

import net.minecraft.item.ItemStack;

import io.github.srdjanv.endreforked.api.base.HandlerRegistry;
import io.github.srdjanv.endreforked.api.base.Recipe;
import io.github.srdjanv.endreforked.api.base.groupings.RecipeGrouping;

public class ItemRecipeProcessor<OUT,
        RG extends RecipeGrouping<ItemStack, ItemStack, R>,
        R extends Recipe<ItemStack, ItemStack, OUT>>
                                extends RecipeProcessor<ItemStack, ItemStack, OUT, RG, R> {

    public ItemRecipeProcessor(HandlerRegistry<ItemStack, ItemStack, OUT, RG, R> handlerRegistry) {
        super(handlerRegistry);
    }

    @Override
    public boolean validateGrouping(ItemStack input) {
        if (Objects.isNull(input)) return false;
        if (input.isEmpty()) return false;
        return super.validateGrouping(input);
    }

    @Override
    public boolean validateRecipe(ItemStack input) {
        if (Objects.isNull(input)) return false;
        if (input.isEmpty()) return false;
        return super.validateRecipe(input);
    }
}
