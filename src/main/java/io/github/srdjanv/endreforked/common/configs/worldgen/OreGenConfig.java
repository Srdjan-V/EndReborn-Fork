package io.github.srdjanv.endreforked.common.configs.worldgen;

import java.util.Objects;

import io.github.srdjanv.endreforked.common.configs.base.ResourceLocationWrapper;
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
import io.github.srdjanv.endreforked.common.configs.worldgen.base.WorldGenBaseConfigReloadable;

public class OreGenConfig extends WorldGenBaseConfigReloadable {

    private static OreGenConfig instance;

    public static OreGenConfig getInstance() {
        if (Objects.isNull(instance)) instance = new OreGenConfig();
        return instance;
    }

    private OreGenConfig() {
        super("OreGenConfig");
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        registerGen("EssenceOre",
                worldGenConfiguration -> {
                    worldGenConfiguration.weight = 200;
                    worldGenConfiguration.dimData.whiteList.put(0, DimConfig.builder()
                            .setRarity(80)
                            .setAmountModifier(20)
                            .setMaxHeight(256)
                            .setMinHeight(0).build());

                    worldGenConfiguration.dimData.whiteList.put(1, DimConfig.builder()
                            .setRarity(60)
                            .setAmountModifier(40)
                            .setMaxHeight(256)
                            .setMinHeight(0).build());
                    return worldGenConfiguration;
                },
                (world, biome, config) -> new WorldGenMinable(ModBlocks.ESSENCE_ORE.get().getDefaultState(),
                        config.amountModifier(), BlockMatcher.forBlock(Blocks.OBSIDIAN)));

        registerGen("TungstenOre",
                worldGenConfiguration -> {
                    worldGenConfiguration.weight = 180;
                    worldGenConfiguration.dimData.whiteList.put(0, DimConfig.builder()
                            .setRarity(80)
                            .setAmountModifier(10)
                            .setMaxHeight(48)
                            .setMinHeight(0).build());
                    return worldGenConfiguration;
                },
                (world, biome, config) -> new WorldGenMinable(ModBlocks.TUNGSTEN_ORE.get().getDefaultState(),
                        config.amountModifier(), input -> {
                    if (input != null && input.getBlock() == Blocks.STONE) {
                        return input.getValue(BlockStone.VARIANT).equals(BlockStone.EnumType.DIORITE);
                    } else return false;
                }));

        registerGen("TungstenEndOre",
                worldGenConfiguration -> {
                    worldGenConfiguration.weight = 160;
                    worldGenConfiguration.defaultGenConfigForData = DimConfig.builder()
                            .setRarity(80)
                            .setAmountModifier(25)
                            .setMaxHeight(48)
                            .setMinHeight(0).build();

                    worldGenConfiguration.dimData.whiteListDefaultConfig.add(1);
                    worldGenConfiguration.biomeData.whiteListDefaultConfig
                            .add(ResourceLocationWrapper.of(new ResourceLocation("sky")));

                    return worldGenConfiguration;
                },
                ((world, biome, config) -> new WorldGenMinable(
                        ModBlocks.TUNGSTEN_END_ORE.get().getDefaultState(),
                        config.amountModifier(), BlockMatcher.forBlock(Blocks.END_STONE))));

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
