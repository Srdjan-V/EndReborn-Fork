package io.github.srdjanv.endreforked.api.worldgen.base;

import java.util.*;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;

import org.jetbrains.annotations.Nullable;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.api.worldgen.GenConfig;

public abstract class PositionedFeature extends WorldGenerator implements SpacedGen {
    protected final List<Locator> locators;
    protected final GenConfig config;
    protected boolean isSpacedGenDisabled = false;

    protected PositionedFeature(Locator locator, GenConfig config) {
        this(Collections.singletonList(locator), config);
    }

    protected PositionedFeature(GenConfig config, Locator... locators) {
        this(Arrays.asList(locators), config);
    }

    protected PositionedFeature(List<Locator> locator, GenConfig config) {
        this.locators = locator;
        this.config = config;
    }

    @Override public void setDisabled(boolean flag) {
        isSpacedGenDisabled = flag;
    }

    @Override public boolean isDisabled() {
        return isSpacedGenDisabled;
    }

    @Nullable
    protected BlockPos getStartPos(WorldServer server, Random rand, BlockPos pos, PositionValidator validator) {
        BlockPos retPos = null;
        for (Locator locator : locators) {
            retPos = locator.compute(server, rand, config, pos, validator);
            if (Objects.nonNull(retPos)) break;
        }
        return retPos;
    }

    @Override
    public final boolean generate(World worldIn, Random rand, BlockPos position) {
        if (!(worldIn instanceof WorldServer server)) {
            EndReforked.LOGGER.error("Unable to run world generator on ClientWorld");
            return false;
        }

        BlockPos newPos = getStartPos(server, rand, position, getStartPosValidator());
        if (Objects.isNull(newPos)) return false;
        if (shouldSpacedGen(config) && !validSpacing(server, config, position))
            return false;
        return doGenerate(server, rand, newPos);
    }

    protected abstract boolean doGenerate(WorldServer server, Random rand, BlockPos startPos);

    protected PositionValidator getStartPosValidator() {
        return PositionValidators.ALWAYS_TRUE;
    }
}
