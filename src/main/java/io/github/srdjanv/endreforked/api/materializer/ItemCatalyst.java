package io.github.srdjanv.endreforked.api.materializer;

import net.minecraft.item.ItemStack;

import io.github.srdjanv.endreforked.api.base.groupings.ItemToItemGrouping;

public class ItemCatalyst extends ItemToItemGrouping<MaterializerRecipe> {

    public ItemCatalyst(ItemStack catalyst) {
        super(catalyst);
    }
}
