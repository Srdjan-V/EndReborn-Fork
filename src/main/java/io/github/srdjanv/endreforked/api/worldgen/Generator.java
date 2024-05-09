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

public class Generator implements Comparable<Generator> {

    private final String name;
    private final int weight;
    private final GeneratorBuilder builder;

    private final Map<Biome, GenConfig> biomeConfigs;
    private final List<Biome> biomeBlackList;

    private final IntList dimBlackList;
    private final Int2ObjectMap<GenConfig> dimConfigs;
    private final GenConfig defaultGenConfig;

    public Generator(String name, int weight, GeneratorBuilder builder,
                     Map<Biome, GenConfig> biomeConfigs, List<Biome> biomeBlackList,
                     IntList dimBlackList, Int2ObjectMap<GenConfig> dimConfigs, GenConfig defaultGenConfig) {
        this.name = name;
        this.weight = weight;
        this.builder = builder;
        this.biomeConfigs = Collections.unmodifiableMap(biomeConfigs);
        this.biomeBlackList = Collections.unmodifiableList(biomeBlackList);
        this.dimBlackList = IntLists.unmodifiable(dimBlackList);
        this.dimConfigs = Int2ObjectMaps.unmodifiable(dimConfigs);
        this.defaultGenConfig = defaultGenConfig;
    }

    public String getName() {
        return name;
    }

    public @Nullable GenConfig getDimConfig(@Nullable Integer dim, @Nullable Biome biome) {
        final var dimGenConfig = Objects.isNull(dim) ? null : dimConfigs.get(dim);
        final var biomeGenConfig = Objects.isNull(biome) ? null : biomeConfigs.get(biome);

        if (Objects.nonNull(dimGenConfig)) {
            if (Objects.nonNull(biomeGenConfig)) return biomeGenConfig;
            return dimGenConfig;
        } else if (Objects.nonNull(biomeGenConfig)) return biomeGenConfig;
        return defaultGenConfig;
    }

    public @Nullable WorldGenerator getDefaultGenerator(@NotNull World world) {
        return getGenerator(world, null, (Integer) null);
    }

    public @Nullable WorldGenerator getGenerator(@NotNull World world, @Nullable Biome biome, @Nullable Integer dim) {
        return getGenerator(world, biome, getDimConfig(dim, biome));
    }

    public @Nullable WorldGenerator getGenerator(@NotNull World world, @Nullable Biome biome,
                                                 @Nullable GenConfig config) {
        if (Objects.isNull(config)) {
            if (Objects.isNull(defaultGenConfig)) return null;
            return builder.build(world, biome, defaultGenConfig);
        }
        return builder.build(world, biome, config);
    }

    @Unmodifiable
    public Map<Biome, GenConfig> getBiomeConfigs() {
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
    public Int2ObjectMap<GenConfig> getDimConfigs() {
        return dimConfigs;
    }

    @Nullable
    public GenConfig getDefaultGenConfig() {
        return defaultGenConfig;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override public String toString() {
        return "Generator{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Generator generator = (Generator) o;
        return Objects.equals(name, generator.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(@NotNull Generator o) {
        return o.weight - weight;
    }

    public static class Builder {

        private String name;
        private Integer weight;
        private GeneratorBuilder generatorBuilder;

        private final Map<Biome, GenConfig> biomeWhiteList = new Object2ObjectOpenHashMap<>();
        private final List<Biome> biomeBlackList = new ObjectArrayList<>();
        private final IntList dimBlackList = new IntArrayList();
        private final Int2ObjectMap<GenConfig> genConfigs = new Int2ObjectOpenHashMap<>();
        private GenConfig defaultGenConfig;

        private Builder() {}

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder weight(int generatorWeight) {
            this.weight = generatorWeight;
            return this;
        }

        public Builder generatorBuilder(GeneratorBuilder generatorBuilder) {
            this.generatorBuilder = generatorBuilder;
            return this;
        }

        public Builder defaultDimConfig(GenConfig defaultGenConfig) {
            this.defaultGenConfig = defaultGenConfig;
            genConfigs.defaultReturnValue(defaultGenConfig);
            return this;
        }

        public Builder biomeWhiteList(Biome biome) {
            return biomeWhiteList(biome, null);
        }

        public Builder biomeWhiteList(Biome biome, @Nullable GenConfig config) {
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

        public Builder dimWhiteList(int dim, @Nullable GenConfig config) {
            genConfigs.put(dim, config);
            return this;
        }

        public Generator build() {
            return new Generator(
                    Objects.requireNonNull(name),
                    Objects.requireNonNull(weight),
                    Objects.requireNonNull(generatorBuilder),
                    biomeWhiteList, biomeBlackList, dimBlackList, genConfigs, defaultGenConfig);
        }
    }
}
