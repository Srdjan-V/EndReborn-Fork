package io.github.srdjanv.endreforked.api.fluids.base;

import io.github.srdjanv.endreforked.api.base.crafting.HandlerRegistry;
import io.github.srdjanv.endreforked.api.fluids.HashStrategies;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.block.state.IBlockState;

public class FluidCollisionHandler extends HandlerRegistry<IBlockState, CollisionRecipe<IBlockState, IBlockState>> {
    @Override public Hash.Strategy<IBlockState> getHashStrategy() {
        return HashStrategies.FLUID_SATE_HASH_STRATEGY;
    }
}
