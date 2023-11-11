package endreborn.compat.jei;

import java.util.IllegalFormatException;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import endreborn.common.ModBlocks;
import endreborn.common.ModItems;
import endreborn.compat.jei.endforge.EndForgeCategory;
import endreborn.compat.jei.entropywand.EntropyWandCategory;
import endreborn.compat.jei.materializer.MaterializerCategory;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helpers = registry.getJeiHelpers();
        final IGuiHelper gui = helpers.getGuiHelper();

        registry.addRecipeCategories(new EndForgeCategory(gui));
        registry.addRecipeCategories(new MaterializerCategory(gui));
        registry.addRecipeCategories(new EntropyWandCategory(gui));
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipes(EndForgeCategory.getRecipes(), EndForgeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.BLOCK_END_FORGE.get()), EndForgeCategory.UID);

        registry.addRecipes(MaterializerCategory.getRecipes(), MaterializerCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.MATERIALIZER.get()), MaterializerCategory.UID);

        registry.addRecipes(EntropyWandCategory.getRecipes(), EntropyWandCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.ENTROPY_WAND.get()), EntropyWandCategory.UID);
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
