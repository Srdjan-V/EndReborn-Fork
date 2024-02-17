package io.github.srdjanv.endreforked.api.worldgen.base;

import static net.minecraft.util.EnumFacing.*;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import org.jetbrains.annotations.Nullable;

import io.github.srdjanv.endreforked.api.worldgen.DimConfig;

public enum Locators implements Locator {
    EMPTY {
        @Override public @Nullable BlockPos compute(WorldServer server, Random rand, DimConfig config, BlockPos pos, PositionValidator validator) {
            if (validator.validate(server, rand, pos)) return pos;
            return null;
        }
    }, SURFACE_BLOCK {
        @Override
        public @Nullable BlockPos compute(WorldServer server, Random rand, DimConfig config, BlockPos pos,
                                          PositionValidator validator) {
            pos = new BlockPos(pos.getX(), config.maxHeight(), pos.getZ());
            boolean valid = !server.isAirBlock(pos) && validator.validate(server, rand, pos);
            while (!valid) {
                pos = pos.down();
                if (config.minHeight() > pos.getY()) return null;
                valid = !server.isAirBlock(pos) && validator.validate(server, rand, pos);
                if (valid) valid = validateEdges(server, pos, server.getBlockState(pos).getBlock());
            }
            return pos;
        }

        private boolean validateEdges(WorldServer server, BlockPos pos, Block block) {
            for (EnumFacing facing : Arrays.asList(DOWN, NORTH, SOUTH, WEST, EAST)) {
                if (server.getBlockState(pos.offset(facing)).getBlock() == block) continue;
                return false;
            }
            return true;
        }
    },
    SURFACE_AIR {
        @Override
        public @Nullable BlockPos compute(WorldServer server, Random rand, DimConfig config, BlockPos pos,
                                          PositionValidator validator) {
            pos = new BlockPos(pos.getX(), config.maxHeight(), pos.getZ());
            boolean valid = server.isAirBlock(pos) && validator.validate(server, rand, pos);
            while (!valid) {
                pos = pos.down();
                if (config.minHeight() > pos.getY()) return null;
                valid = server.isAirBlock(pos) && validator.validate(server, rand, pos);
            }
            return pos;
        }
    },
    OFFSET_16 {
        @Override
        public BlockPos compute(WorldServer server, Random rand, DimConfig config, BlockPos pos,
                                PositionValidator validator) {
            return new BlockPos(pos.getX() + 16, pos.getY(), pos.getZ() + 16);
        }
    },
    OFFSET_8 {
        @Override
        public BlockPos compute(WorldServer server, Random rand, DimConfig config, BlockPos pos,
                                PositionValidator validator) {
            return new BlockPos(pos.getX() + 8, pos.getY(), pos.getZ() + 8);
        }
    },
    DIM_CONFIG_MIN_MAX {
        @Override
        public BlockPos compute(WorldServer server, Random rand, DimConfig config, BlockPos pos,
                                PositionValidator validator) {
            final int heightDiff = config.maxHeight() - config.minHeight();
            return new BlockPos(
                    pos.getX(),
                    config.minHeight() + rand.nextInt(Math.max(1, heightDiff)),
                    pos.getZ());
        }
    }

}
