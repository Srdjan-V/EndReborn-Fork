package io.github.srdjanv.endreforked.api.base.crafting;

import java.util.function.Function;

public class Recipe<IN, OUT> {
    protected final IN input;
    protected final Function<IN, OUT> recipeFunction;
    protected final int ticksToComplete;

    public Recipe(IN input, int ticksToComplete, Function<IN, OUT> recipeFunction) {
        this.input = input;
        this.ticksToComplete = ticksToComplete;
        this.recipeFunction = recipeFunction;
    }

    public IN getInput() {
        return input;
    }

    public int getTicksToComplete() {
        return ticksToComplete;
    }

    public Function<IN, OUT> getRecipeFunction() {
        return recipeFunction;
    }
}
