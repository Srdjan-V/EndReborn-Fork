package endreborn.compat.jei.materializer;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;

import com.google.common.collect.Lists;

import endreborn.compat.jei.JEIPlugin;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

public class MaterializerRecipe implements IRecipeWrapper {

    private final List<String> craftDescription;
    private final ItemStack input, catalyst;
    private final ItemStack output;

    public MaterializerRecipe(ItemStack input, ItemStack catalyst,
                              ItemStack output, List<String> craftDescription) {
        this.input = input;
        this.catalyst = catalyst;
        this.output = output;
        this.craftDescription = craftDescription;
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (mouseX > 141 && mouseX < 141 + 16 && mouseY > 41 && mouseY < 41 + 16) {
            return craftDescription.stream().map(JEIPlugin::translateToLocal).collect(Collectors.toList());
        }
        return IRecipeWrapper.super.getTooltipStrings(mouseX, mouseY);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, Lists.newArrayList(input, catalyst));
        ingredients.setOutput(VanillaTypes.ITEM, output);
    }
}
