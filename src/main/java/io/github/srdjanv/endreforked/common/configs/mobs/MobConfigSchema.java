package io.github.srdjanv.endreforked.common.configs.mobs;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.github.bsideup.jabel.Desugar;

import io.github.srdjanv.endreforked.common.configs.base.ResourceLocationWrapper;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class MobConfigSchema {

    private final boolean register;
    private final boolean registerSpawn;
    private final SpawnConfig defaultConfig;
    private final List<ResourceLocationWrapper> defaultBiomeSpawns;
    private final Map<ResourceLocationWrapper, SpawnConfig> biomeSpawnConfig;

    public MobConfigSchema(boolean register, boolean registerSpawn, SpawnConfig defaultConfig,
                           List<ResourceLocationWrapper> defaultBiomeSpawns,
                           Map<ResourceLocationWrapper, SpawnConfig> biomeSpawnConfig) {
        this.register = register;
        this.registerSpawn = registerSpawn;
        this.defaultConfig = defaultConfig;
        this.defaultBiomeSpawns = defaultBiomeSpawns;
        this.biomeSpawnConfig = biomeSpawnConfig;
    }

    public MobConfigSchema() {
        defaultConfig = null;
        defaultBiomeSpawns = null;
        register = false;
        registerSpawn = false;
        biomeSpawnConfig = new Object2ObjectOpenHashMap<>();
    }

    public boolean isRegister() {
        return register;
    }

    public boolean isRegisterSpawn() {
        return registerSpawn;
    }

    public SpawnConfig getDefaultConfig() {
        return defaultConfig;
    }

    public List<ResourceLocationWrapper> getDefaultBiomeSpawns() {
        return defaultBiomeSpawns;
    }

    public Map<ResourceLocationWrapper, SpawnConfig> getBiomeSpawnConfig() {
        return biomeSpawnConfig;
    }

    @Desugar
    public record SpawnConfig(int spawnProbability, int maximumSpawnGroup, int minimumSpawnGroup) {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Boolean register;
        private Boolean registerSpawn;
        private SpawnConfig defaultSpawnConfig;
        private final Map<ResourceLocationWrapper, SpawnConfig> biomeSpawnConfig = new Object2ObjectOpenHashMap<>();
        private final List<ResourceLocationWrapper> defaultBiomeSpawns = new ObjectArrayList<>();

        private Builder() {}

        public Builder register(boolean register) {
            this.register = register;
            return this;
        }

        public Builder registerSpawn(boolean registerSpawn) {
            this.registerSpawn = registerSpawn;
            return this;
        }

        public Builder defaultBiomeSpawnConfig(SpawnConfig spawnConfig) {
            this.defaultSpawnConfig = spawnConfig;
            return this;
        }

        public Builder biomeSpawn(ResourceLocationWrapper wrapper) {
            this.defaultBiomeSpawns.add(wrapper);
            return this;
        }

        public Builder biomeSpawn(ResourceLocationWrapper wrapper, SpawnConfig spawnConfig) {
            this.biomeSpawnConfig.put(wrapper, spawnConfig);
            return this;
        }

        public MobConfigSchema build() {
            return new MobConfigSchema(Objects.requireNonNull(register),
                    Objects.requireNonNull(registerSpawn),
                    defaultSpawnConfig, defaultBiomeSpawns, biomeSpawnConfig);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MobConfigSchema that = (MobConfigSchema) o;
        return register == that.register && registerSpawn == that.registerSpawn &&
                Objects.equals(biomeSpawnConfig, that.biomeSpawnConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(register, registerSpawn, biomeSpawnConfig);
    }
}
