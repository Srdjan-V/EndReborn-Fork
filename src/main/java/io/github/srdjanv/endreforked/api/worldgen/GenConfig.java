package io.github.srdjanv.endreforked.api.worldgen;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class GenConfig implements Comparable<GenConfig> {

    private final String generatorName;
    private final int generatorWeight;
    private final GeneratorBuilder generatorBuilder;

    private final Map<Biome, DimConfig> biomeConfigs;
    private final List<Biome> biomeBlackList;

    private final IntList dimBlackList;
    private final Int2ObjectMap<DimConfig> dimConfigs;
    private final DimConfig defaultDimConfig;

    public GenConfig(String generatorName, int generatorWeight, GeneratorBuilder generatorBuilder,
                     Map<Biome, DimConfig> biomeConfigs, List<Biome> biomeBlackList,
                     IntList dimBlackList, Int2ObjectMap<DimConfig> dimConfigs, DimConfig defaultDimConfig) {
        this.generatorName = generatorName;
        this.generatorWeight = generatorWeight;
        this.generatorBuilder = generatorBuilder;
        this.biomeConfigs = Collections.unmodifiableMap(biomeConfigs);
        this.biomeBlackList = Collections.unmodifiableList(biomeBlackList);
        this.dimBlackList = IntLists.unmodifiable(dimBlackList);
        this.dimConfigs = Int2ObjectMaps.unmodifiable(dimConfigs);
        this.defaultDimConfig = defaultDimConfig;
    }

    public String getGeneratorName() {
        return generatorName;
    }

    public @Nullable DimConfig getDimConfig(@Nullable Integer dim, @Nullable Biome biome) {
        final var dimDimConfig = Objects.isNull(dim) ? null : dimConfigs.get(dim);
        final var biomeDimConfig = Objects.isNull(biome) ? null : biomeConfigs.get(biome);

        if (Objects.nonNull(dimDimConfig)) {
            if (Objects.nonNull(biomeDimConfig)) return biomeDimConfig;
            return dimDimConfig;
        } else if (Objects.nonNull(biomeDimConfig)) return biomeDimConfig;
        return defaultDimConfig;
    }

    public @Nullable WorldGenerator getDefaultGenerator(@NotNull World world) {
        return getGenerator(world, null, (Integer) null);
    }

    public @Nullable WorldGenerator getGenerator(@NotNull World world, @Nullable Biome biome, @Nullable Integer dim) {
        return getGenerator(world, biome, getDimConfig(dim, biome));
    }

    public @Nullable WorldGenerator getGenerator(@NotNull World world, @Nullable Biome biome,
                                                 @Nullable DimConfig config) {
        if (Objects.isNull(config)) {
            if (Objects.isNull(defaultDimConfig)) return null;
            return generatorBuilder.getGenerator(world, biome, defaultDimConfig);
        }
        return generatorBuilder.getGenerator(world, biome, config);
    }

    @Unmodifiable
    public Map<Biome, DimConfig> getBiomeConfigs() {
        return biomeConfigs;
    }

    @Unmodifiable
    public List<Biome> getBiomeBlackList() {
        return biomeBlackList;
    }

    @Unmodifiable
    public IntList getDimBlackList() {
        return dimBlackList;
    }

    @Unmodifiable
    public Int2ObjectMap<DimConfig> getDimConfigs() {
        return dimConfigs;
    }

    @Nullable
    public DimConfig getDefaultDimConfig() {
        return defaultDimConfig;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "GenConfig{" +
                "generatorName='" + generatorName + '\'' +
                ", generatorWeight=" + generatorWeight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenConfig genConfig = (GenConfig) o;
        return Objects.equals(generatorName, genConfig.generatorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(generatorName);
    }

    @Override
    public int compareTo(@NotNull GenConfig o) {
        return o.generatorWeight - generatorWeight;
    }

    public static class Builder {

        private String generatorName;
        private Integer generatorWeight;
        private GeneratorBuilder generatorBuilder;

        private final Map<Biome, DimConfig> biomeWhiteList = new Object2ObjectOpenHashMap<>();
        private final List<Biome> biomeBlackList = new ObjectArrayList<>();
        private final IntList dimBlackList = new IntArrayList();
        private final Int2ObjectMap<DimConfig> dimConfigs = new Int2ObjectOpenHashMap<>();
        private DimConfig defaultDimConfig;

        private Builder() {}

        public Builder generatorName(String generatorName) {
            this.generatorName = generatorName;
            return this;
        }

        public Builder weight(int generatorWeight) {
            this.generatorWeight = generatorWeight;
            return this;
        }

        public Builder generatorBuilder(GeneratorBuilder generatorBuilder) {
            this.generatorBuilder = generatorBuilder;
            return this;
        }

        public Builder defaultDimConfig(DimConfig defaultDimConfig) {
            this.defaultDimConfig = defaultDimConfig;
            dimConfigs.defaultReturnValue(defaultDimConfig);
            return this;
        }

        public Builder biomeWhiteList(Biome biome) {
            return biomeWhiteList(biome, null);
        }

        public Builder biomeWhiteList(Biome biome, @Nullable DimConfig config) {
            biomeWhiteList.put(biome, config);
            return this;
        }

        public Builder biomeBlackList(Biome biome) {
            biomeBlackList.add(biome);
            return this;
        }

        public Builder dimBlackList(int dim) {
            dimBlackList.add(dim);
            return this;
        }

        public Builder dimWhiteList(int dim) {
            return dimWhiteList(dim, null);
        }

        public Builder dimWhiteList(int dim, @Nullable DimConfig config) {
            dimConfigs.put(dim, config);
            return this;
        }

        public GenConfig build() {
            return new GenConfig(
                    Objects.requireNonNull(generatorName),
                    Objects.requireNonNull(generatorWeight),
                    Objects.requireNonNull(generatorBuilder),
                    biomeWhiteList, biomeBlackList, dimBlackList, dimConfigs, defaultDimConfig);
        }
    }
}
