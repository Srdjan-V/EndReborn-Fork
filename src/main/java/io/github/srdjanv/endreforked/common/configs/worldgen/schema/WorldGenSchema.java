package io.github.srdjanv.endreforked.common.configs.worldgen.schema;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import net.minecraft.util.ResourceLocation;
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

    public final boolean enableGeneration;
    public final int weight;
    public final DimConfig dimConfigFallback;

    public final Data<Integer> dimData;
    public final Data<ResourceLocationWrapper> biomeData;

    public static class Data<D> {

        public final List<D> blackList;
        public final List<D> whiteList;
        public final Map<D, DimConfig> whiteListMap;

        public Data(List<D> blackList, List<D> whiteList, Map<D, DimConfig> whiteListMap) {
            this.blackList = blackList;
            this.whiteList = whiteList;
            this.whiteListMap = whiteListMap;
        }

        public Data() {
            this.blackList = null;
            this.whiteList = null;
            this.whiteListMap = null;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Data<?> data = (Data<?>) o;
            return Objects.equals(blackList, data.blackList) && Objects.equals(whiteList, data.whiteList) && Objects.equals(whiteListMap, data.whiteListMap);
        }

        @Override
        public int hashCode() {
            return Objects.hash(blackList, whiteList, whiteListMap);
        }
    }

    private WorldGenSchema(boolean enableGeneration, int weight, DimConfig dimConfigFallback, Data<Integer> dimData, Data<ResourceLocationWrapper> biomeData) {
        this.enableGeneration = enableGeneration;
        this.weight = weight;
        this.dimConfigFallback = dimConfigFallback;
        this.dimData = dimData;
        this.biomeData = biomeData;
    }

    private WorldGenSchema() {
        this.weight = 0;
        this.enableGeneration = false;
        this.dimConfigFallback = null;
        this.dimData = new Data<>();
        this.biomeData = new Data<>();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Boolean enableGeneration;
        private Integer weight = 0;
        private DimConfig dimConfigFallback;

        private final DataWrapper<Integer> dimData = new DataWrapper<>();
        private final DataWrapper<ResourceLocationWrapper> biomeData = new DataWrapper<>();

        public Builder enableGeneration(boolean enableGeneration) {
            this.enableGeneration = enableGeneration;
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public Builder dimConfigFallback(DimConfig dimConfigFallback) {
            this.dimConfigFallback = dimConfigFallback;
            return this;
        }

        ////////////////////////////////////////////////////////////////////

        public Builder blackListDim(int dim) {
            this.dimData.blackList.add(dim);
            return this;
        }

        public Builder blackListDim(int... dim) {
            return blackListDim(Arrays.stream(dim).boxed().collect(Collectors.toList()));
        }

        public Builder blackListDim(List<Integer> dims) {
            this.dimData.blackList.addAll(dims);
            return this;
        }

        public Builder whiteListDim(int dim) {
            this.dimData.whiteList.add(dim);
            return this;
        }

        public Builder whiteListDim(int dim, DimConfig dimConfig) {
            this.dimData.whiteListMap.put(dim, dimConfig);
            return this;
        }

        public Builder whiteListDim(DimConfig dimConfig, int... dim) {
            return whiteListDim(dimConfig, Arrays.stream(dim).boxed().collect(Collectors.toList()));
        }

        public Builder whiteListDim(DimConfig dimConfig, List<Integer> dims) {
            for (int dim : dims) this.dimData.whiteListMap.put(dim, dimConfig);
            return this;
        }

        ////////////////////////////////////////////////////////////////////

        public Builder blackListBiome(String biome) {
            return blackListBiome(ResourceLocationWrapper.of(new ResourceLocation(biome)));
        }

        public Builder blackListBiome(ResourceLocationWrapper wrapper) {
            this.biomeData.blackList.add(wrapper);
            return this;
        }

        public Builder blackListBiome(String... biome) {
            return blackListBiome(Arrays.stream(biome)
                    .map(ResourceLocationWrapper::of)
                    .collect(Collectors.toList()));
        }


        public Builder blackListBiome(ResourceLocationWrapper... wrapper) {
            return blackListBiome(Arrays.stream(wrapper).collect(Collectors.toList()));
        }

        public Builder blackListBiome(List<ResourceLocationWrapper> wrappers) {
            this.biomeData.whiteList.addAll(wrappers);
            return this;
        }

        ////////////////////////////////////////////////////////////////////

        public Builder whiteListBiome(String biome) {
            this.biomeData.whiteList.add(ResourceLocationWrapper.of(biome));
            return this;
        }

        public Builder whiteListBiome(ResourceLocationWrapper wrapper) {
            this.biomeData.whiteList.add(wrapper);
            return this;
        }

        public Builder whiteListBiome(String... biome) {
            return whiteListBiome(Arrays.stream(biome)
                    .map(ResourceLocationWrapper::of)
                    .collect(Collectors.toList()));
        }

        public Builder whiteListBiome(ResourceLocationWrapper... wrapper) {
            return whiteListBiome(Arrays.stream(wrapper).collect(Collectors.toList()));
        }


        public Builder whiteListBiome(List<ResourceLocationWrapper> wrappers) {
            this.biomeData.whiteList.addAll(wrappers);
            return this;
        }

        public Builder whiteListBiome(String biome, DimConfig dimConfig) {
            return whiteListBiome(ResourceLocationWrapper.of(biome), dimConfig);
        }


        public Builder whiteListBiome(ResourceLocationWrapper wrapper, DimConfig dimConfig) {
            this.biomeData.whiteListMap.put(wrapper, dimConfig);
            return this;
        }

        public Builder whiteListBiome(DimConfig dimConfig, String... biome) {
            return whiteListBiome(dimConfig, Arrays.stream(biome)
                    .map(ResourceLocationWrapper::of)
                    .collect(Collectors.toList()));
        }

        public Builder whiteListBiome(DimConfig dimConfig, ResourceLocationWrapper... wrapper) {
            return whiteListBiome(dimConfig, Arrays.stream(wrapper).collect(Collectors.toList()));
        }

        public Builder whiteListBiome(DimConfig dimConfig, List<ResourceLocationWrapper> wrappers) {
            for (ResourceLocationWrapper locationWrapper : wrappers)
                this.biomeData.whiteListMap.put(locationWrapper, dimConfig);
            return this;
        }

        public WorldGenSchema build() {
            return new WorldGenSchema(
                    Objects.requireNonNull(enableGeneration),
                    Objects.requireNonNull(weight),
                    dimConfigFallback,
                    dimData.build(), biomeData.build());
        }

        private static class DataWrapper<D> {

            private final List<D> blackList = new ObjectArrayList<>(1);
            private final List<D> whiteList = new ObjectArrayList<>(1);
            private final Map<D, DimConfig> whiteListMap = new Object2ObjectOpenHashMap<>(1);

            private Data<D> build() {
                return new Data<>(blackList, whiteList, whiteListMap);
            }
        }
    }

    public GenConfig parseConfig(String name, GeneratorBuilder generatorBuilder) {
        GenConfig.Builder<?> gen = GenConfig.builder();
        gen.setGeneratorName(name)
                .setGeneratorWeight(weight)
                .setGeneratorBuilder(generatorBuilder);
        parseBiomes(gen);
        parseDims(gen);
        gen.setDefaultDimConfig(dimConfigFallback);
        return gen.build();
    }

    private void parseDims(GenConfig.Builder<?> gen) {
        for (Integer dim : dimData.blackList)
            gen.addDimToBlackList(dim);

        for (Integer dim : dimData.whiteList)
            gen.addDimToWhiteList(dim);

        for (var configEntry : dimData.whiteListMap.entrySet())
            gen.addDimToWhiteList(configEntry.getKey(), configEntry.getValue());
    }

    private void parseBiomes(GenConfig.Builder<?> gen) {
        for (ResourceLocationWrapper biome : biomeData.blackList) {
            final var res = resolveBiome(biome);
            if (Objects.isNull(res)) continue;
            gen.addBiomeToBlackList(res);
        }

        for (ResourceLocationWrapper biome : biomeData.whiteList) {
            final var res = resolveBiome(biome);
            if (Objects.isNull(res)) continue;
            gen.addBiomeToWhiteList(res);
        }

        for (Map.Entry<ResourceLocationWrapper, DimConfig> entry : biomeData.whiteListMap.entrySet()) {
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
                Objects.equals(dimConfigFallback, that.dimConfigFallback) &&
                Objects.equals(dimData, that.dimData) && Objects.equals(biomeData, that.biomeData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enableGeneration, weight, dimConfigFallback, dimData, biomeData);
    }
}
