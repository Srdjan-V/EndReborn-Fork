package io.github.srdjanv.endreforked.api.worldgen;

import java.util.*;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;

import net.minecraftforge.common.BiomeDictionary;
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

    private final Map<BiomeDictionary.Type, GenConfig> biomeTypeConfigs;
    private final List<BiomeDictionary.Type> biomeTypeBlackList;

    private final Int2ObjectMap<GenConfig> dimConfigs;
    private final IntList dimBlackList;

    private final GenConfig defaultGenConfig;

    public Generator(String name, int weight, GeneratorBuilder builder,
                     Map<Biome, GenConfig> biomeConfigs, List<Biome> biomeBlackList,
                     Map<BiomeDictionary.Type, GenConfig> biomeTypeConfigs, List<BiomeDictionary.Type> biomeTypeBlackList,
                     Int2ObjectMap<GenConfig> dimConfigs, IntList dimBlackList, GenConfig defaultGenConfig) {
        this.name = name;
        this.weight = weight;
        this.builder = builder;
        this.biomeConfigs = Collections.unmodifiableMap(biomeConfigs);
        this.biomeBlackList = Collections.unmodifiableList(biomeBlackList);
        this.biomeTypeConfigs = Collections.unmodifiableMap(biomeTypeConfigs);
        this.biomeTypeBlackList = Collections.unmodifiableList(biomeTypeBlackList);
        this.dimConfigs = Int2ObjectMaps.unmodifiable(dimConfigs);
        this.dimBlackList = IntLists.unmodifiable(dimBlackList);
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
            for (var type : BiomeDictionary.getTypes(biome)) {
                var resType = biomeTypeConfigs.get(type);
                if (Objects.nonNull(resType)) return resType;
            }

            return dimGenConfig;
        } else if (Objects.nonNull(biomeGenConfig)) {
            for (var type : BiomeDictionary.getTypes(biome)) {
                var resType = biomeTypeConfigs.get(type);
                if (Objects.nonNull(resType)) return resType;
            }
            return biomeGenConfig;
        }
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
    public Map<BiomeDictionary.Type, GenConfig> getBiomeTypeConfigs() {
        return biomeTypeConfigs;
    }

    @Unmodifiable
    public List<BiomeDictionary.Type> getBiomeTypeBlackList() {
        return biomeTypeBlackList;
    }

    @Unmodifiable
    public Int2ObjectMap<GenConfig> getDimConfigs() {
        return dimConfigs;
    }

    @Unmodifiable
    public IntList getDimBlackList() {
        return dimBlackList;
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

        private final Map<Biome, GenConfig> biomeWhiteListMap = new Object2ObjectOpenHashMap<>();
        private final List<Biome> biomeBlackList = new ObjectArrayList<>();

        private final Int2ObjectMap<GenConfig> dimWhiteListMap = new Int2ObjectOpenHashMap<>();
        private final IntList dimBlackList = new IntArrayList();

        private final Map<BiomeDictionary.Type, GenConfig> biomeTypeWhiteListMap = new Object2ObjectOpenHashMap<>();
        private final List<BiomeDictionary.Type> biomeTypeBlackList = new ObjectArrayList<>();

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
            dimWhiteListMap.defaultReturnValue(defaultGenConfig);
            return this;
        }

        ////////////////////////////////////////////////////////////////////
        public Builder biomeWhiteList(Biome... biome) {
            return biomeWhiteList(null, biome);
        }

        public Builder biomeWhiteList(List<Biome> biomes) {
            return biomeWhiteList(null, biomes);
        }

        public Builder biomeWhiteList(@Nullable GenConfig config, Biome... biome) {
            return biomeWhiteList(config, new ObjectArrayList<>(biome));
        }

        public Builder biomeWhiteList(@Nullable GenConfig config, List<Biome> biomes) {
            for (Biome biome : biomes) biomeWhiteListMap.put(biome, config);
            return this;
        }

        public Builder biomeBlackList(Biome... biome) {
            return biomeBlackList(new ObjectArrayList<>(biome));
        }

        public Builder biomeBlackList(List<Biome> biome) {
            biomeBlackList.addAll(biome);
            return this;
        }

        ////////////////////////////////////////////////////////////////////

        public Builder dimTypeWhiteList(BiomeDictionary.Type... types) {
            return dimTypeWhiteList(null, types);
        }

        public Builder dimTypeWhiteList(List<BiomeDictionary.Type> types) {
            return dimTypeWhiteList(null, types);
        }

        public Builder dimTypeWhiteList(@Nullable GenConfig config, BiomeDictionary.Type... types) {
            return dimTypeWhiteList(config, new ObjectArrayList<>(types));
        }

        public Builder dimTypeWhiteList(@Nullable GenConfig config, List<BiomeDictionary.Type> types) {
            for (var type : types) biomeTypeWhiteListMap.put(type, config);
            return this;
        }

        public Builder dimTypeBlackList(BiomeDictionary.Type... types) {
            return dimTypeBlackList(new ObjectArrayList<>(types));
        }

        public Builder dimTypeBlackList(List<BiomeDictionary.Type> types) {
            biomeTypeBlackList.addAll(types);
            return this;
        }

        ////////////////////////////////////////////////////////////////////

        public Builder dimWhiteList(int... dims) {
            return dimWhiteList(null, dims);
        }

        public Builder dimWhiteList(IntList dims) {
            return dimWhiteList(null, dims);
        }

        public Builder dimWhiteList(@Nullable GenConfig config, int... dims) {
            return dimWhiteList(config, new IntArrayList(dims));
        }

        public Builder dimWhiteList(@Nullable GenConfig config, IntList biomes) {
            for (var dim : biomes) dimWhiteListMap.put(dim, config);
            return this;
        }

        public Builder dimBlackList(int... dim) {
            return dimBlackList(new IntArrayList(dim));
        }

        public Builder dimBlackList(IntList dim) {
            dimBlackList.addAll(dim);
            return this;
        }

        public Generator build() {
            return new Generator(
                    Objects.requireNonNull(name),
                    Objects.requireNonNull(weight),
                    Objects.requireNonNull(generatorBuilder),
                    biomeWhiteListMap, biomeBlackList,
                    biomeTypeWhiteListMap, biomeTypeBlackList,
                    dimWhiteListMap, dimBlackList,
                    defaultGenConfig);
        }
    }
}
