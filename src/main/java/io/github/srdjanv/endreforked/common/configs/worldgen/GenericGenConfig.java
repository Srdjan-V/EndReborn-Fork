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
import io.github.srdjanv.endreforked.api.worldgen.features.BushSurfaceGenerator;
import io.github.srdjanv.endreforked.api.worldgen.features.RadiusSurfaceGenerator;
import io.github.srdjanv.endreforked.api.worldgen.features.SphereGenerator;
import io.github.srdjanv.endreforked.common.ModBlocks;
import io.github.srdjanv.endreforked.common.blocks.BlockEnderCrop;
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
        registerGen("Lormyte",
                builder -> {
                    builder.whiteListDim(1, DimConfig.builder()
                            .setRarity(80)
                            .setAmountModifier(12)
                            .setMaxHeight(45)
                            .setMinHeight(20).build());

                    return builder.build();
                },
                (world, biome, config) -> {
                    return new SphereGenerator(config, (server, rand, pos) -> {
                        var state = server.getBlockState(pos);
                        return state.getBlock() == Blocks.END_STONE;
                    }, (server, rand, pos) -> {
                        if (rand.nextInt(100) > 80) return false;
                        server.setBlockState(pos, ModBlocks.LORMYTE_CRYSTAL_BLOCK.get().getDefaultState());
                        return true;
                    });
                });

        registerGen("EndMagma",
                builder -> {
                    builder.whiteListDim(1, DimConfig.builder()
                            .setRarity(80)
                            .setAmountModifier(12)
                            .setMaxHeight(25)
                            .setMinHeight(10).build());
                    return builder.build();
                },
                (world, biome, config) -> {
                    return new SphereGenerator(config, (server, rand, pos) -> {
                        var state = server.getBlockState(pos);
                        return state.getBlock() == Blocks.END_STONE;
                    }, (server, rand, pos) -> {
                        if (rand.nextInt(100) > 80) return false;
                        server.setBlockState(pos, ModBlocks.END_MAGMA_BLOCK.get().getDefaultState());
                        return true;
                    });
                });

        registerGen("EntropyEndStone",
                builder -> {
                    builder.whiteListDim(1, DimConfig.builder()
                            .setRarity(60)
                            .setAmountModifier(30)
                            .setMaxHeight(30)
                            .setMinHeight(10).build());
                    return builder.build();
                },
                (world, biome, config) -> new WorldGenMinable(
                        ModBlocks.END_STONE_ENTROPY_BLOCK.get().getDefaultState(), config.amountModifier(),
                        BlockMatcher.forBlock(Blocks.END_STONE)));

        registerGen("EndMossPatch",
                builder -> {
                    builder.whiteListDim(1, DimConfig.builder()
                            .setRarity(5)
                            .setAmountModifier(4)
                            .setMaxHeight(90)
                            .setMinHeight(50).build());

                    return builder.build();
                },
                (world, biome, config) -> new RadiusSurfaceGenerator(config,
                        (server, rand, pos) -> server.getBlockState(pos).getBlock() == Blocks.END_STONE,
                        (server, rand, pos) -> {
                            if (rand.nextInt(100) > 80) return false;
                            if (server.isAirBlock(pos) && !server.isAirBlock(pos.up())) return false;
                            server.setBlockState(pos, ModBlocks.END_MOSS.get().getDefaultState());
                            if (rand.nextInt(100) > 80) return true;
                            server.setBlockState(pos.up(), ModBlocks.ORGANA_WEED.get().getDefaultState());
                            return true;
                        }));

        registerGen("EndCoral",
                builder -> {
                    builder.whiteListDim(1, DimConfig.builder()
                            .setRarity(0)
                            .setAmountModifier(20)
                            .setMaxHeight(90)
                            .setMinHeight(50).build());
                    return builder.build();
                },
                (world, biome, config) -> new BushSurfaceGenerator(config, ModBlocks.END_CORAL.get()));

        registerGen("EndFlower",
                builder -> {
                    builder.whiteListDim(1, DimConfig.builder()
                            .setRarity(5)
                            .setAmountModifier(6)
                            .setMaxHeight(90)
                            .setMinHeight(50).build());
                    return builder.build();
                },
                (world, biome, config) -> new BushSurfaceGenerator(config, ModBlocks.ENDER_FLOWER_CROP.get()) {

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
            instance.unregisterOreGenerator(genConfig -> genConfig.getGeneratorName().equals(name));
        });
    }
}
