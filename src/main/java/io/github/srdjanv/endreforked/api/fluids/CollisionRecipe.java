package io.github.srdjanv.endreforked.api.fluids;

import io.github.srdjanv.endreforked.api.base.crafting.Recipe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class CollisionRecipe<IN, OUT> extends Recipe<IN, OUT> {
    protected final int chance;
    protected final boolean consumeSource;
    protected final BiConsumer<WorldServer, BlockPos> interactionCallback;
    protected final BiConsumer<WorldServer, BlockPos> fluidInteractionCallback;

    public CollisionRecipe(IN input, int chance, boolean consumeSource, Function<IN, OUT> recipeFunction,
                           BiConsumer<WorldServer, BlockPos> interactionCallback,
                           BiConsumer<WorldServer, BlockPos> fluidInteractionCallback) {
        super(input, recipeFunction);
        this.chance = chance;
        this.consumeSource = consumeSource;
        this.interactionCallback = interactionCallback;
        this.fluidInteractionCallback = fluidInteractionCallback;
    }

    public int getChance() {
        return chance;
    }

    public boolean isConsumeSource() {
        return consumeSource;
    }

    public BiConsumer<WorldServer, BlockPos> getFluidInteractionCallback() {
        return fluidInteractionCallback;
    }

    public BiConsumer<WorldServer, BlockPos> getInteractionCallback() {
        return interactionCallback;
    }
}