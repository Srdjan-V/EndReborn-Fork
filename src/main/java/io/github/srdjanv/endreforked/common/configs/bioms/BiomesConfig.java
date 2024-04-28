package io.github.srdjanv.endreforked.common.configs.bioms;


import com.google.gson.reflect.TypeToken;
import git.jbredwards.nether_api.api.event.NetherAPIRegistryEvent;
import git.jbredwards.nether_api.api.registry.INetherAPIRegistry;
import git.jbredwards.nether_api.mod.common.config.NetherAPIConfig;
import git.jbredwards.nether_api.mod.common.registry.NetherAPIRegistry;
import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.common.ModBioms;
import io.github.srdjanv.endreforked.common.configs.base.ReloadableServerSideConfig;
import io.github.srdjanv.endreforked.common.configs.worldgen.schema.WorldGenSchema;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BiomesConfig extends ReloadableServerSideConfig<Map<String, BiomesSchema>> {
    private static BiomesConfig instance;

    public static BiomesConfig getInstance() {
        if (Objects.isNull(instance)) {
            instance = new BiomesConfig();
            EndReforked.LOGGER.debug("Instantiating {}", instance);
        }
        return instance;
    }

    private final Map<String, Function<BiomesSchema.Builder, BiomesSchema>> defaultData = new Object2ObjectOpenHashMap<>();
    private final Map<String, Pair<INetherAPIRegistry, Supplier<? extends Biome>>> name2Data = new Object2ObjectOpenHashMap<>();
    private Map<String, BiomesSchema> loadedDataData;

    private BiomesConfig() {
        super("BiomesConfig", new TypeToken<Map<String, BiomesSchema>>() {}.getType());
    }

    @Override public void registerEventBus() {
        registerThisToEventBus();
    }

    @Override public void preInit(FMLPreInitializationEvent event) {
        registerBiome("Organa",
                NetherAPIRegistry.THE_END,
                ModBioms.ORGANA_BIOME,
                builder -> builder.weight(1000).build());
    }

    private void registerBiome(String name,
                               INetherAPIRegistry api,
                               Supplier<? extends Biome> biome,
                               Function<BiomesSchema.Builder, BiomesSchema> function) {
        name2Data.put(name, Pair.of(api, biome));
        defaultData.put(name, function);
    }

    @Override protected Map<String, BiomesSchema> getDefaultData() {
        return defaultData.entrySet()
                .stream()
                .map(entry-> Pair.of(entry.getKey(),
                        entry.getValue().apply(BiomesSchema.builder().enabled(true))))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    @Override protected @Nullable Map<String, BiomesSchema> getLoadedData() {
        return loadedDataData;
    }

    @Override
    protected void dataChanged(Map<String, BiomesSchema> newData, boolean fistLoad) {
        loadedDataData = newData;
        if (fistLoad) {
            register();
        } else {
            unRegister();
            register();
        }
    }

    @Override protected Map<String, BiomesSchema> dataMerger(
            Map<String, BiomesSchema> defaultData,
            Map<String, BiomesSchema> loadedData,
            Map<String, BiomesSchema> existingData,
            boolean fistLoad) {
        if (fistLoad) {
            Map<String, BiomesSchema> data = new Object2ObjectOpenHashMap<>();
            for (var entry : defaultData.entrySet()) {
                BiomesSchema biomesSchema = loadedData.get(entry.getKey());
                if (Objects.isNull(biomesSchema)) biomesSchema = entry.getValue();
                data.put(entry.getKey(), biomesSchema);
            }

            return data;
        }

        Map<String, BiomesSchema> data = new Object2ObjectOpenHashMap<>();
        for (var entry : defaultData.entrySet()) {
            BiomesSchema biomesSchema = loadedData.get(entry.getKey());
            if (Objects.isNull(biomesSchema)) biomesSchema = existingData.get(entry.getKey());
            if (Objects.isNull(biomesSchema)) biomesSchema = entry.getValue();
            data.put(entry.getKey(), biomesSchema);
        }
        return data;
    }

    //todo look into making this not subscribe to all events
    @SubscribeEvent
    public void registerNetherAPI(@Nonnull NetherAPIRegistryEvent.End event) {
        handle(event.registry);
    }

    @SubscribeEvent
    public void registerNetherAPI(@Nonnull NetherAPIRegistryEvent.Nether event) {
        handle(event.registry);
    }


    private void handle(INetherAPIRegistry registry) {
        if (loadedDataData == null) return;
        loadedDataData.forEach((name, biomesSchema) -> {
            if (!biomesSchema.isEnabled()) return;
            var data = name2Data.get(name);
            data.getLeft().registerBiome(data.getRight().get(), biomesSchema.getWeight());
        });
    }

    private void register() {
        loadedDataData.forEach((name, biomesSchema) -> {
            if (!biomesSchema.isEnabled()) return;
            var data = name2Data.get(name);
            data.getLeft().registerBiome(data.getRight().get(), biomesSchema.getWeight());
        });
    }

    private void unRegister() {
        loadedDataData.forEach((name, biomesSchema) -> {
            var data = name2Data.get(name);
            data.getLeft().removeBiome(data.getRight().get());
        });
    }
}
