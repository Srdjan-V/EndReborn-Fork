package io.github.srdjanv.endreforked.api.base.crafting.recipe.timed;

import io.github.srdjanv.endreforked.api.base.crafting.recipe.base.BaseRecipe;
import io.github.srdjanv.endreforked.api.base.crafting.recipe.base.Recipe;

import java.util.function.Function;

public class TimedRecipe<IN, OUT> extends Recipe<IN, OUT> {
    protected final int ticksToComplete;

    public TimedRecipe(IN input, int ticksToComplete, Function<IN, OUT> recipeFunction) {
        super(input, recipeFunction);
        this.ticksToComplete = ticksToComplete;
    }

    public int getTicksToComplete() {
        return ticksToComplete;
    }
}
