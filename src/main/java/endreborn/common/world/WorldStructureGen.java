package endreborn.common.world;

import java.util.Random;
import java.util.function.ToIntFunction;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import endreborn.common.Configs;
import endreborn.common.world.gen.WorldGenStructure;

public class WorldStructureGen implements IWorldGenerator {

    private final WorldGenStructure end_ruins, end_island, observ;

    public WorldStructureGen() {
        end_ruins = new WorldGenStructure("end_ruins");
        end_island = new WorldGenStructure("end_island");
        observ = new WorldGenStructure("observ");
    }

    @Override
    public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator generator,
                         IChunkProvider provider) {
        final var chunkBiome = world.provider.getBiomeForCoords(
                new BlockPos(chunkX, world.provider.getAverageGroundLevel(), chunkX));
        final var generators = Configs.WORLD_GEN_STRUCTURE_CONFIG
                .getStructGensForBiome(chunkBiome);
        for (var confGenerator : generators) {
            final WorldGenerator gen;
            final ToIntFunction<Random> yCordFunction;

            if (confGenerator.getKey() instanceof Configs.WorldGenStructureConfig.EndRuins) {
                gen = end_ruins;
                yCordFunction = yRand -> 50 + rand.nextInt(15);
            } else if (confGenerator.getKey() instanceof Configs.WorldGenStructureConfig.EndIslands) {
                gen = end_island;
                yCordFunction = yRand -> 90 + rand.nextInt(15);
            } else if (confGenerator.getKey() instanceof Configs.WorldGenStructureConfig.Observatory) {
                gen = observ;
                yCordFunction = yRand -> 3;
            } else throw new IllegalStateException("Non existent generator");

            for (var config : confGenerator.getValue().entrySet()) {
                if (!config.getKey().equals(chunkBiome)) continue;

                generateStruct(gen, world, rand, chunkX, yCordFunction, chunkZ, config.getValue());
            }
        }
    }

    private void generateStruct(WorldGenerator generator, World world, Random rand,
                                int chunkX, ToIntFunction<Random> yCordFunction, int chunkZ,
                                int chance) {
        int x = (chunkX * 16) + rand.nextInt(15);
        int z = (chunkZ * 16) + rand.nextInt(15);
        int y = yCordFunction.applyAsInt(rand);
        if (rand.nextInt(chance) == 0)
            generator.generate(world, rand, new BlockPos(x, y, z));
    }
}
