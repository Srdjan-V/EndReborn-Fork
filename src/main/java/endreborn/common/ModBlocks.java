package endreborn.common;

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

import endreborn.common.blocks.*;
import endreborn.utils.IHasModel;

@SuppressWarnings("unused")
public final class ModBlocks {

    private static final List<Supplier<? extends Block>> BLOCKS = new ArrayList<>();

    public static final Supplier<Block> BLOCK_ENDORIUM = register(
            () -> new BlockEndBase("block_endorium", Material.IRON));
    public static final Supplier<Block> END_STONE_SMOOTH = register(
            () -> new BlockEndBase("block_end_stone_smooth", Material.ROCK));
    public static final Supplier<Block> END_STONE_PILLAR = register(
            () -> new BlockPillar("block_end_stone_pillar", Material.ROCK));
    public static final Supplier<Block> PURPUR_LAMP = register(() -> new LampBase("block_purpur_lamp", Material.ROCK));
    public static final Supplier<Block> ENDER_FLOWER = register(() -> new EnderCropBase("crop_ender_flower"));
    public static final Supplier<Block> DRAGON_BUSH = register(() -> new DragonBush("crop_dragonite"));
    public static final Supplier<Block> ESSENCE_ORE = register(
            () -> new BlockEssenceOre("block_essence_ore", Material.ROCK));
    public static final Supplier<Block> PHANTOM_BLOCK = register(
            () -> new BlockPhantom("block_phantom", Material.BARRIER, false));
    public static final Supplier<Block> ENTROPY_END_STONE = register(
            () -> new BlockEndBase("block_entropy_end_stone", Material.ROCK));
    public static final Supplier<Block> LORMYTE_CRYSTAL = register(
            () -> new BlockEndBase("block_lormyte_crystal", Material.ROCK));
    public static final Supplier<Block> DECORATIVE_LORMYTE = register(
            () -> new BlockEndBase("block_decorative_lormyte", Material.ROCK));
    public static final Supplier<Block> TUNGSTEN_BLOCK = register(
            () -> new BlockEndBase("tungsten_block", Material.IRON));
    public static final Supplier<Block> TUNGSTEN_ORE = register(() -> new TungstenOre("tungsten_ore", Material.ROCK));
    public static final Supplier<Block> BLOCK_RUNE = register(() -> new BlockRune("block_rune", Material.ROCK));
    public static final Supplier<Block> BLOCK_END_MAGMA = register(() -> new BlockEndMagma("block_end_magma"));
    public static final Supplier<Block> BLOCK_END_FORGE = register(() -> new BlockEndForge("block_end_forge"));
    public static final Supplier<Block> XORCITE_BLOCK = register(() -> new XorcitePlantBlock("xorcite_block"));
    public static final Supplier<Block> BLOCK_E_USER = register(() -> new BlockEntropyUser("entropy_user"));
    public static final Supplier<Block> BROKEN_FLOWER = register(
            () -> new CutoutBlock("broken_ender_flower", Material.PLANTS));
    public static final Supplier<Block> END_STONE_CHISELED = register(
            () -> new BlockEndBase("chiseled_end_bricks", Material.ROCK));
    public static final Supplier<Block> COLD_FIRE = register(() -> new BlockColdFire("end_fire"));

    public static final Supplier<Block> STAIRS_END_BRICKS = register(
            () -> new BlockStairsBase("e_end_bricks_stairs", net.minecraft.init.Blocks.END_BRICKS));
    public static final Supplier<Block> STAIRS_SMOOTH_END_STONE = register(
            () -> new BlockStairsBase("smooth_end_stone_stairs", ModBlocks.END_STONE_SMOOTH.get()));
    public static final Supplier<Block> WALL_END_BRICKS = register(
            () -> new BlockWallBase("e_end_bricks_wall", net.minecraft.init.Blocks.END_BRICKS));
    public static final Supplier<Block> WALL_PURPUR = register(
            () -> new BlockWallBase("e_purpur_wall", net.minecraft.init.Blocks.PURPUR_BLOCK));
    public static final Supplier<Block> WALL_SMOOTH_END_STONE = register(
            () -> new BlockWallBase("smooth_end_stone_wall", ModBlocks.END_STONE_SMOOTH.get()));

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
