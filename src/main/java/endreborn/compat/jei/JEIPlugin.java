package endreborn.compat.jei;

import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;

import endreborn.client.gui.GuiEUser;
import endreborn.common.blocks.ContainerEntropyUser;
import endreborn.utils.RecipesUser;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helpers = registry.getJeiHelpers();
        final IGuiHelper gui = helpers.getGuiHelper();

        registry.addRecipeCategories(new EUserCategory(gui));
    }

    @Override
    public void register(IModRegistry registry) {
        IRecipeTransferRegistry recipeTransfer = registry.getRecipeTransferRegistry();

        registry.addRecipes(getRecipes(), EUserCategory.USER);
        registry.addRecipeClickArea(GuiEUser.class, 109, 43, 24, 17, EUserCategory.USER);
        recipeTransfer.addRecipeTransferHandler(ContainerEntropyUser.class, EUserCategory.USER, 0, 1, 3, 36);
    }

    public static List<EUserRecipe> getRecipes() {
        RecipesUser instance = RecipesUser.getInstance();
        Table<ItemStack, ItemStack, ItemStack> recipes = instance.getDualSmeltingList();
        List<EUserRecipe> jeiRecipes = Lists.newArrayList();

        for (Map.Entry<ItemStack, Map<ItemStack, ItemStack>> entry : recipes.columnMap().entrySet()) {
            for (Map.Entry<ItemStack, ItemStack> ent : entry.getValue().entrySet()) {
                ItemStack input0 = entry.getKey();
                ItemStack input1 = entry.getKey();
                ItemStack input2 = ent.getKey();
                ItemStack output = ent.getValue();

                List<ItemStack> inputs = Lists.newArrayList(input1, input2, input0);
                EUserRecipe recipe = new EUserRecipe(inputs, output);
                jeiRecipes.add(recipe);
            }
        }

        return jeiRecipes;
    }

    @SuppressWarnings("deprecation")
    public static String translateToLocal(String key) {
        if (I18n.canTranslate(key)) {
            return I18n.translateToLocal(key);
        }
        return I18n.translateToFallback(key);
    }

    public static String translateToLocalFormatted(String key, Object... format) {
        String string = translateToLocal(key);
        try {
            return String.format(string, format);
        } catch (IllegalFormatException exception) {
            return "FormatError: " + string;
        }
    }
}
