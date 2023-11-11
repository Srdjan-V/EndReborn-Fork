package endreborn.api.materializer;

import net.minecraft.item.ItemStack;

import endreborn.api.base.HandlerRegistry;
import endreborn.api.util.ItemStackHashStrategy;
import it.unimi.dsi.fastutil.Hash;

public final class MaterializerHandler extends
                                       HandlerRegistry<ItemStack, ItemStack, ItemStack, ItemCatalyst, MaterializerRecipe> {

    private static final MaterializerHandler instance = new MaterializerHandler();

    public static MaterializerHandler getInstance() {
        return instance;
    }

    @Override
    public Hash.Strategy<ItemStack> strategy() {
        return ItemStackHashStrategy.comparingAllButCount();
    }
}
