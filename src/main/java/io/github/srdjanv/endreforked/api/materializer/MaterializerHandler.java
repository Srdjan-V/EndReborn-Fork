package io.github.srdjanv.endreforked.api.materializer;

import net.minecraft.item.ItemStack;

import io.github.srdjanv.endreforked.api.base.HandlerRegistry;
import io.github.srdjanv.endreforked.api.util.ItemStackHashStrategy;
import it.unimi.dsi.fastutil.Hash;

public final class MaterializerHandler extends
                                       HandlerRegistry<ItemStack, ItemStack, ItemStack, ItemCatalyst, MaterializerRecipe> {

    private static final MaterializerHandler instance = new MaterializerHandler();

    public static MaterializerHandler getInstance() {
        return instance;
    }

    @Override
    public Hash.Strategy<ItemStack> getHashStrategy() {
        return ItemStackHashStrategy.comparingAllButCount();
    }
}
