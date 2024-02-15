package io.github.srdjanv.endreforked.common.configs.worldgen.schema;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.world.biome.Biome;

import org.jetbrains.annotations.Nullable;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.api.worldgen.DimConfig;
import io.github.srdjanv.endreforked.api.worldgen.GenConfig;
import io.github.srdjanv.endreforked.api.worldgen.GeneratorBuilder;
import io.github.srdjanv.endreforked.common.configs.base.ResourceLocationWrapper;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class WorldGenSchema {

    public boolean enableGeneration = true;
    public int weight = 0;
    public DimConfig defaultGenConfigForData;

    public final DimData dimData = new DimData();

    public static class DimData {

        public final List<Integer> blackList = new ObjectArrayList<>();
        public final List<Integer> whiteListDefaultConfig = new ObjectArrayList<>(1);
        public final Map<Integer, DimConfig> whiteList = new Object2ObjectOpenHashMap<>(1);

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DimData dimData = (DimData) o;
            return Objects.equals(blackList, dimData.blackList) &&
                    Objects.equals(whiteListDefaultConfig, dimData.whiteListDefaultConfig) &&
                    Objects.equals(whiteList, dimData.whiteList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(blackList, whiteListDefaultConfig, whiteList);
        }
    }

    public final BiomeData biomeData = new BiomeData();

    public static class BiomeData {

        public final List<ResourceLocationWrapper> blackList = new ObjectArrayList<>(1);
        public final List<ResourceLocationWrapper> whiteListDefaultConfig = new ObjectArrayList<>(1);
        public final Map<ResourceLocationWrapper, DimConfig> whiteList = new Object2ObjectOpenHashMap<>(1);

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BiomeData biomeData = (BiomeData) o;
            return Objects.equals(blackList, biomeData.blackList) &&
                    Objects.equals(whiteListDefaultConfig, biomeData.whiteListDefaultConfig) &&
                    Objects.equals(whiteList, biomeData.whiteList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(blackList, whiteListDefaultConfig, whiteList);
        }
    }

    public GenConfig parseConfig(String name, GeneratorBuilder generatorBuilder) {
        GenConfig.Builder<?> gen = GenConfig.builder();
        gen.setGeneratorName(name)
                .setGeneratorWeight(weight)
                .setGeneratorBuilder(generatorBuilder);
        parseBiomes(gen);
        parseDims(gen);
        gen.setDefaultDimConfig(defaultGenConfigForData);
        return gen.build();
    }

    private void parseDims(GenConfig.Builder<?> gen) {
        for (Integer dim : dimData.blackList)
            gen.addDimToBlackList(dim);

        for (Integer dim : dimData.whiteListDefaultConfig)
            gen.addDimToWhiteList(dim);

        for (var configEntry : dimData.whiteList.entrySet())
            gen.addDimToWhiteList(configEntry.getKey(), configEntry.getValue());
    }

    private void parseBiomes(GenConfig.Builder<?> gen) {
        for (ResourceLocationWrapper biome : biomeData.blackList) {
            final var res = resolveBiome(biome);
            if (Objects.isNull(res)) continue;
            gen.addBiomeToBlackList(res);
        }

        for (ResourceLocationWrapper biome : biomeData.whiteListDefaultConfig) {
            final var res = resolveBiome(biome);
            if (Objects.isNull(res)) continue;
            gen.addBiomeToWhiteList(res);
        }

        for (Map.Entry<ResourceLocationWrapper, DimConfig> entry : biomeData.whiteList.entrySet()) {
            final var biomeRes = resolveBiome(entry.getKey());
            if (Objects.isNull(biomeRes)) continue;
            gen.addBiomeToWhiteList(biomeRes, entry.getValue());
        }
    }

    @Nullable
    private static Biome resolveBiome(ResourceLocationWrapper biome) {
        var resBiome = Biome.REGISTRY.getObject(biome.get());
        if (Objects.isNull(resBiome)) {
            EndReforked.LOGGER.warn("Unable to find biome `{}`", biome);
            return null;
        }
        return resBiome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldGenSchema that = (WorldGenSchema) o;
        return enableGeneration == that.enableGeneration && weight == that.weight &&
                Objects.equals(defaultGenConfigForData, that.defaultGenConfigForData) &&
                Objects.equals(dimData, that.dimData) && Objects.equals(biomeData, that.biomeData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enableGeneration, weight, defaultGenConfigForData, dimData, biomeData);
    }
}
