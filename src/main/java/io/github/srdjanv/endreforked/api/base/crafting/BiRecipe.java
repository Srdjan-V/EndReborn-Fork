package io.github.srdjanv.endreforked.api.base.crafting;

import java.util.function.BiFunction;

public abstract class BiRecipe<IN1, IN2, OUT> {

    protected final IN2 input;
    protected final BiFunction<IN1, IN2, OUT> recipeFunction;
    protected final int ticksToComplete;

    public BiRecipe(IN2 input, int ticksToComplete, BiFunction<IN1, IN2, OUT> recipeFunction) {
        this.input = input;
        this.ticksToComplete = ticksToComplete;
        this.recipeFunction = recipeFunction;
    }

    public IN2 getInput() {
        return input;
    }

    public int getTicksToComplete() {
        return ticksToComplete;
    }

    public BiFunction<IN1, IN2, OUT> getRecipeFunction() {
        return recipeFunction;
    }
}
