package io.github.srdjanv.endreforked.api.worldgen.features;

import io.github.srdjanv.endreforked.api.worldgen.DimConfig;
import io.github.srdjanv.endreforked.api.worldgen.base.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.Random;

public class RadiusSurfaceGenerator extends PositionedFeature {
    protected final PositionGenerator positionGenerator;
    protected final PositionValidator startPosValidator;
    protected final PositionValidator positionValidator;

    public RadiusSurfaceGenerator(DimConfig config, PositionValidator positionValidator, PositionGenerator positionGenerator) {
        this(config, PositionValidators.ALWAYS_TRUE, positionValidator, positionGenerator);
    }

    public RadiusSurfaceGenerator(DimConfig config, PositionValidator startPosValidator, PositionValidator positionValidator, PositionGenerator positionGenerator) {
        super(Locators.SURFACE_BLOCK, config);
        this.positionGenerator = positionGenerator;
        this.positionValidator = positionValidator;
        this.startPosValidator = startPosValidator;
    }


    @Override protected boolean doGenerate(WorldServer server, Random rand, BlockPos startPos) {
        int radius = config.amountModifier();
        int startX = startPos.getX();
        int startZ = startPos.getZ();

        for (int x = startX - radius; x <= startX + radius; x++) {
            TopFor:
            for (int z = startZ - radius; z <= startZ + radius; z++) {
                if (Math.pow(x - startX, 2) + Math.pow(z - startZ, 2) <= Math.pow(radius, 2)) {
                    var pos = new BlockPos(x, startPos.getY(), z);

                    for (int i = 0; i < 2; i++) {
                        if (server.isAirBlock(pos)) {
                            pos = pos.down();
                            if (server.isAirBlock(pos)) continue TopFor;
                        } else break;
                    }

                    for (int i = 0; i < 2; i++) {
                        var upPos = pos.up();
                        if (server.isAirBlock(upPos)) break;
                    }
                    if (!server.isAirBlock(pos.up())) continue;

                    if (!positionValidator.validate(server, rand, pos)) continue;
                    positionGenerator.generate(server, rand, pos);
                }
            }
        }

        return true;
    }

    @Override protected PositionValidator getStartPosValidator() {
        return startPosValidator;
    }
}
