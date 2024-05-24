package io.github.srdjanv.endreforked.api.fluids.base;

import io.github.srdjanv.endreforked.api.base.crafting.HandlerRegistry;
import io.github.srdjanv.endreforked.api.fluids.HashStrategies;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class FluidEntityCollisionHandler extends HandlerRegistry<Class<? extends Entity>, EntityFluidRecipe<?>> {
    @Override public Hash.Strategy<Class<? extends Entity>> getHashStrategy() {
        return HashStrategies.ENTITY_FLUID_HASH_STRATEGY;
    }

    @SuppressWarnings("unchecked") @Nullable
    public <T extends Entity> EntityFluidRecipe<T> findRecipe(T type) {
        return (EntityFluidRecipe<T>) registry.get(type.getClass());
    }
}
