package io.github.srdjanv.endreforked.api.worldgen.features;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import io.github.srdjanv.endreforked.api.worldgen.DimConfig;

public class SurfaceGenerator extends WorldGenerator {

    protected final DimConfig config;
    protected final BlockBush block;

    public SurfaceGenerator(DimConfig config, BlockBush block) {
        this.config = config;
        this.block = block;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int count = 0;
        position = new BlockPos(position.getX() + 16, config.maxHeight(), position.getZ() + 16);
        boolean valid = worldIn.isAirBlock(position) && block.canBlockStay(worldIn, position, block.getDefaultState());
        while (!valid) {
            position = position.down();
            valid = worldIn.isAirBlock(position) && block.canBlockStay(worldIn, position, block.getDefaultState());
            if (config.minHeight() > position.getY()) return false;
        }

        for (int i = 0; i < config.count() * 2; ++i) {
            if (count > config.count()) break;
            BlockPos blockpos = position.add(
                    rand.nextInt(8) - rand.nextInt(8),
                    rand.nextInt(4) - rand.nextInt(4),
                    rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(blockpos) && blockpos.getY() < worldIn.getHeight() - 1) {
                var newState = getState(worldIn, blockpos, rand);
                if (block.canBlockStay(worldIn, blockpos, newState)) {
                    worldIn.setBlockState(blockpos, newState, 2);
                    count++;
                }
            }
        }

        return count > config.count();
    }

    public IBlockState getState(World world, BlockPos pos, Random random) {
        return block.getDefaultState();
    }
}
