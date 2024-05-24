package io.github.srdjanv.endreforked.api.base.crafting;

import java.util.function.Function;

public abstract class Recipe<IN, OUT> {
    protected final IN input;
    protected final Function<IN, OUT> recipeFunction;

    public Recipe(IN input, Function<IN, OUT> recipeFunction) {
        this.input = input;
        this.recipeFunction = recipeFunction;
    }

    public IN getInput() {
        return input;
    }


    public Function<IN, OUT> getRecipeFunction() {
        return recipeFunction;
    }
}
