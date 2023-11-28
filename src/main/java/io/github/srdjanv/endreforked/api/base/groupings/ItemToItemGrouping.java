package io.github.srdjanv.endreforked.api.base.groupings;

import net.minecraft.item.ItemStack;

import io.github.srdjanv.endreforked.api.base.Recipe;
import io.github.srdjanv.endreforked.api.util.ItemStackHashStrategy;

public class ItemToItemGrouping<R extends Recipe<ItemStack, ItemStack, ?>>
                               extends RecipeGrouping<ItemStack, ItemStack, R> {

    public static final ItemStackHashStrategy defaultStrategy = ItemStackHashStrategy.comparingAllButCount();

    public ItemToItemGrouping(ItemStack catalyst, ItemStackHashStrategy strategy) {
        super(catalyst, strategy);
    }

    public ItemToItemGrouping(ItemStack catalyst) {
        super(catalyst, defaultStrategy);
    }
}
