package io.github.srdjanv.endreforked.api.worldgen.base;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public enum PositionValidators implements PositionValidator {

    ALWAYS_TRUE {

        @Override
        public boolean validate(WorldServer server, Random rand, BlockPos pos) {
            return true;
        }
    },
    BLOCK_ANY {

        @Override
        public boolean validate(WorldServer server, Random rand, BlockPos pos) {
            return !server.isAirBlock(pos);
        }
    },
    BLOCK_DOWN_ANY {

        @Override
        public boolean validate(WorldServer server, Random rand, BlockPos pos) {
            return !server.isAirBlock(pos.down());
        }
    };

    public static PositionValidator blockBushValidator(BlockBush blockBush) {
        return blockBushValidator(blockBush, blockBush.getDefaultState());
    }

    public static PositionValidator blockBushValidator(final BlockBush blockBush, final IBlockState state) {
        return (server, rand, pos) -> server.isAirBlock(pos) && blockBush.canBlockStay(server, pos, state);
    }
}
