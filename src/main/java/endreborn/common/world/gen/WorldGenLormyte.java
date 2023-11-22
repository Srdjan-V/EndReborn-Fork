package endreborn.common.world.gen;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import endreborn.common.ModBlocks;

public class WorldGenLormyte extends WorldGenerator {

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if (worldIn.isAirBlock(position)) return false;
        worldIn.setBlockState(position, ModBlocks.LORMYTE_CRYSTAL_BLOCK.get().getDefaultState(), 2);

        for (int i = 0; i < 512; ++i) {
            BlockPos blockpos = position.add(rand.nextInt(4) - rand.nextInt(6), rand.nextInt(6) - rand.nextInt(6),
                    rand.nextInt(8) - rand.nextInt(10));
            var block = worldIn.getBlockState(blockpos);
            if (!block.getBlock().isAir(worldIn.getBlockState(blockpos), worldIn, blockpos) &&
                    block.getBlock().isReplaceable(worldIn, blockpos))
                worldIn.setBlockState(blockpos, ModBlocks.LORMYTE_CRYSTAL_BLOCK.get().getDefaultState(), 2);
        }

        return true;
    }
}
