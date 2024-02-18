package io.github.srdjanv.endreforked.api.worldgen.features;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import io.github.srdjanv.endreforked.api.worldgen.DimConfig;
import io.github.srdjanv.endreforked.api.worldgen.base.*;

public class SphereGenerator extends PositionedFeature {

    protected final PositionGenerator positionGenerator;
    protected final PositionValidator startPosValidator;
    protected final PositionValidator positionValidator;

    public SphereGenerator(DimConfig config, PositionValidator positionValidator, PositionGenerator positionGenerator) {
        this(config, PositionValidators.ALWAYS_TRUE, positionValidator, positionGenerator);
    }

    public SphereGenerator(DimConfig config, PositionValidator startPosValidator, PositionValidator positionValidator,
                           PositionGenerator positionGenerator) {
        super(Locators.DIM_CONFIG_MIN_MAX, config);
        this.startPosValidator = startPosValidator;
        this.positionValidator = positionValidator;
        this.positionGenerator = positionGenerator;
    }

    @Override
    protected boolean doGenerate(WorldServer server, Random rand, BlockPos startPos) {
        int radius = config.amountModifier();
        int startX = startPos.getX();
        int startY = startPos.getY();
        int startZ = startPos.getZ();

        for (int x = startX - radius; x <= startX + radius; x++) {
            for (int y = startY - radius; y <= startY + radius; y++) {
                for (int z = startZ - radius; z <= startZ + radius; z++) {
                    if (Math.pow(x - startX, 2) + Math.pow(y - startY, 2) + Math.pow(z - startZ, 2) <=
                            Math.pow(radius, 2)) {
                        var pos = new BlockPos(x, y, z);
                        if (positionValidator.validate(server, rand, pos))
                            positionGenerator.generate(server, rand, pos);
                    }
                }
            }
        }

        return true;
    }

    @Override
    protected PositionValidator getStartPosValidator() {
        return startPosValidator;
    }
}
