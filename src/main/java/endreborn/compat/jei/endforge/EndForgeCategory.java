package endreborn.compat.jei.endforge;

import java.util.List;

import net.minecraft.client.Minecraft;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.drawable.GuiTextures;
import com.google.common.collect.Lists;

import endreborn.Reference;
import endreborn.api.endforge.EndForgeHandler;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;

public class EndForgeCategory implements IRecipeCategory<EndForgeRecipe> {

    public static final String UID = Reference.MODID + ".endforge";
    protected static final int inputFluid = 0;
    protected static final int input = 1;
    protected static final int output = 2;

    private final IDrawable background;
    private final IDrawable slot;

    public EndForgeCategory(IGuiHelper helper) {
        background = helper.createBlankDrawable(160, 60);
        slot = helper.getSlotDrawable();
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        GuiTextures.PROGRESS_ARROW.getSubArea(0f, 0f, 1f, 0.5f).draw(78, 20, 20, 20);
        float progress = Minecraft.getSystemTime() % 2000 / 2000f; // 2 seconds
        GuiTextures.PROGRESS_ARROW.getSubArea(0f, 0.5f, progress, 1f).draw(78, 20, progress * 20, 20);
        slot.draw(minecraft, 105, 20);
        slot.draw(minecraft, 55, 20);
        slot.draw(minecraft, 35, 20);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return "endforge"; // TODO: 11/11/2023
    }

    @Override
    public @NotNull String getModName() {
        return Reference.NAME;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @NotNull EndForgeRecipe recipeWrapper,
                          @NotNull IIngredients ingredients) {
        var fluidStacks = recipeLayout.getFluidStacks();
        fluidStacks.init(inputFluid, true, 35, 20, 18, 18, 16000, true, slot);
        fluidStacks.set(ingredients);

        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(input, true, 55, 20);
        stacks.init(output, false, 105, 20);
        stacks.set(ingredients);
    }

    public static List<EndForgeRecipe> getRecipes() {
        List<EndForgeRecipe> jeiRecipes = Lists.newArrayList();
        for (var recipeGroping : EndForgeHandler.getInstance().getRecipeGroupings()) {
            for (var rec : recipeGroping.getRecipes().values()) {
                jeiRecipes.add(new EndForgeRecipe(
                        recipeGroping.getGrouping(),
                        rec.getInput(),
                        rec.getRecipeFunction().apply(recipeGroping.getGrouping(), rec.getInput())));
            }
        }

        return jeiRecipes;
    }
}
