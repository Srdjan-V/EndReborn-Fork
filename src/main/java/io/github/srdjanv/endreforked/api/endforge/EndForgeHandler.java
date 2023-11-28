package io.github.srdjanv.endreforked.api.endforge;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import io.github.srdjanv.endreforked.api.base.HandlerRegistry;
import io.github.srdjanv.endreforked.api.base.groupings.FluidToItemGrouping;
import io.github.srdjanv.endreforked.api.util.FluidStackHashStrategy;

public class EndForgeHandler extends
                             HandlerRegistry<FluidStack, ItemStack, ItemStack, FluidToItemGrouping<EndForgeRecipe>, EndForgeRecipe> {

    private static final EndForgeHandler instance = new EndForgeHandler();

    public static EndForgeHandler getInstance() {
        return instance;
    }

    @Override
    public FluidStackHashStrategy getHashStrategy() {
        return FluidStackHashStrategy.memorizedComparingAll();
    }
}
