package io.github.srdjanv.endreforked.api.fluids.base;

import io.github.srdjanv.endreforked.api.base.crafting.HandlerRegistry;
import io.github.srdjanv.endreforked.api.fluids.CollisionRecipe;
import io.github.srdjanv.endreforked.api.fluids.HashStrategies;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class FluidAnyStateCollisionHandler extends HandlerRegistry<Block, CollisionRecipe<Block, IBlockState>> {
    @Override public Hash.Strategy<Block> getHashStrategy() {
        return HashStrategies.FLUID_ANY_STATE_HASH_STRATEGY;
    }
}
