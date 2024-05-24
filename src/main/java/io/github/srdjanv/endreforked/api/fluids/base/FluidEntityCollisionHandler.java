package io.github.srdjanv.endreforked.api.fluids.base;

import io.github.srdjanv.endreforked.api.base.crafting.HandlerRegistry;
import io.github.srdjanv.endreforked.api.fluids.CollisionRecipe;
import io.github.srdjanv.endreforked.api.fluids.HashStrategies;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.entity.Entity;

public class FluidEntityCollisionHandler extends HandlerRegistry<Entity, CollisionRecipe<Entity, Entity>> {
    @Override public Hash.Strategy<Entity> getHashStrategy() {
        return HashStrategies.ENTITY_FLUID_HASH_STRATEGY;
    }
}
