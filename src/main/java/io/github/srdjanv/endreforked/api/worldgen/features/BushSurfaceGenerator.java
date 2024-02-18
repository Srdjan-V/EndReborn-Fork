package io.github.srdjanv.endreforked.api.worldgen.features;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import io.github.srdjanv.endreforked.api.worldgen.DimConfig;
import io.github.srdjanv.endreforked.api.worldgen.base.*;

public class BushSurfaceGenerator extends PositionedFeature {

    protected final BlockBush block;

    public BushSurfaceGenerator(DimConfig config, BlockBush block) {
        super(Locators.SURFACE_AIR, config);
        this.block = block;
    }

    @Override
    protected boolean doGenerate(WorldServer server, Random rand, BlockPos startPos) {
        int count = 0;
        for (int i = 0; i < config.amountModifier() * 2; ++i) {
            if (count > config.amountModifier()) break;
            BlockPos blockpos = startPos.add(
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

        return count > config.amountModifier();
    }

    public IBlockState getState(World world, Random random, BlockPos pos) {
        return block.getDefaultState();
    }

    @Override
    protected PositionValidator getStartPosValidator() {
        return PositionValidators.BLOCK_DOWN_ANY;
    }
}
