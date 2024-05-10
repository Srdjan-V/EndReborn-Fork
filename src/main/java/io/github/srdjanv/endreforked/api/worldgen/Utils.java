package io.github.srdjanv.endreforked.api.worldgen;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class Utils {
    public static List<Generator> getApplicableGeneratorsForChunk(Set<Generator> generatorMap, World world,
                                                                  Biome biome) {
        final var dim = world.provider.getDimension();
        return generatorMap.stream()
                .filter(entry -> isValidDimension(dim, entry))
                .filter(entry -> isValidBiome(biome, entry))
                .collect(Collectors.toList());
    }

    public static boolean isValidGen(int dim, Biome biome, Generator config) {
        return isValidDimension(dim, config) && isValidBiome(biome, config);
    }

    public static boolean isValidDimension(int dim, Generator config) {
        if (!config.getDimBlackList().isEmpty())
            if (config.getDimBlackList().contains(dim))
                return false;

        if (!config.getDimConfigs().isEmpty())
            return config.getDimConfigs().containsKey(dim);

        return true;
    }

    public static boolean isValidBiome(Biome biome, Generator config) {
        if (!config.getBiomeBlackList().isEmpty())
            if (config.getBiomeBlackList().contains(biome))
                return false;

        if (!config.getBiomeConfigs().isEmpty())
            return config.getBiomeConfigs().containsKey(biome);

        for (var type : BiomeDictionary.getTypes(biome)) {
            if (!config.getBiomeTypeBlackList().isEmpty())
                if (config.getBiomeTypeBlackList().contains(type))
                    return false;

            if (!config.getBiomeTypeConfigs().isEmpty())
                return config.getBiomeTypeConfigs().containsKey(type);
        }

        return true;
    }


}
