package io.github.srdjanv.endreforked.api.worldgen.base;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@FunctionalInterface
public interface WorldGeneratorBuilder {
    @Nullable WorldGenerator build(WorldServer server, Random rand, BlockPos position);
}
