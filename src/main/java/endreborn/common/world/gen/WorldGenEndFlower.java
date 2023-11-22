package endreborn.common.world.gen;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import endreborn.common.blocks.BlockEnderCrop;

public class WorldGenEndFlower extends WorldGenerator {

    private final BlockEnderCrop block;

    public WorldGenEndFlower(BlockEnderCrop blockIn) {
        this.block = blockIn;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        for (int i = 0; i < 32; ++i) {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4),
                    rand.nextInt(8) - rand.nextInt(8));

            var flowerState = block.getDefaultState().withProperty(BlockEnderCrop.AGE, rand.nextInt(5));

            if (worldIn.isAirBlock(blockpos) &&
                    (!worldIn.provider.isNether() || blockpos.getY() < worldIn.getHeight() - 1) &&
                    block.canBlockStay(worldIn, blockpos, flowerState)) {
                worldIn.setBlockState(blockpos, flowerState, 2);
            }
        }

        return true;
    }
}
