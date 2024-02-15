package io.github.srdjanv.endreforked.common.configs.mobs;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.reflect.TypeToken;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.common.configs.base.ResourceLocationWrapper;
import io.github.srdjanv.endreforked.common.configs.base.StaticServerSideConfig;
import io.github.srdjanv.endreforked.common.entity.EntityColdFireball;
import io.github.srdjanv.endreforked.common.entity.EntityEndGuard;
import io.github.srdjanv.endreforked.common.entity.EntityWatcher;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class MobConfig extends StaticServerSideConfig<Map<String, MobConfigSchema>> {

    private static MobConfig instance;

    public static MobConfig getInstance() {
        if (Objects.isNull(instance)) {
            instance = new MobConfig();
            EndReforked.LOGGER.debug("Instantiating {}", instance);
        }
        return instance;
    }

    @Override
    public void onDispose() {
        instance = null;
    }

    private final Map<String, Function<MobConfigSchema.Builder, MobConfigSchema>> defaultData = new Object2ObjectOpenHashMap<>();
    private final Map<String, Function<EntityConfig.Builder, EntityConfig>> entityConfigs = new Object2ObjectOpenHashMap<>();

    private MobConfig() {
        super("MobConfig", new TypeToken<Map<String, MobConfigSchema>>() {}.getType());
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        registerMob("endguard", builder -> {
            builder.registerSpawn(true).register(true);
            builder.biomeSpawn(ResourceLocationWrapper.of("sky"))
                    .defaultBiomeSpawnConfig(new MobConfigSchema.SpawnConfig(4, 3, 1));
            return builder.build();
        }, builder -> {
            builder.name("endguard").entityClass(EntityEndGuard.class).id(0)
                    .creatureType(EnumCreatureType.MONSTER)
                    .trackingRange(64)
                    .updateFrequency(3)
                    .sendsVelocityUpdates(false)
                    .registerEgg(true).eggPrimary(9654933).eggSecondary(11237052);
            return builder.build();
        });

        registerMob("end_cold_ball", builder -> {
            return builder.register(true).registerSpawn(false).build();
        }, builder -> {
            builder.name("end_cold_ball").entityClass(EntityColdFireball.class).id(1)
                    .registerEgg(false)
                    .trackingRange(30)
                    .updateFrequency(1)
                    .sendsVelocityUpdates(true);
            return builder.build();
        });

        registerMob("watcher", builder -> {
            builder.registerSpawn(true).register(true);
            builder.biomeSpawn(ResourceLocationWrapper.of("sky"),
                    new MobConfigSchema.SpawnConfig(10, 3, 1));

            builder.biomeSpawn(ResourceLocationWrapper.of("plains"),
                    new MobConfigSchema.SpawnConfig(4, 1, 1));

            return builder.build();
        }, builder -> {
            builder.name("watcher").entityClass(EntityWatcher.class).id(2)
                    .creatureType(EnumCreatureType.MONSTER)
                    .trackingRange(64)
                    .updateFrequency(3)
                    .sendsVelocityUpdates(false)
                    .registerEgg(true).eggPrimary(461076).eggSecondary(2236447);
            return builder.build();
        });

        registerMob("endlord", builder -> {
            return builder.registerSpawn(false).register(true).build();
        }, builder -> {
            builder.name("endlord").entityClass(EntityWatcher.class).id(3)
                    .creatureType(EnumCreatureType.MONSTER)
                    .trackingRange(64)
                    .updateFrequency(3)
                    .sendsVelocityUpdates(false)
                    .registerEgg(true).eggPrimary(461076).eggSecondary(681365);
            return builder.build();
        });

        registerMob("chronologist", builder -> {
            builder.registerSpawn(true).register(true);
            builder.biomeSpawn(ResourceLocationWrapper.of("sky"));
            builder.defaultBiomeSpawnConfig(new MobConfigSchema.SpawnConfig(4, 3, 1));
            return builder.build();
        }, builder -> {
            builder.name("chronologist").entityClass(EntityWatcher.class).id(4)
                    .creatureType(EnumCreatureType.MONSTER)
                    .trackingRange(64)
                    .updateFrequency(3)
                    .sendsVelocityUpdates(false)
                    .registerEgg(true).eggPrimary(461076).eggSecondary(13680725);
            return builder.build();
        });
    }

    private void registerMob(String name,
                             Function<MobConfigSchema.Builder, MobConfigSchema> worldGenConfig,
                             Function<EntityConfig.Builder, EntityConfig> configFunction) {
        defaultData.put(name, worldGenConfig);
        entityConfigs.put(name, configFunction);
    }

    @Override
    protected Map<String, MobConfigSchema> getDefaultData() {
        return defaultData.entrySet().stream().map(
                stringFunctionEntry -> Pair.of(stringFunctionEntry.getKey(),
                        stringFunctionEntry.getValue().apply(MobConfigSchema.builder())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    @Override
    protected Map<String, MobConfigSchema> getLoadedData() {
        return null;
    }

    @Override
    protected boolean validateLoadedData(Map<String, MobConfigSchema> newLoadedData) {
        return newLoadedData.size() == defaultData.size();
    }

    @Override
    protected Map<String, MobConfigSchema> dataMerger(Map<String, MobConfigSchema> defaultData,
                                                      Map<String, MobConfigSchema> loadedData,
                                                      Map<String, MobConfigSchema> existingData, boolean fistLoad) {
        if (!fistLoad) throw new IllegalStateException();
        Map<String, MobConfigSchema> data = new Object2ObjectOpenHashMap<>();
        data.putAll(defaultData);
        data.putAll(loadedData);
        return data;
    }

    @Override
    protected void dataLoaded(Map<String, MobConfigSchema> data) {
        for (var entry : data.entrySet()) {
            var name = entry.getKey();
            var configSchema = entry.getValue();
            if (configSchema.isRegister()) {
                var config = entityConfigs.get(name).apply(EntityConfig.builder());
                assert Objects.nonNull(config);
                config.init(configSchema);
            }
        }
    }
}
