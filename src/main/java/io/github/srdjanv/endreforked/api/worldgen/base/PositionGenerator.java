package io.github.srdjanv.endreforked.api.worldgen.base;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.Random;

@FunctionalInterface
public interface PositionGenerator {
    boolean generate(WorldServer server, Random rand, BlockPos pos);
}
