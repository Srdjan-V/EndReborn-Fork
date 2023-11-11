package endreborn.api.endforge;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import endreborn.api.base.HandlerRegistry;
import endreborn.api.base.groupings.FluidToItemGrouping;
import endreborn.api.util.FluidStackHashStrategy;

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
