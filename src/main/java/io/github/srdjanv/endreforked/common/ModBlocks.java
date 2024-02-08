package io.github.srdjanv.endreforked.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.google.common.base.Suppliers;

import io.github.srdjanv.endreforked.common.blocks.*;
import io.github.srdjanv.endreforked.common.blocks.base.BaseBlockBush;
import io.github.srdjanv.endreforked.common.blocks.base.BlockEndBase;
import io.github.srdjanv.endreforked.common.blocks.base.BlockStairsBase;
import io.github.srdjanv.endreforked.common.blocks.base.BlockWallBase;
import io.github.srdjanv.endreforked.utils.models.IHasModel;

@SuppressWarnings("unused")
public final class ModBlocks {

    private static final List<Supplier<? extends Block>> BLOCKS = new ArrayList<>();

    public static final Supplier<Block> END_FIRE = register(
            () -> new BlockColdFire("end_fire"));

    public static final Supplier<Block> END_STONE_SMOOTH_BLOCK = register(
            () -> new BlockEndBase("end_stone_smooth_block", Material.ROCK));
    public static final Supplier<Block> END_STONE_SMOOTH_STAIRS = register(
            () -> new BlockStairsBase("end_stone_smooth_stairs", ModBlocks.END_STONE_SMOOTH_BLOCK.get()));
    public static final Supplier<Block> END_STONE_SMOOTH_WALL = register(
            () -> new BlockWallBase("end_stone_smooth_wall", ModBlocks.END_STONE_SMOOTH_BLOCK.get()));

    public static final Supplier<Block> END_STONE_PILLAR = register(
            () -> new BlockPillar("end_stone_pillar", Material.ROCK));
    public static final Supplier<Block> END_BRICKS_CHISELED = register(
            () -> new BlockEndBase("end_bricks_chiseled", Material.ROCK));

    public static final Supplier<Block> END_BRICKS_STAIRS = register(
            () -> new BlockStairsBase("end_bricks_stairs", net.minecraft.init.Blocks.END_BRICKS));
    public static final Supplier<Block> END_BRICKS_WALL = register(
            () -> new BlockWallBase("end_bricks_wall", net.minecraft.init.Blocks.END_BRICKS));

    public static final Supplier<Block> PURPUR_WALL = register(
            () -> new BlockWallBase("purpur_wall", net.minecraft.init.Blocks.PURPUR_BLOCK));
    public static final Supplier<Block> PURPUR_LAMP = register(
            () -> new BlockPurpurLamp("purpur_lamp_block"));

    public static final Supplier<Block> ESSENCE_ORE = register(
            () -> new BlockEssenceOre("essence_ore", Material.ROCK));
    public static final Supplier<Block> PHANTOM_BLOCK = register(
            () -> new BlockPhantom("phantom_block", Material.BARRIER, false));

    public static final Supplier<Block> END_STONE_ENTROPY_BLOCK = register(
            () -> new BlockEndBase("end_stone_entropy_block", Material.ROCK));

    public static final Supplier<BaseBlockBush> END_CORAL = register(
            () -> new BlockEndCoral("end_coral", Material.VINE));

    public static final Supplier<Block> END_MAGMA_BLOCK = register(
            () -> new BlockEndMagma("end_magma_block"));

    public static final Supplier<Block> LORMYTE_CRYSTAL_BLOCK = register(
            () -> new BlockEndBase("lormyte_crystal_block", Material.ROCK));
    public static final Supplier<Block> DECORATIVE_LORMYTE_BLOCK = register(
            () -> new BlockEndBase("decorative_lormyte_block", Material.ROCK));

    public static final Supplier<Block> ENDORIUM_BLOCK = register(
            () -> new BlockEndBase("endorium_block", Material.IRON));

    // tungsten
    public static final Supplier<Block> TUNGSTEN_BLOCK = register(
            () -> new BlockEndBase("tungsten_block", Material.IRON));
    public static final Supplier<Block> TUNGSTEN_ORE = register(
            () -> new TungstenOre("tungsten_ore", Material.ROCK));

    public static final Supplier<Block> TUNGSTEN_END_ORE = register(
            () -> new TungstenOre("tungsten_end_ore", Material.ROCK));

    // tiles
    public static final Supplier<Block> BLOCK_END_FORGE = register(
            () -> new BlockEndForge("end_forge_block"));
    public static final Supplier<Block> MATERIALIZER_BLOCK = register(
            () -> new BlockMaterializer("materializer_block"));

    public static final Supplier<Block> RUNE_BLOCK = register(
            () -> new BlockRune("rune_block", Material.ROCK));

    public static final Supplier<Block> DRAGONITE_CROP = register(
            () -> new CropDragonite("dragonite_crop"));
    public static final Supplier<Block> XORCITE_BLOCK = register(
            () -> new BlockXorciteCrop("xorcite_block"));

    public static final Supplier<BlockEnderCrop> ENDER_FLOWER_CROP = register(
            () -> new BlockEnderCrop("ender_flower_crop"));

    public static <B extends Block> Supplier<B> register(com.google.common.base.Supplier<B> supplier) {
        Supplier<B> memorized = Suppliers.memoize(supplier);
        BLOCKS.add(memorized);
        return memorized;
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        var registry = event.getRegistry();
        BLOCKS.stream().map(Supplier::get).filter(Objects::nonNull)
                .forEach(registry::register);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        BLOCKS.stream().map(Supplier::get)
                .filter(Objects::nonNull)
                .filter(item -> item instanceof IHasModel)
                .map(item -> (IHasModel) item)
                .forEach(IHasModel::registerModels);
    }
}