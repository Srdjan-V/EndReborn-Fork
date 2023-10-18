package endreborn.common.world;

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

    private final WorldGenerator ore_end_essence, lormyte, ore_over_essence, ore_wolframium, end_magma, end_entropy;

    public OreGen() {
        ore_end_essence = new WorldGenMinable(ModBlocks.ESSENCE_ORE.getDefaultState(), 9,
                BlockMatcher.forBlock(Blocks.OBSIDIAN));
        ore_over_essence = new WorldGenMinable(ModBlocks.ESSENCE_ORE.getDefaultState(), 9,
                BlockMatcher.forBlock(Blocks.OBSIDIAN));
        ore_wolframium = new WorldGenMinable(ModBlocks.TUNGSTEN_ORE.getDefaultState(), 4,
                BlockMatcher.forBlock(Blocks.STONE));
        lormyte = new WorldGenLormyte();
        end_magma = new WorldGenMinable(ModBlocks.BLOCK_END_MAGMA.getDefaultState(), 30,
                BlockMatcher.forBlock(Blocks.END_STONE));
        end_entropy = new WorldGenMinable(ModBlocks.ENTROPY_END_STONE.getDefaultState(), 10,
                BlockMatcher.forBlock(Blocks.END_STONE));
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
                         IChunkProvider chunkProvider) {
        switch (world.provider.getDimension()) {
            case -1:

                break;

            case 0:
                if (Configs.GENERAL.spawnEssenceOre) {
                    runGenerator(ore_over_essence, world, random, chunkX, chunkZ,
                            Configs.BALANCE.essenceRareOver, 0, 256);
                }
                if (Configs.GENERAL.spawnWolframiumOre) {
                    runGenerator(ore_wolframium, world, random, chunkX, chunkZ, Configs.BALANCE.wolframiumRare,
                            0, 48);
                }

                break;
            case 7:
                if (Configs.GENERAL.spawnEssenceOre) {
                    runGenerator(ore_over_essence, world, random, chunkX, chunkZ,
                            Configs.BALANCE.essenceRareOver, 0, 256);
                }
                if (Configs.GENERAL.spawnWolframiumOre) {
                    runGenerator(ore_wolframium, world, random, chunkX, chunkZ, Configs.BALANCE.wolframiumRare,
                            0, 48);
                }
                break;
            case 6:
                if (Configs.GENERAL.spawnEssenceOre) {
                    runGenerator(ore_over_essence, world, random, chunkX, chunkZ,
                            Configs.BALANCE.essenceRareOver, 0, 256);
                }
                if (Configs.GENERAL.spawnWolframiumOre) {
                    runGenerator(ore_wolframium, world, random, chunkX, chunkZ, Configs.BALANCE.wolframiumRare,
                            0, 48);
                }
                break;

            case 1:
                if (Configs.GENERAL.spawnEssenceOre) {
                    runGenerator(ore_end_essence, world, random, chunkX, chunkZ, Configs.BALANCE.essenceRareEnd,
                            0, 256);
                }

                if (Configs.GENERAL.decoratorEnd) {
                    runGenerator(end_entropy, world, random, chunkX, chunkZ, 2, 0, 22);
                }
                if (Configs.GENERAL.spawnLormyte) {
                    runLormyteGenerator(lormyte, world, random, chunkX, chunkZ, 1, 35, 52);

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
