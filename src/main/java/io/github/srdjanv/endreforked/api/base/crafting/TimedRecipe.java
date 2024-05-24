package io.github.srdjanv.endreforked.api.base.crafting;

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
