package endreborn.compat.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import endreborn.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import org.jetbrains.annotations.NotNull;

public class EUserCategory extends EUserAbstract<EUserRecipe> {
    public static final String USER = Reference.MODID + ".user";
    private final IDrawable background;
    private final String name;

    public EUserCategory(IGuiHelper helper) {
        super(helper);
        background = helper.createDrawable(TEXTURES, 4, 4, 169, 78);
        name = I18n.format("tile.entropy_user.name");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(@NotNull Minecraft minecraft) {
        animatedFlame.draw(minecraft, 64, 13);
        animatedArrow.draw(minecraft, 106, 39);
    }

    @Override
    public @NotNull String getTitle() {
        return name;
    }

    @Override
    public @NotNull String getModName() {
        return Reference.NAME;
    }

    @Override
    public @NotNull String getUid() {
        return USER;
    }

    public void setRecipe(IRecipeLayout recipeLayout, @NotNull EUserRecipe recipeWrapper, @NotNull IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(input1, true, 64, 38);
        stacks.init(input2, true, 86, 38);
        stacks.init(output, false, 136, 38);
        stacks.set(ingredients);
    }
}
