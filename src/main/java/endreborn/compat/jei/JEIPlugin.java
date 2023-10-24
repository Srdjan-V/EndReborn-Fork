package endreborn.compat.jei;

import java.util.IllegalFormatException;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import com.google.common.collect.Lists;

import endreborn.api.materializer.MaterializerHandler;
import endreborn.client.gui.MaterializerContainer;
import endreborn.client.gui.MaterializerGui;
import endreborn.common.ModBlocks;
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
        registry.addRecipeClickArea(MaterializerGui.class, 109, 43, 24, 17, MaterializerCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.BLOCK_E_USER.get()), MaterializerCategory.UID);
        recipeTransfer.addRecipeTransferHandler(MaterializerContainer.class, MaterializerCategory.UID, 0, 1, 3, 36);
    }

    public static List<MaterializerRecipe> getRecipes() {
        List<MaterializerRecipe> jeiRecipes = Lists.newArrayList();
        for (endreborn.api.materializer.MaterializerRecipe value : MaterializerHandler.getRecipes()) {
            List<ItemStack> inputs = Lists.newArrayList(value.input);
            // TODO: 23/10/2023 improve
            MaterializerRecipe recipe = new MaterializerRecipe(inputs,
                    value.output.apply(value.input, MaterializerHandler.getCatalysts().stream().findFirst().get()));
            jeiRecipes.add(recipe);
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
