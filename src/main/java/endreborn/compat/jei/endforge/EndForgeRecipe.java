package endreborn.compat.jei.endforge;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

public class EndForgeRecipe implements IRecipeWrapper {

    private final FluidStack input1;
    private final ItemStack input2, output;

    public EndForgeRecipe(FluidStack input1, ItemStack input2, ItemStack output) {
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.FLUID, input1);
        ingredients.setInput(VanillaTypes.ITEM, input2);
        ingredients.setOutput(VanillaTypes.ITEM, output);
    }
}
