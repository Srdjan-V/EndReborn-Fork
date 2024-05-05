package io.github.srdjanv.endreforked.api.entropy.chamber;

import io.github.srdjanv.endreforked.api.util.ItemStackHashStrategy;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.item.ItemStack;

public class EntropyItemChamberHandler extends EntropyChamberHandler<ItemStack, ItemChamberRecipe> {
    public static final EntropyItemChamberHandler INSTANCE = new EntropyItemChamberHandler();

    private EntropyItemChamberHandler() {
    }

    @Override public Hash.Strategy<ItemStack> getHashStrategy() {
        return ItemStackHashStrategy.memorizedComparingAllButCount();
    }
}
