package io.github.srdjanv.endreforked.api.fluids;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.function.BiConsumer;

public interface IWorldRecipe {
    int getChance();
    boolean isConsumeSource();
    BiConsumer<WorldServer, BlockPos> getFluidInteractionCallback();
    BiConsumer<WorldServer, BlockPos> getInteractionCallback();
}
