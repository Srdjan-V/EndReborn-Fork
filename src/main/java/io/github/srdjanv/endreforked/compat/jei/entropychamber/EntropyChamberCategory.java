package io.github.srdjanv.endreforked.compat.jei.entropychamber;

import io.github.srdjanv.endreforked.Tags;
import io.github.srdjanv.endreforked.api.entropy.EntropyChamberHandler;
import io.github.srdjanv.endreforked.utils.LangUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;

import java.util.List;

public class EntropyChamberCategory implements IRecipeCategory<EntropyChamberRecipe> {
    public static final String UID = Tags.MODID + ".entropy_chamber";

    private final IDrawable background;
    private final IDrawable slot;

    public EntropyChamberCategory(IGuiHelper helper) {
        background = helper.createBlankDrawable(160, 60);
        slot = helper.getSlotDrawable();
    }


    @Override public String getUid() {
        return UID;
    }

    @Override public String getTitle() {
        return LangUtil.translateToLocal("item.entropy_wand.name");
    }

    @Override public String getModName() {
        return Tags.MODNAME;
    }

    @Override public IDrawable getBackground() {
        return background;
    }

    @Override public void setRecipe(IRecipeLayout recipeLayout, EntropyChamberRecipe recipeWrapper, IIngredients ingredients) {
        IGuiIngredientGroup<?> group;
        switch (recipeWrapper.getType()) {
            case FLUID -> group = recipeLayout.getFluidStacks();
            case ITEM -> group = recipeLayout.getItemStacks();
            default -> throw new IllegalStateException("Unexpected value: " + recipeWrapper.getType());
        }

        group.init(0, true, 48, 20);
        group.init(1, false, 96, 20);
        group.set(ingredients);
    }

    public static List<EntropyChamberRecipe> getRecipes() {
        List<EntropyChamberRecipe> jeiRecipes = new ObjectArrayList<>();
        for (var set : EntropyChamberHandler.ITEM.getRegistry().entrySet()) {
            jeiRecipes.add(new EntropyChamberRecipe(set.getKey(), set.getValue()));
        }

        for (var set : EntropyChamberHandler.FLUID.getRegistry().entrySet()) {
            jeiRecipes.add(new EntropyChamberRecipe(set.getKey(), set.getValue()));
        }

        return jeiRecipes;
    }
}
