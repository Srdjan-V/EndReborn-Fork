package endreborn.api.base.processors;

import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import endreborn.api.base.HandlerRegistry;
import endreborn.api.base.Recipe;
import endreborn.api.base.groupings.RecipeGrouping;

public class FluidItemRecipeProcessor<OUT,
        RG extends RecipeGrouping<FluidStack, ItemStack, R>,
        R extends Recipe<FluidStack, ItemStack, OUT>>
                                     extends RecipeProcessor<FluidStack, ItemStack, OUT, RG, R> {

    public FluidItemRecipeProcessor(HandlerRegistry<FluidStack, ItemStack, OUT, RG, R> handlerRegistry) {
        super(handlerRegistry);
    }

    @Override
    public boolean validateGrouping(FluidStack input) {
        if (Objects.isNull(input)) return false;
        if (input.amount == 0) return false;
        return super.validateGrouping(input);
    }

    @Override
    public boolean validateRecipe(ItemStack input) {
        if (Objects.isNull(input)) return false;
        if (input.isEmpty()) return false;
        return super.validateRecipe(input);
    }
}
