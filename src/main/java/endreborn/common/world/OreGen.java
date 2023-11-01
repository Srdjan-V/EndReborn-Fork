package endreborn.common.world;

import java.util.Map;
import java.util.Random;

import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import endreborn.common.Configs;
import endreborn.common.ModBlocks;

public class OreGen implements IWorldGenerator {

    private final WorldGenerator oreEndEssence, lormyte, tungstenOre, endMagma, endEntropy;

    public OreGen() {
        oreEndEssence = new WorldGenMinable(ModBlocks.ESSENCE_ORE.get().getDefaultState(), 9,
                BlockMatcher.forBlock(Blocks.OBSIDIAN));
        tungstenOre = new WorldGenMinable(ModBlocks.TUNGSTEN_ORE.get().getDefaultState(), 4,
                BlockMatcher.forBlock(Blocks.STONE));
        lormyte = new WorldGenLormyte();
        endMagma = new WorldGenMinable(ModBlocks.BLOCK_END_MAGMA.get().getDefaultState(), 30,
                BlockMatcher.forBlock(Blocks.END_STONE));
        endEntropy = new WorldGenMinable(ModBlocks.ENTROPY_END_STONE.get().getDefaultState(), 10,
                BlockMatcher.forBlock(Blocks.END_STONE));
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
                         IChunkProvider chunkProvider) {
        var generators = Configs.WORLD_ORE_GEN_CONFIG.getOreGensForDim(world.provider.getDimension());
        for (Configs.WorldOreGenConfig.OreGen generator : generators) {
            final WorldGenerator gen;

            if (generator instanceof Configs.WorldOreGenConfig.EssenceOre) {
                gen = oreEndEssence;
            } else if (generator instanceof Configs.WorldOreGenConfig.TungstenOre) {
                gen = tungstenOre;
            } else if (generator instanceof Configs.WorldOreGenConfig.Lormyte) {
                gen = lormyte;
            } else if (generator instanceof Configs.WorldOreGenConfig.EndMagma) {
                gen = endMagma;
            } else if (generator instanceof Configs.WorldOreGenConfig.EntropyEndStone) {
                gen = endEntropy;
            } else throw new IllegalStateException("Non existent generator");

            for (Map.Entry<String, int[]> dimConfig : generator.oreSpawnConfig.entrySet()) {
                if (world.provider.getDimension() != Integer.parseInt(dimConfig.getKey())) continue;
                int[] config = dimConfig.getValue();

                runGenerator(gen, world, random, chunkX, chunkZ,
                        config[0], config[1], config[2]);
            }
        }
    }

    private void runGenerator(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance,
                              int minHeight, int maxHeight) {
        if (minHeight > maxHeight || minHeight < 0 || maxHeight > 256)
            throw new IllegalArgumentException("Ore generated out of bounds");

        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chance; i++) {
            int x = chunkX * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunkZ * 16 + rand.nextInt(16);

            gen.generate(world, rand, new BlockPos(x, y, z));
        }
    }

    // TODO: 31/10/2023 remove
    private void runLormyteGenerator(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance,
                                     int minHeight, int maxHeight) {
        if (minHeight > maxHeight || minHeight < 0 || maxHeight > 256)
            throw new IllegalArgumentException("Ore generated out of bounds");

        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chance; i++) {
            int x = chunkX * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunkZ * 16 + rand.nextInt(16);

            gen.generate(world, rand, new BlockPos(x, y - 6, z));
        }
    }

    private void runCorGenerator(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance,
                                 int minHeight, int maxHeight) {
        if (minHeight > maxHeight || minHeight < 0 || maxHeight > 256)
            throw new IllegalArgumentException("Ore generated out of bounds");

        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chance; i++) {
            int x = chunkX * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunkZ * 16 + rand.nextInt(16);
            if (world.isAirBlock(new BlockPos(x, y, z))) {
                if (world.getBlockState(new BlockPos(x, y, z).down()) == Blocks.END_STONE ||
                        world.getBlockState(new BlockPos(x, y, z).down()) == Blocks.END_BRICKS) {
                    gen.generate(world, rand, new BlockPos(x, y, z));
                }
            }
        }
    }
}
