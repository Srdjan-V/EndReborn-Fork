package io.github.srdjanv.endreforked.common.configs.worldgen;

import java.util.Objects;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import io.github.srdjanv.endreforked.api.worldgen.DimConfig;
import io.github.srdjanv.endreforked.api.worldgen.GenConfig;
import io.github.srdjanv.endreforked.api.worldgen.WorldGenHandler;
import io.github.srdjanv.endreforked.api.worldgen.features.SurfaceGenerator;
import io.github.srdjanv.endreforked.common.ModBlocks;
import io.github.srdjanv.endreforked.common.blocks.BlockEnderCrop;
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
        registerGen("Lormyte",
                worldGenConfiguration -> {
                    worldGenConfiguration.dimData.whiteList.put(1, DimConfig.builder()
                            .setRarity(80)
                            .setCount(120)
                            .setMaxHeight(55)
                            .setMinHeight(35).build());

                    return worldGenConfiguration;
                },
                (world, biome, config) -> new WorldGenMinable(
                        ModBlocks.LORMYTE_CRYSTAL_BLOCK.get().getDefaultState(),
                        config.count(), input -> input != null && input.getBlock() == Blocks.END_STONE));

        registerGen("EndMagma",
                worldGenConfiguration -> {
                    worldGenConfiguration.dimData.whiteList.put(1, DimConfig.builder()
                            .setRarity(80)
                            .setCount(50)
                            .setMaxHeight(22)
                            .setMinHeight(0).build());
                    return worldGenConfiguration;
                },
                (world, biome, config) -> new WorldGenMinable(ModBlocks.END_MAGMA_BLOCK.get().getDefaultState(),
                        config.count(), BlockMatcher.forBlock(Blocks.END_STONE)));

        registerGen("EntropyEndStone",
                worldGenConfiguration -> {
                    worldGenConfiguration.dimData.whiteList.put(1, DimConfig.builder()
                            .setRarity(60)
                            .setCount(30)
                            .setMaxHeight(22)
                            .setMinHeight(0).build());
                    return worldGenConfiguration;
                },
                (world, biome, config) -> new WorldGenMinable(
                        ModBlocks.END_STONE_ENTROPY_BLOCK.get().getDefaultState(), config.count(),
                        BlockMatcher.forBlock(Blocks.END_STONE)));

        registerGen("EndCoral",
                worldGenConfiguration -> {
                    worldGenConfiguration.weight = -10;
                    worldGenConfiguration.dimData.whiteList.put(1, DimConfig.builder()
                            .setRarity(0)
                            .setCount(20)
                            .setMaxHeight(90)
                            .setMinHeight(50).build());
                    return worldGenConfiguration;
                },
                (world, biome, config) -> new SurfaceGenerator(config, ModBlocks.END_CORAL.get()));

        registerGen("EndFlower",
                worldGenConfiguration -> {
                    worldGenConfiguration.weight = -10;
                    worldGenConfiguration.dimData.whiteList.put(1, DimConfig.builder()
                            .setRarity(5)
                            .setCount(6)
                            .setMaxHeight(90)
                            .setMinHeight(50).build());
                    return worldGenConfiguration;
                },
                (world, biome, config) -> new SurfaceGenerator(config, ModBlocks.ENDER_FLOWER_CROP.get()) {

                    @Override
                    public IBlockState getState(World world, Random random, BlockPos pos) {
                        return block.getDefaultState().withProperty(BlockEnderCrop.AGE,
                                random.nextInt(BlockEnderCrop.AGE.getAllowedValues().size() - 1));
                    }
                });
    }

    @Override
    public void registerToHandler() {
        var instance = WorldGenHandler.getInstance();
        loadedDataData.forEach((name, worldGenConfiguration) -> {
            GenConfig genConfig = worldGenConfiguration.parseConfig(name, nameToGenerator.get(name));
            instance.registerGenericGenerator(genConfig);
        });
    }

    @Override
    public void unRegisterFromHandler() {
        var instance = WorldGenHandler.getInstance();
        defaultData.keySet().forEach(name -> {
            instance.unregisterGenericGenerator(genConfig -> genConfig.getGeneratorName().equals(name));
        });
    }
}
