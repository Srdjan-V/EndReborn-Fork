package endreborn.compat.jei;

import java.util.List;

import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import endreborn.utils.RecipesUser;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import org.jetbrains.annotations.NotNull;

public class EUserRecipe implements IRecipeWrapper {

    private final List<ItemStack> inputs;
    private final ItemStack output;

    public EUserRecipe(List<ItemStack> inputs, ItemStack output) {
        this.inputs = inputs;
        this.output = output;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, inputs);
        ingredients.setOutput(VanillaTypes.ITEM, output);
    }
}
