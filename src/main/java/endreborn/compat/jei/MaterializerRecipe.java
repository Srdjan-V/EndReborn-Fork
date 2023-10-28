package endreborn.compat.jei;

import net.minecraft.item.ItemStack;

import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

public class MaterializerRecipe implements IRecipeWrapper {

    private final ItemStack input, catalyst;
    private final ItemStack output;

    public MaterializerRecipe(ItemStack input, ItemStack catalyst, ItemStack output) {
        this.input = input;
        this.catalyst = catalyst;
        this.output = output;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, Lists.newArrayList(input, catalyst));
        ingredients.setOutput(VanillaTypes.ITEM, output);
    }
}
