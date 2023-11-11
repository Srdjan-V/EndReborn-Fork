package endreborn.api.materializer;

import net.minecraft.item.ItemStack;

import endreborn.api.base.groupings.ItemToItemGrouping;

public class ItemCatalyst extends ItemToItemGrouping<MaterializerRecipe> {

    public ItemCatalyst(ItemStack catalyst) {
        super(catalyst);
    }
}
