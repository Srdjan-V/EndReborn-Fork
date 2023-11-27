package endreborn.api.worldgen;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class GenConfig<G extends WorldGenerator> implements Comparable<GenConfig<G>> {

    private final String generatorName;
    private final int generatorWeight;
    private final GeneratorBuilder<G> generatorBuilder;

    private final Map<Biome, DimConfig> biomeConfigs;
    private final List<Biome> biomeBlackList;

    private final IntList dimBlackList;
    private final Int2ObjectMap<DimConfig> dimConfigs;
    private final DimConfig defaultDimConfig;

    public GenConfig(String generatorName, int generatorWeight, GeneratorBuilder<G> generatorBuilder,
                     Map<Biome, DimConfig> biomeConfigs, List<Biome> biomeBlackList,
                     IntList dimBlackList, Int2ObjectMap<DimConfig> dimConfigs, DimConfig defaultDimConfig) {
        this.generatorName = generatorName;
        this.generatorWeight = generatorWeight;
        this.generatorBuilder = generatorBuilder;
        this.biomeConfigs = biomeConfigs;
        this.biomeBlackList = biomeBlackList;
        this.dimBlackList = dimBlackList;
        this.dimConfigs = dimConfigs;
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

    public @Nullable G getDefaultGenerator(@NotNull World world) {
        return getGenerator(world, null, (Integer) null);
    }

    public @Nullable G getGenerator(@NotNull World world, @Nullable Biome biome, @Nullable Integer dim) {
        return getGenerator(world, biome, getDimConfig(dim, biome));
    }

    public @Nullable G getGenerator(@NotNull World world, @Nullable Biome biome, @Nullable DimConfig config) {
        if (Objects.isNull(config)) {
            if (Objects.isNull(defaultDimConfig)) return null;
            return generatorBuilder.getGenerator(world, biome, defaultDimConfig);
        }
        return generatorBuilder.getGenerator(world, biome, config);
    }

    public Map<Biome, DimConfig> getBiomeConfigs() {
        return biomeConfigs;
    }

    public List<Biome> getBiomeBlackList() {
        return biomeBlackList;
    }

    public IntList getDimBlackList() {
        return dimBlackList;
    }

    public Int2ObjectMap<DimConfig> getDimConfigs() {
        return dimConfigs;
    }

    @Nullable
    public DimConfig getDefaultDimConfig() {
        return defaultDimConfig;
    }

    public static <G extends WorldGenerator> Builder<G> builder() {
        return new Builder<>();
    }

    @Override
    public int compareTo(@NotNull GenConfig<G> o) {
        return o.generatorWeight - generatorWeight;
    }

    public static class Builder<G extends WorldGenerator> {

        private String generatorName;
        private Integer generatorWeight;
        private GeneratorBuilder<G> generatorBuilder;

        private final Map<Biome, DimConfig> biomeWhiteList = new Object2ObjectOpenHashMap<>();
        private final List<Biome> biomeBlackList = new ObjectArrayList<>();
        private final IntList dimBlackList = new IntArrayList();
        private final Int2ObjectMap<DimConfig> dimConfigs = new Int2ObjectOpenHashMap<>();
        private DimConfig defaultDimConfig;

        private Builder() {}

        public Builder<G> setGeneratorName(String generatorName) {
            this.generatorName = generatorName;
            return this;
        }

        public Builder<G> setGeneratorWeight(int generatorWeight) {
            this.generatorWeight = generatorWeight;
            return this;
        }

        public Builder<G> setGeneratorBuilder(GeneratorBuilder<G> generatorBuilder) {
            this.generatorBuilder = generatorBuilder;
            return this;
        }

        public Builder<G> setDefaultDimConfig(DimConfig defaultDimConfig) {
            this.defaultDimConfig = defaultDimConfig;
            dimConfigs.defaultReturnValue(defaultDimConfig);
            return this;
        }

        public Builder<G> addBiomeToWhiteList(Biome biome) {
            return addBiomeToWhiteList(biome, null);
        }

        public Builder<G> addBiomeToWhiteList(Biome biome, @Nullable DimConfig config) {
            biomeWhiteList.put(biome, config);
            return this;
        }

        public Builder<G> addBiomeToBlockList(Biome biome) {
            biomeBlackList.add(biome);
            return this;
        }

        public Builder<G> addDimToBlackList(int dim) {
            dimBlackList.add(dim);
            return this;
        }

        public Builder<G> addDimToWhiteList(int dim) {
            return addDimToWhiteList(dim, null);
        }

        public Builder<G> addDimToWhiteList(int dim, @Nullable DimConfig config) {
            dimConfigs.put(dim, config);
            return this;
        }

        public GenConfig<G> build() {
            return new GenConfig<>(
                    Objects.requireNonNull(generatorName),
                    Objects.requireNonNull(generatorWeight),
                    Objects.requireNonNull(generatorBuilder),
                    biomeWhiteList, biomeBlackList, dimBlackList, dimConfigs, defaultDimConfig);
        }
    }
}
