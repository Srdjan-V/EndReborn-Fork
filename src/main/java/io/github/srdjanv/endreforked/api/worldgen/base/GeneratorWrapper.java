package io.github.srdjanv.endreforked.api.worldgen.base;

import io.github.srdjanv.endreforked.EndReforked;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class GeneratorWrapper extends WorldGenerator {
    private final List<WorldGenerator> featureList = new ObjectArrayList<>();
    private final List<WorldGeneratorBuilder> builders = new ObjectArrayList<>();

    public GeneratorWrapper(WorldGenerator... featureList) {
        this(Arrays.asList(featureList));
    }

    public GeneratorWrapper(List<WorldGenerator> featureList) {
        this.featureList.addAll(featureList);
    }

    public GeneratorWrapper subGen(WorldGeneratorBuilder builder) {
        this.builders.add(builder);
        return this;
    }

    public GeneratorWrapper subGen(List<WorldGeneratorBuilder> builders) {
        builders.forEach(this::subGen);
        return this;
    }

    @Override public boolean generate(World worldIn, Random rand, BlockPos position) {
        if (!(worldIn instanceof WorldServer server)) {
            EndReforked.LOGGER.error("Unable to run world generator on ClientWorld");
            return false;
        }

        featureList.addAll(builders.stream()
                .map(builder-> builder.build(server, rand, position))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        boolean flag = false;
        for (var gen : featureList) {
            flag |= gen.generate(worldIn, rand, position);
        }
        return flag;
    }
}
