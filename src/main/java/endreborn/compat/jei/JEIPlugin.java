package endreborn.compat.jei;

import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;

import endreborn.client.gui.GuiEUser;
import endreborn.common.ModBlocks;
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

        registry.addRecipeCategories(new MaterializerCategory(gui));
    }

    @Override
    public void register(IModRegistry registry) {
        IRecipeTransferRegistry recipeTransfer = registry.getRecipeTransferRegistry();

        registry.addRecipes(getRecipes(), MaterializerCategory.UID);
        registry.addRecipeClickArea(GuiEUser.class, 109, 43, 24, 17, MaterializerCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.BLOCK_E_USER.get()), MaterializerCategory.UID);
        recipeTransfer.addRecipeTransferHandler(ContainerEntropyUser.class, MaterializerCategory.UID, 0, 1, 3, 36);
    }

    public static List<MaterializerRecipe> getRecipes() {
        RecipesUser instance = RecipesUser.getInstance();
        Table<ItemStack, ItemStack, ItemStack> recipes = instance.getDualSmeltingList();
        List<MaterializerRecipe> jeiRecipes = Lists.newArrayList();

        for (Map.Entry<ItemStack, Map<ItemStack, ItemStack>> entry : recipes.columnMap().entrySet()) {
            for (Map.Entry<ItemStack, ItemStack> ent : entry.getValue().entrySet()) {
                ItemStack input0 = entry.getKey();
                ItemStack input1 = entry.getKey();
                ItemStack input2 = ent.getKey();
                ItemStack output = ent.getValue();

                List<ItemStack> inputs = Lists.newArrayList(input1, input2, input0);
                MaterializerRecipe recipe = new MaterializerRecipe(inputs, output);
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
