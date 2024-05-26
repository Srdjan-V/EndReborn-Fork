package io.github.srdjanv.endreforked.api.fluids;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.function.BiConsumer;

public interface IWorldRecipe {
    BiConsumer<WorldServer, BlockPos> EMPTY_INTERACTION_CALLBACK = (world, pos) -> {};
    BiConsumer<WorldServer, BlockPos> EMPTY_FLUID_INTERACTION_CALLBACK = (world, pos) -> {};

    int getChance();
    int getChanceConsumeSource();
    boolean isConsumeSource();
    BiConsumer<WorldServer, BlockPos> getFluidInteractionCallback();
    BiConsumer<WorldServer, BlockPos> getInteractionCallback();
}
