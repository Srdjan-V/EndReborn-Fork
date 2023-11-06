package endreborn.compat.jei.materializer;

import java.util.List;

import net.minecraft.client.Minecraft;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.drawable.GuiTextures;
import com.google.common.collect.Lists;

import endreborn.Reference;
import endreborn.api.materializer.Catalyst;
import endreborn.api.materializer.MaterializerHandler;
import endreborn.compat.jei.JEIPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;

public class MaterializerCategory implements IRecipeCategory<MaterializerRecipe> {

    public static final String UID = Reference.MODID + ".materializer";
    protected static final int input = 0;
    protected static final int catalyst = 1;
    protected static final int output = 3;

    private final IDrawable background;
    private final IDrawable slot;

    public MaterializerCategory(IGuiHelper helper) {
        background = helper.createBlankDrawable(160, 60);
        slot = helper.getSlotDrawable();
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(@NotNull Minecraft minecraft) {
        GuiTextures.PROGRESS_ARROW.getSubArea(0f, 0f, 1f, 0.5f).draw(78, 20, 20, 20);
        float progress = Minecraft.getSystemTime() % 2000 / 2000f; // 2 seconds
        GuiTextures.PROGRESS_ARROW.getSubArea(0f, 0.5f, progress, 1f).draw(78, 20, progress * 20, 20);
        slot.draw(minecraft, 105, 20);
        slot.draw(minecraft, 55, 20);
        slot.draw(minecraft, 35, 20);
        GuiTextures.HELP.draw(142, 42, 16, 16);
    }

    @Override
    public @NotNull String getTitle() {
        return JEIPlugin.translateToLocal("tile.materializer");
    }

    @Override
    public @NotNull String getModName() {
        return Reference.NAME;
    }

    @Override
    public @NotNull String getUid() {
        return UID;
    }

    public void setRecipe(IRecipeLayout recipeLayout, @NotNull MaterializerRecipe recipeWrapper,
                          @NotNull IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(input, true, 35, 20);
        stacks.init(catalyst, true, 55, 20);
        stacks.init(output, false, 105, 20);
        stacks.set(ingredients);
    }

    public static List<MaterializerRecipe> getRecipes() {
        List<MaterializerRecipe> jeiRecipes = Lists.newArrayList();
        for (Catalyst catalyst : MaterializerHandler.getCatalysts()) {
            for (var rec : catalyst.getRecipes().values()) {
                jeiRecipes.add(new MaterializerRecipe(
                        rec.getInput(),
                        catalyst.getCatalyst(),
                        rec.getOutput().apply(rec.getInput(), catalyst),
                        rec.getCraftDescription()));
            }
        }

        return jeiRecipes;
    }
}
