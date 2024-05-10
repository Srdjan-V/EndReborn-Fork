package io.github.srdjanv.endreforked.api.worldgen.base;

import io.github.srdjanv.endreforked.api.worldgen.Generator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

//Unfinished
public class SimpleMapGenStructure extends MapGenStructure {

    private final Generator generator;
    private final String structureName;

    public SimpleMapGenStructure(String structureName, Generator generator) {
        this.generator = generator;
        this.structureName = structureName;
    }

    @Override public String getStructureName() {
        return structureName;
    }

    @Override public void generate(World worldIn, int x, int z, ChunkPrimer primer) {
    }

    @Override public boolean generateStructure(World worldIn, Random randomIn, ChunkPos chunkCoord) {
/*        final var biome = worldIn.getBiome(chunkCoord.getBlock(8, 0, 8));
        if (Utils.isValidGen(worldIn.provider.getDimension(), biome, generator)) {
            var gen = generator.getGenerator(world, biome, worldIn.provider.getDimension());
            if (gen == null) return false;
            return gen.generate(worldIn, rand, chunkCoord.getBlock(0, 0, 0));
        }*/
        return false;
    }

    @Nullable @Override public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored) {
        this.world = worldIn;
        //broken
/*        var config = generatorConfig.getDimConfig(worldIn.provider.getDimension(), worldIn.getBiome(pos));
        if (config == null) return null;
        return findNearestStructurePosBySpacing(worldIn,
                this, pos, config.rarity(),
                8, 10387312, false, 100, findUnexplored);*/
        return null;
    }

    @Override protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
/*        final var biome = world.getBiome(new BlockPos(chunkX << 4, 0, chunkZ << 4).add(8, 0, 8));
        if (!Utils.isValidGen(world.provider.getDimension(), biome, generator)) return false;

        var generator = this.generator.getGenerator(world, biome, world.provider.getDimension());
        if (generator == null) return false;
        if (generator instanceof PositionedFeature positionedFeature) {
            return positionedFeature
                    .getStartPos((WorldServer) world, rand,
                            new BlockPos(chunkX << 4, 0, chunkZ << 4),
                            positionedFeature.getStartPosValidator()) != null;
        }*/
        return false;
    }

    @Override protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new StructureStart() {};
    }
}
