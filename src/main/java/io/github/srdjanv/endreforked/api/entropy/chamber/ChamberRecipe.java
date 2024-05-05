package io.github.srdjanv.endreforked.api.entropy.chamber;

import io.github.srdjanv.endreforked.api.base.Recipe;

import java.util.function.Function;

public class ChamberRecipe<IN, OUT> extends Recipe<IN, OUT> {
    private final int entropyCost;

    public ChamberRecipe(IN input, int ticksToComplete, int entropyCost, Function<IN, OUT> recipeFunction) {
        super(input, ticksToComplete, recipeFunction);
        this.entropyCost = entropyCost;
    }

    public int getEntropyCost() {
        return entropyCost;
    }
}
