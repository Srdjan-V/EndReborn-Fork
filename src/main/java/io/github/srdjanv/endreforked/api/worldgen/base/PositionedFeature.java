package io.github.srdjanv.endreforked.api.worldgen.base;

import java.util.Objects;
import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;

import org.jetbrains.annotations.Nullable;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.api.worldgen.DimConfig;

public abstract class PositionedFeature extends WorldGenerator {

    private final Locator locator;
    protected final DimConfig config;

    protected PositionedFeature(Locator locator, DimConfig config) {
        this.locator = locator;
        this.config = config;
    }

    @Nullable
    protected BlockPos getStartPos(WorldServer server, Random rand, BlockPos pos, PositionValidator validator) {
        return locator.compute(server, rand, config, pos, validator);
    }

    @Override
    public final boolean generate(World worldIn, Random rand, BlockPos position) {
        if (!(worldIn instanceof WorldServer server)) {
            EndReforked.LOGGER.error("Unable to run world generator on ClientWorld");
            return false;
        }

        position = getStartPos(server, rand, position, getValidator());
        if (Objects.isNull(position)) return false;
        return doGenerate(server, rand, position);
    }

    protected abstract boolean doGenerate(WorldServer worldIn, Random rand, BlockPos position);

    protected PositionValidator getValidator() {
        return PositionValidators.ALWAYS_TRUE;
    }
}
