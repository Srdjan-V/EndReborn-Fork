package io.github.srdjanv.endreforked.api.worldgen.features;

import io.github.srdjanv.endreforked.api.worldgen.DimConfig;
import io.github.srdjanv.endreforked.api.worldgen.base.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.Random;

public class FilledSphereGenerator extends PositionedFeature {
    protected final PositionGenerator innerPosGenerator;
    protected final PositionValidator innerPositionValidator;

    protected final PositionGenerator outerPosGenerator;
    protected final PositionValidator outerPositionValidator;

    protected final PositionValidator startPosValidator;

    public FilledSphereGenerator(DimConfig config, PositionValidator positionValidator,
                                 PositionGenerator innerPosGenerator,
                                 PositionGenerator outerPosGenerator) {
        this(config, PositionValidators.ALWAYS_TRUE, positionValidator, innerPosGenerator, outerPosGenerator);
    }

    public FilledSphereGenerator(DimConfig config, PositionValidator startPosValidator,
                                 PositionValidator positionValidator,
                                 PositionGenerator innerPosGenerator, PositionGenerator outerPosGenerator) {
        this(config, startPosValidator,
                positionValidator, innerPosGenerator,
                positionValidator, outerPosGenerator);
    }

    public FilledSphereGenerator(DimConfig config,
                                 PositionValidator innerPositionValidator, PositionGenerator innerPosGenerator,
                                 PositionValidator outerPositionValidator, PositionGenerator outerPosGenerator) {
        this(config, PositionValidators.ALWAYS_TRUE,
                innerPositionValidator, innerPosGenerator,
                outerPositionValidator, outerPosGenerator);
    }

    public FilledSphereGenerator(DimConfig config, PositionValidator startPosValidator,
                                 PositionValidator innerPositionValidator, PositionGenerator innerPosGenerator,
                                 PositionValidator outerPositionValidator, PositionGenerator outerPosGenerator) {
        super(Locators.OFFSET_16.andThenLocate(Locators.DIM_CONFIG_MIN_MAX), config);
        this.startPosValidator = startPosValidator;

        this.innerPositionValidator = innerPositionValidator;
        this.innerPosGenerator = innerPosGenerator;

        this.outerPositionValidator = outerPositionValidator;
        this.outerPosGenerator = outerPosGenerator;
    }


    @Override protected boolean doGenerate(WorldServer server, Random rand, BlockPos startPos) {
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

                        double distance = startPos.getDistance(x, y, z);

                        // Check if the distance is less than or equal to the radius of the sphere
                        if (distance <= (double) radius / 2) {
                            if (innerPositionValidator.validate(server, rand, pos))
                                innerPosGenerator.generate(server, rand, pos);
                        } else {
                            if (outerPositionValidator.validate(server, rand, pos))
                                outerPosGenerator.generate(server, rand, pos);
                        }
                    }
                }
            }
        }

        return true;
    }
}
