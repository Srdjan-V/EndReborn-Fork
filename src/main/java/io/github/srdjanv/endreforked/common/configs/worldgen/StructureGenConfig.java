package io.github.srdjanv.endreforked.common.configs.worldgen;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import io.github.srdjanv.endreforked.Tags;
import io.github.srdjanv.endreforked.api.worldgen.DimConfig;
import io.github.srdjanv.endreforked.api.worldgen.GenConfig;
import io.github.srdjanv.endreforked.api.worldgen.WorldGenHandler;
import io.github.srdjanv.endreforked.api.worldgen.base.Locators;
import io.github.srdjanv.endreforked.api.worldgen.features.WorldGenStructure;
import io.github.srdjanv.endreforked.common.configs.base.ResourceLocationWrapper;
import io.github.srdjanv.endreforked.common.configs.worldgen.base.WorldGenBaseConfigReloadable;

public class StructureGenConfig extends WorldGenBaseConfigReloadable {

    private static StructureGenConfig instance;

    public static StructureGenConfig getInstance() {
        if (Objects.isNull(instance)) instance = new StructureGenConfig();
        return instance;
    }

    public StructureGenConfig() {
        super("StructureGenConfig");
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        registerGen("EndRuins",
                worldGenConfiguration -> {
                    worldGenConfiguration.biomeData.whiteListDefaultConfig
                            .add(ResourceLocationWrapper.of(new ResourceLocation("sky")));
                    worldGenConfiguration.defaultGenConfigForData = DimConfig.builder()
                            .setRarity(600)
                            .setCount(1)
                            .setMaxHeight(100)
                            .setMinHeight(85).build();
                    return worldGenConfiguration;
                },
                (world, biome, config) -> new WorldGenStructure(Locators.OFFSET_16, config, "end_ruins"));

        registerGen("EndIslands",
                worldGenConfiguration -> {
                    var data = Stream.of("sky", "plains", "desert", "ocean", "deep_ocean", "savanna")
                            .map(ResourceLocation::new)
                            .map(ResourceLocationWrapper::of).collect(Collectors.toList());

                    worldGenConfiguration.biomeData.whiteListDefaultConfig.addAll(data);

                    worldGenConfiguration.defaultGenConfigForData = DimConfig.builder()
                            .setRarity(600)
                            .setCount(1)
                            .setMaxHeight(115)
                            .setMinHeight(90).build();
                    return worldGenConfiguration;
                },
                ((world, biome, config) -> new WorldGenStructure(Locators.OFFSET_16, config, "end_island")));

        registerGen("Observatory",
                worldGenConfiguration -> {
                    var data = Stream.of("desert", "ocean", "deep_ocean", "forest", "birch_forest", "swampland")
                            .map(ResourceLocation::new)
                            .map(ResourceLocationWrapper::of).collect(Collectors.toList());

                    worldGenConfiguration.biomeData.whiteListDefaultConfig.addAll(data);
                    worldGenConfiguration.defaultGenConfigForData = DimConfig.builder()
                            .setRarity(600)
                            .setCount(1)
                            .setMaxHeight(3)
                            .setMinHeight(3).build();

                    return worldGenConfiguration;
                },
                (world, biome, config) -> new WorldGenStructure(Locators.OFFSET_16, config, "observ"));

        registerGen("ShipWreck",
                worldGenConfiguration -> {
                    worldGenConfiguration.biomeData.whiteListDefaultConfig
                            .add(ResourceLocationWrapper.of(new ResourceLocation("sky")));
                    worldGenConfiguration.defaultGenConfigForData = DimConfig.builder()
                            .setRarity(600)
                            .setCount(1)
                            .setMaxHeight(70)
                            .setMinHeight(50).build();

                    return worldGenConfiguration;
                },
                ((world, biome, config) -> new WorldGenStructure(
                        Locators.OFFSET_16.andThenLocate(Locators.SURFACE_BLOCK).andThenMove(pos -> pos.add(0, -2, 2)),
                        config, new ResourceLocation(Tags.MODID, "end_shipwreck"), WorldGenStructure.defaultSettings)));
    }

    @Override
    public void registerToHandler() {
        var instance = WorldGenHandler.getInstance();
        loadedDataData.forEach((name, worldGenConfiguration) -> {
            GenConfig genConfig = worldGenConfiguration.parseConfig(name, nameToGenerator.get(name));
            instance.registerStructureGenerator(genConfig);
        });
    }

    @Override
    public void unRegisterFromHandler() {
        var instance = WorldGenHandler.getInstance();
        defaultData.keySet().forEach(name -> {
            instance.unregisterStructureGenerator(genConfig -> genConfig.getGeneratorName().equals(name));
        });
    }
}
