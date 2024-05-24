package io.github.srdjanv.endreforked.api.fluids.base;

import io.github.srdjanv.endreforked.api.base.crafting.recipe.base.BaseRecipe;
import io.github.srdjanv.endreforked.api.fluids.IWorldRecipe;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static io.github.srdjanv.endreforked.api.fluids.base.CollisionRecipe.EMPTY_FLUID_INTERACTION_CALLBACK;
import static io.github.srdjanv.endreforked.api.fluids.base.CollisionRecipe.EMPTY_INTERACTION_CALLBACK;

public class EntityFluidRecipe<T extends Entity> extends BaseRecipe<Class<? extends Entity>, BiFunction<WorldServer, T, ? extends Entity>> implements IWorldRecipe {

    protected final int chance;
    protected final boolean consumeSource;
    protected final BiConsumer<WorldServer, BlockPos> interactionCallback;
    protected final BiConsumer<WorldServer, BlockPos> fluidInteractionCallback;

    public EntityFluidRecipe(Class<T> input, int chance, boolean consumeSource, BiFunction<WorldServer, T, ? extends Entity> recipeFunction) {
        this(input, chance, consumeSource, recipeFunction, EMPTY_FLUID_INTERACTION_CALLBACK, EMPTY_INTERACTION_CALLBACK);
    }

    public EntityFluidRecipe(Class<T> input, int chance, boolean consumeSource, BiFunction<WorldServer, T, ? extends Entity> recipeFunction,
                             BiConsumer<WorldServer, BlockPos> interactionCallback,
                             BiConsumer<WorldServer, BlockPos> fluidInteractionCallback) {
        super(input, recipeFunction);
        this.chance = chance;
        this.consumeSource = consumeSource;
        this.interactionCallback = interactionCallback;
        this.fluidInteractionCallback = fluidInteractionCallback;
    }


    @Override
    public int getChance() {
        return chance;
    }

    @Override
    public boolean isConsumeSource() {
        return consumeSource;
    }

    @Override
    public BiConsumer<WorldServer, BlockPos> getFluidInteractionCallback() {
        return fluidInteractionCallback;
    }

    @Override
    public BiConsumer<WorldServer, BlockPos> getInteractionCallback() {
        return interactionCallback;
    }
}
