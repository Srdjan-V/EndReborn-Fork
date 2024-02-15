package io.github.srdjanv.endreforked.common.configs.worldgen;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.github.srdjanv.endreforked.common.configs.base.ReloadableServerSideConfig;

public enum GenConfigs {

    GenericGen(GenericGenConfig.getInstance()),
    OreGen(OreGenConfig.getInstance()),
    StructureGen(StructureGenConfig.getInstance());

    public static ReloadableServerSideConfig<?> get(String name) {
        for (GenConfigs value : values()) {
            if (value.getConfig().getConfigName().equalsIgnoreCase(name))
                return value.getConfig();
        }

        return null;
    }

    private final ReloadableServerSideConfig<?> config;

    GenConfigs(ReloadableServerSideConfig<?> config) {
        this.config = config;
    }

    public ReloadableServerSideConfig<?> getConfig() {
        return config;
    }

    public static List<ReloadableServerSideConfig<?>> getAllConfigs() {
        return Arrays.stream(GenConfigs.values()).map(GenConfigs::getConfig).collect(Collectors.toList());
    }
}
