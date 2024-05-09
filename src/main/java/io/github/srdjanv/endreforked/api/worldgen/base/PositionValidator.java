package io.github.srdjanv.endreforked.api.worldgen.base;

import java.util.Objects;
import java.util.Random;

import io.github.srdjanv.endreforked.api.worldgen.GenConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

@FunctionalInterface
public interface PositionValidator {

    boolean validate(WorldServer server, GenConfig config, Random rand, BlockPos pos);

    default PositionValidator andValidate(final PositionValidator and) {
        Objects.requireNonNull(and);
        return (server, rand, config, pos) -> {
            var computed = PositionValidator.this.validate(server, rand, config, pos);
            return computed && and.validate(server, rand, config, pos);
        };
    }

    default PositionValidator orValidate(final PositionValidator or) {
        Objects.requireNonNull(or);
        return (server, rand, config, pos) -> {
            var computed = PositionValidator.this.validate(server, rand, config, pos);
            return computed || or.validate(server, rand, config, pos);
        };
    }
}
