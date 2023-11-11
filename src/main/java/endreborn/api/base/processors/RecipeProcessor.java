package endreborn.api.base.processors;

import java.util.Objects;

import endreborn.api.base.HandlerRegistry;
import endreborn.api.base.Recipe;
import endreborn.api.base.groupings.RecipeGrouping;

public abstract class RecipeProcessor<IN1, IN2, OUT,
        RG extends RecipeGrouping<IN1, IN2, R>,
        R extends Recipe<IN1, IN2, OUT>> {

    protected final HandlerRegistry<IN1, IN2, OUT, RG, R> handlerRegistry;
    protected RG recipeGrouping;
    protected R recipe;

    public RecipeProcessor(HandlerRegistry<IN1, IN2, OUT, RG, R> handlerRegistry) {
        this.handlerRegistry = handlerRegistry;
    }

    public boolean validateGrouping(IN1 input) {
        if (Objects.isNull(recipeGrouping)) {
            recipeGrouping = handlerRegistry.findRecipeGrouping(input);
            return Objects.nonNull(recipeGrouping);
        }
        if (handlerRegistry.getHashStrategy().equals(input, recipeGrouping.getGrouping())) return true;
        recipeGrouping = handlerRegistry.findRecipeGrouping(input);

        return Objects.nonNull(recipeGrouping);
    }

    public boolean validateRecipe(IN2 input) {
        if (Objects.isNull(recipeGrouping)) return false;
        if (Objects.isNull(recipe)) {
            recipe = handlerRegistry.findRecipe(recipeGrouping, input);
            return Objects.nonNull(recipe);
        }
        if (recipeGrouping.getHashStrategy().equals(input, recipe.getInput())) return true;
        recipe = handlerRegistry.findRecipe(recipeGrouping, input);
        return Objects.nonNull(recipe);
    }

    public HandlerRegistry<IN1, IN2, OUT, RG, R> getHandlerRegistry() {
        return handlerRegistry;
    }

    public RG getRecipeGrouping() {
        return recipeGrouping;
    }

    public R getRecipe() {
        return recipe;
    }
}
