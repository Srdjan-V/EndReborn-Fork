package endreborn.compat.jei.materializer;

import static endreborn.utils.TooltipUtils.addBeforeLast;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;

import com.google.common.collect.Lists;

import endreborn.utils.LangUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

public class MaterializerRecipe implements IRecipeWrapper {

    private final List<String> craftDescription;
    private final ItemStack input1, input2;
    private final String input1Tooltip, input2Tooltip;
    private final ItemStack output;

    public MaterializerRecipe(ItemStack input1, String input1Tooltip,
                              ItemStack input2, String input2Tooltip,
                              ItemStack output, List<String> craftDescription) {
        this.input1 = input1;
        this.input1Tooltip = input1Tooltip;
        this.input2 = input2;
        this.input2Tooltip = input2Tooltip;
        this.output = output;
        this.craftDescription = craftDescription;
    }

    public void applyInput1Tooltip(List<String> tooltip) {
        addBeforeLast(tooltip, input1Tooltip);
    }

    public void applyInput2Tooltip(List<String> tooltip) {
        addBeforeLast(tooltip, input2Tooltip);
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (mouseX > 141 && mouseX < 141 + 16 && mouseY > 41 && mouseY < 41 + 16) {
            return craftDescription.stream().map(LangUtil::translateToLocal).collect(Collectors.toList());
        }
        return IRecipeWrapper.super.getTooltipStrings(mouseX, mouseY);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, Lists.newArrayList(input2, input1));
        ingredients.setOutput(VanillaTypes.ITEM, output);
    }
}
