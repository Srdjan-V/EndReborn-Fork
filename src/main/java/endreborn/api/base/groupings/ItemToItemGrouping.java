package endreborn.api.base.groupings;

import net.minecraft.item.ItemStack;

import endreborn.api.base.Recipe;
import endreborn.api.util.ItemStackHashStrategy;

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
