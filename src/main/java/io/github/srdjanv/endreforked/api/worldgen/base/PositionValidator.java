package io.github.srdjanv.endreforked.api.worldgen.base;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

@FunctionalInterface
public interface PositionValidator {

    boolean validate(WorldServer server, Random rand, BlockPos pos);
}
