package io.github.srdjanv.endreforked.api.base.groupings;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import io.github.srdjanv.endreforked.api.base.Recipe;
import io.github.srdjanv.endreforked.api.util.ItemStackHashStrategy;
import it.unimi.dsi.fastutil.Hash;

public class FluidToItemGrouping<R extends Recipe<FluidStack, ItemStack, ?>>
                                extends RecipeGrouping<FluidStack, ItemStack, R> {

    public FluidToItemGrouping(FluidStack grouping, Hash.Strategy<ItemStack> strategy) {
        super(grouping, strategy);
    }

    public FluidToItemGrouping(FluidStack grouping) {
        super(grouping, ItemStackHashStrategy.memorizedComparingAllButCount());
    }
}
