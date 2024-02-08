package io.github.srdjanv.endreforked.api.worldgen.features;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import io.github.srdjanv.endreforked.api.worldgen.DimConfig;
import io.github.srdjanv.endreforked.api.worldgen.base.*;

public class SurfaceGenerator extends PositionedFeature {

    protected final BlockBush block;
    protected final PositionValidator positionValidator;

    public SurfaceGenerator(DimConfig config, BlockBush block) {
        super(Locators.OFFSET_16.andThenLocate(Locators.SURFACE_AIR), config);
        this.block = block;
        positionValidator = PositionValidators.blockBushValidator(block);
    }

    @Override
    protected boolean doGenerate(WorldServer server, Random rand, BlockPos position) {
        int count = 0;
        for (int i = 0; i < config.count() * 2; ++i) {
            if (count > config.count()) break;
            BlockPos blockpos = position.add(
                    rand.nextInt(8) - rand.nextInt(8),
                    rand.nextInt(4) - rand.nextInt(4),
                    rand.nextInt(8) - rand.nextInt(8));

            if (server.isAirBlock(blockpos) && blockpos.getY() < server.getHeight() - 1) {
                var newState = getState(server, rand, blockpos);
                if (block.canBlockStay(server, blockpos, newState)) {
                    server.setBlockState(blockpos, newState, 2);
                    count++;
                }
            }
        }

        return count > config.count();
    }

    public IBlockState getState(World world, Random random, BlockPos pos) {
        return block.getDefaultState();
    }

    @Override
    protected PositionValidator getValidator() {
        return positionValidator;
    }
}
