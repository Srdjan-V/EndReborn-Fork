package io.github.srdjanv.endreforked.common.configs.worldgen;

import java.util.Objects;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import io.github.srdjanv.endreforked.api.worldgen.DimConfig;
import io.github.srdjanv.endreforked.api.worldgen.GenConfig;
import io.github.srdjanv.endreforked.api.worldgen.WorldGenHandler;
import io.github.srdjanv.endreforked.common.ModBlocks;
import io.github.srdjanv.endreforked.common.configs.base.ResourceLocationWrapper;
import io.github.srdjanv.endreforked.common.configs.worldgen.base.WorldGenBaseConfigReloadable;

public class GenericGenConfig extends WorldGenBaseConfigReloadable {

    private static GenericGenConfig instance;

    public static GenericGenConfig getInstance() {
        if (Objects.isNull(instance)) instance = new GenericGenConfig();
        return instance;
    }

    private GenericGenConfig() {
        super("GenericGenConfig");
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        registerGen("EssenceOre",
                worldGenConfiguration -> {
                    worldGenConfiguration.dimData.whiteList.put(0, DimConfig.builder()
                            .setRarity(80)
                            .setCount(20)
                            .setMaxHeight(256)
                            .setMinHeight(0).build());

                    worldGenConfiguration.dimData.whiteList.put(1, DimConfig.builder()
                            .setRarity(60)
                            .setCount(40)
                            .setMaxHeight(256)
                            .setMinHeight(0).build());
                    return worldGenConfiguration;
                },
                (world, biome, config) -> new WorldGenMinable(ModBlocks.ESSENCE_ORE.get().getDefaultState(),
                        config.count(), BlockMatcher.forBlock(Blocks.OBSIDIAN)));

        registerGen("TungstenOre",
                worldGenConfiguration -> {
                    worldGenConfiguration.dimData.whiteList.put(0, DimConfig.builder()
                            .setRarity(80)
                            .setCount(10)
                            .setMaxHeight(48)
                            .setMinHeight(0).build());
                    return worldGenConfiguration;
                },
                (world, biome, config) -> new WorldGenMinable(ModBlocks.TUNGSTEN_ORE.get().getDefaultState(),
                        config.count(), input -> {
                            if (input != null && input.getBlock() == Blocks.STONE) {
                                return input.getValue(BlockStone.VARIANT).equals(BlockStone.EnumType.DIORITE);
                            } else return false;
                        }));

        registerGen("TungstenEndOre",
                worldGenConfiguration -> {
                    worldGenConfiguration.defaultGenConfigForData = DimConfig.builder()
                            .setRarity(80)
                            .setCount(25)
                            .setMaxHeight(48)
                            .setMinHeight(0).build();

                    worldGenConfiguration.dimData.whiteListDefaultConfig.add(1);
                    worldGenConfiguration.biomeData.whiteListDefaultConfig
                            .add(ResourceLocationWrapper.of(new ResourceLocation("sky")));

                    return worldGenConfiguration;
                },
                ((world, biome, config) -> new WorldGenMinable(
                        ModBlocks.TUNGSTEN_END_ORE.get().getDefaultState(),
                        config.count(), BlockMatcher.forBlock(Blocks.END_STONE))));
    }

    @Override
    public void registerToHandler() {
        var instance = WorldGenHandler.getInstance();
        loadedDataData.forEach((name, worldGenConfiguration) -> {
            GenConfig genConfig = worldGenConfiguration.parseConfig(name, nameToGenerator.get(name));
            instance.registerOreGenerator(genConfig);
        });
    }

    @Override
    public void unRegisterFromHandler() {
        var instance = WorldGenHandler.getInstance();
        defaultData.keySet().forEach(name -> {
            instance.unregisterOreGenerator(genConfig -> genConfig.getGeneratorName().equals(name));
        });
    }
}
