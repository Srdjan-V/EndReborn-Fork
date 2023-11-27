package endreborn.common.world.features;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import endreborn.api.worldgen.DimConfig;
import endreborn.common.ModBlocks;

public class WorldGenLormyte extends WorldGenerator {

    private final DimConfig config;

    public WorldGenLormyte(DimConfig config) {
        this.config = config;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        position = position.add(16, 0, 16);
        int generated = 0;
        for (int i = 0; i < config.count() * 2; ++i) {
            if (generated >= config.count()) return true;
            BlockPos blockpos = position.add(
                    rand.nextInt(8) - rand.nextInt(8),
                    rand.nextInt(4) - rand.nextInt(4),
                    rand.nextInt(8) - rand.nextInt(8));

            var block = worldIn.getBlockState(blockpos);
            if (!block.getBlock().isAir(block, worldIn, blockpos) &&
                    block.getBlock().isReplaceable(worldIn, blockpos)) {
                worldIn.setBlockState(blockpos, ModBlocks.LORMYTE_CRYSTAL_BLOCK.get().getDefaultState(), 2);
                generated++;
            }
        }

        return false;
    }
}
