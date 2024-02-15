package io.github.srdjanv.endreforked.common;

import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.google.common.base.Suppliers;

import io.github.srdjanv.endreforked.Tags;
import io.github.srdjanv.endreforked.client.armor.ModelDArmor;
import io.github.srdjanv.endreforked.client.armor.ModelEArmor;
import io.github.srdjanv.endreforked.common.configs.content.DisabledContentConfig;
import io.github.srdjanv.endreforked.common.items.*;
import io.github.srdjanv.endreforked.common.items.base.ArmourBase;
import io.github.srdjanv.endreforked.common.items.base.CustomModelArmor;
import io.github.srdjanv.endreforked.common.items.base.ItemBase;
import io.github.srdjanv.endreforked.common.items.base.ItemLegendary;
import io.github.srdjanv.endreforked.common.items.food.DragoniteBerries;
import io.github.srdjanv.endreforked.common.items.food.FoodChorusSoup;
import io.github.srdjanv.endreforked.common.items.food.FoodEnderFlesh;
import io.github.srdjanv.endreforked.common.items.tools.*;
import io.github.srdjanv.endreforked.utils.models.IHasModel;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public final class ModItems {

    private static final List<Supplier<? extends Item>> ITEMS = new ObjectArrayList<>();

    // Materials
    public static final Supplier<ToolMaterial> TOOL_ENDORIUM = Suppliers.memoize(() -> {
        return EnumHelper.addToolMaterial("endorium", 4, 1024, 6.5F, 4.0F,
                13);
    });
    public static final Supplier<ToolMaterial> TUNGSTEN = Suppliers.memoize(() -> {
        return EnumHelper.addToolMaterial("tungsten", 3, 512, 5.5F, 2.5F,
                11);
    });
    public static final Supplier<ToolMaterial> TOOL_MAGNIFIER = Suppliers
            .memoize(() -> EnumHelper.addToolMaterial("magnifier", 4, 256, 5.5F, 1.0F,
                    13));
    public static final Supplier<ToolMaterial> TOOL_END = Suppliers
            .memoize(() -> EnumHelper.addToolMaterial("tool_end", 5, 1024, 6.5F, 9.0F, 14));

    public static final Supplier<ArmorMaterial> ARMOUR_OBSIDIAN = Suppliers
            .memoize(() -> EnumHelper.addArmorMaterial("obsidian",
                    Tags.MOD_PREFIX + "obsidian", 33, new int[] { 4, 7, 8, 4 }, 10,
                    SoundEvents.ITEM_ARMOR_EQUIP_IRON, 3.0F));
    public static final Supplier<ArmorMaterial> ARMOUR_DRAGON = Suppliers
            .memoize(() -> EnumHelper.addArmorMaterial("dragon",
                    Tags.MOD_PREFIX + "dragon", 44, new int[] { 6, 9, 10, 6 }, 10,
                    SoundEvents.ITEM_ARMOR_EQUIP_IRON, 5.0F));

    // Items
    public static final Supplier<Item> INGOT_ENDORIUM = registerDisableable(() -> new ItemBase("item_ingot_endorium"));
    public static final Supplier<Item> SHARD_OBSIDIAN = registerDisableable(() -> new ItemBase("item_shard_obsidian"));
    public static final Supplier<Item> RAW_ENDORIUM = registerDisableable(() -> new ItemBase("item_raw_endorium"));
    // TODO: 08/11/2023 implement fixer old:item_dragonite_seeds
    public static final Supplier<Item> DRAGONITE_SEEDS = registerDisableable(
            () -> new ItemDragoniteSeeds("dragonite_seeds"));
    public static final Supplier<Item> ADVANCED_PEARL = registerDisableable(
            () -> new ItemAdvancedEnderPearl("item_advanced_ender_pearl"));
    public static final Supplier<Item> END_ESSENCE = registerDisableable(() -> new ItemBase("item_end_essence"));
    public static final Supplier<Item> END_SHARD = registerDisableable(() -> new ItemBase("item_end_shard"));
    public static final Supplier<Item> END_RUNE = registerDisableable(() -> new ItemLegendary("item_end_rune"));
    public static final Supplier<Item> ITEM_LORMYTE_CRYSTAL = registerDisableable(
            () -> new ItemBase("item_lormyte_crystal"));
    public static final Supplier<Item> TUNGSTEN_INGOT = registerDisableable(() -> new ItemBase("tungsten_ingot"));
    public static final Supplier<Item> ENDER_STRING = registerDisableable(() -> new ItemLegendary("item_ender_string"));
    public static final Supplier<Item> WORLD_MIRROR = registerDisableable(
            () -> new ItemWorldMirror("item_world_mirror"));
    public static final Supplier<Item> DRAGONITE_TEA = registerDisableable(
            () -> new ItemDragoniteTea("item_dragonite_tea"));
    public static final Supplier<Item> ANGEL_FEATHER = registerDisableable(() -> new ItemBase("item_angel_feather"));
    public static final Supplier<Item> DRAGON_SCALES = registerDisableable(() -> new ItemBase("dragon_scales"));
    public static final Supplier<Item> XORCITE_SHARD = registerDisableable(() -> new ItemBase("xorcite_shard"));
    public static final Supplier<Item> INFUSED_METALL = registerDisableable(() -> new ItemBase("ingot_infused"));
    public static final Supplier<Item> SWORD_SHARD = registerDisableable(() -> new ItemBase("sword_shard"));
    public static final Supplier<Item> CATALYST = registerDisableable(() -> new ItemCatalyst("catalyst"));
    public static final Supplier<Item> RECORD = registerDisableable(
            () -> new ItemEndRecord("end_record", ModSounds.THE_VOID));
    public static final Supplier<Item> TUNGSTEN_NUGGET = registerDisableable(() -> new ItemBase("tungsten_nugget"));

    // public static final XorcitePlantBlockItem plantItem = new XorcitePlantBlockItem(ModBlocks.DRAGON_ESSENCE);

    // Tools
    public static final Supplier<Item> PICKAXE_ENDORIUM = registerDisableable(() -> {
        return new ToolPickaxe("tool_pickaxe_endorium", TOOL_ENDORIUM.get());
    });
    public static final Supplier<Item> SWORD_ENDORIUM = registerDisableable(() -> {
        return new ToolSword("tool_sword_endorium", TOOL_ENDORIUM.get());
    });
    public static final Supplier<Item> HOE_ENDORIUM = registerDisableable(() -> {
        return new ToolHoe("tool_hoe_endorium", TOOL_ENDORIUM.get());
    });
    public static final Supplier<Item> AXE_ENDORIUM = registerDisableable(() -> {
        return new ToolAxe("tool_axe_endorium", TOOL_ENDORIUM.get());
    });
    public static final Supplier<Item> SHOVEL_ENDORIUM = registerDisableable(() -> {
        return new ToolShovel("tool_shovel_endorium", TOOL_ENDORIUM.get());
    });
    public static final Supplier<Item> PICKAXE_WOLFRAMIUM = registerDisableable(() -> {
        return new ToolPickaxe("tool_pickaxe_wolframium", TUNGSTEN.get());
    });
    public static final Supplier<Item> SWORD_WOLFRAMIUM = registerDisableable(() -> {
        return new ToolSword("tool_sword_wolframium", TUNGSTEN.get());
    });
    public static final Supplier<Item> HOE_WOLFRAMIUM = registerDisableable(() -> {
        return new ToolHoe("tool_hoe_wolframium", TUNGSTEN.get());
    });
    public static final Supplier<Item> AXE_WOLFRAMIUM = registerDisableable(() -> {
        return new ToolAxe("tool_axe_wolframium", TUNGSTEN.get());
    });
    public static final Supplier<Item> SHOVEL_WOLFRAMIUM = registerDisableable(() -> {
        return new ToolShovel("tool_shovel_wolframium", TUNGSTEN.get());
    });
    public static final Supplier<Item> ENTROPY_WAND = registerDisableable(
            () -> new ToolEntropyWand("entropy_wand", TOOL_MAGNIFIER.get()));
    public static final Supplier<Item> HAMMER_IRON = registerDisableable(() -> new ItemHammer("tool_hammer_iron"));
    public static final Supplier<Item> ENDER_BOW = registerDisableable(() -> new ItemEnderBow("ender_bow"));
    public static final Supplier<Item> ENDER_SWORD = registerDisableable(
            () -> new ItemEnderSword("ender_sword", TOOL_END.get()));
    public static final Supplier<Item> ENDER_HOOK = registerDisableable(
            () -> new ItemDeather("tool_magnifier", TOOL_MAGNIFIER.get()));

    // Armors
    public static final Supplier<Item> CHESTPLATE_OBSIDIAN = registerDisableable(() -> {
        return new ArmourBase("armour_chestplate_obsidian", ARMOUR_OBSIDIAN.get(), 1,
                EntityEquipmentSlot.CHEST);
    });
    public static final Supplier<Item> LEGGINGS_OBSIDIAN = registerDisableable(() -> {
        return new ArmourBase("armour_leggings_obsidian", ARMOUR_OBSIDIAN.get(), 2,
                EntityEquipmentSlot.LEGS);
    });
    public static final Supplier<Item> BOOTS_OBSIDIAN = registerDisableable(() -> {
        return new ArmourBase("armour_boots_obsidian", ARMOUR_OBSIDIAN.get(), 1,
                EntityEquipmentSlot.FEET);
    });
    public static final Supplier<Item> HELMET_OBSIDIAN = registerDisableable(() -> {
        return new CustomModelArmor("armour_helmet_helmet", ARMOUR_OBSIDIAN.get(), 1,
                EntityEquipmentSlot.HEAD, ModelEArmor::new);
    });
    public static final Supplier<Item> CHESTPLATE_DRAGON = registerDisableable(() -> {
        return new ArmourBase("armour_chestplate_dragon", ARMOUR_DRAGON.get(), 1,
                EntityEquipmentSlot.CHEST);
    });
    public static final Supplier<Item> LEGGINGS_DRAGON = registerDisableable(() -> {
        return new ArmourBase("armour_leggings_dragon", ARMOUR_DRAGON.get(), 2,
                EntityEquipmentSlot.LEGS);
    });
    public static final Supplier<Item> BOOTS_DRAGON = registerDisableable(() -> {
        return new ArmourBase("armour_boots_dragon", ARMOUR_DRAGON.get(), 1,
                EntityEquipmentSlot.FEET);
    });
    public static final Supplier<Item> HELMET_DRAGON = registerDisableable(() -> {
        return new CustomModelArmor("armour_helmet_dragon", ARMOUR_DRAGON.get(), 1,
                EntityEquipmentSlot.HEAD, ModelDArmor::new);
    });

    // Food
    public static final Supplier<Item> ENDER_FLESH = registerDisableable(() -> new FoodEnderFlesh("ender_flesh"));// TODO:
    // 04/11/2023 add
    // fixer
    // TODO: 08/11/2023 implement fixer old:food_dragonite_berries
    public static final Supplier<Item> DRAGONITE_BERRIES = registerDisableable(
            () -> new DragoniteBerries("dragonite_berries"));
    public static final Supplier<Item> CHORUS_SOUP = registerDisableable(
            () -> new FoodChorusSoup(5, "food_chorus_soup"));

    // TODO: 22/10/2023 rename itemBlocks
    public static final Supplier<ItemBlock> BLOCK_ENDORIUM = registerItemBlock(ModBlocks.ENDORIUM_BLOCK);
    public static final Supplier<ItemBlock> END_STONE_SMOOTH = registerItemBlock(ModBlocks.END_STONE_SMOOTH_BLOCK);
    public static final Supplier<ItemBlock> END_STONE_PILLAR = registerItemBlock(ModBlocks.END_STONE_PILLAR);
    public static final Supplier<ItemBlock> PURPUR_LAMP = registerItemBlock(ModBlocks.PURPUR_LAMP);
    public static final Supplier<ItemBlock> ENDER_FLOWER_CROP = registerItemBlock(ModBlocks.ENDER_FLOWER_CROP);
    public static final Supplier<ItemBlock> DRAGON_BUSH = registerItemBlock(ModBlocks.DRAGONITE_CROP);
    public static final Supplier<ItemBlock> ESSENCE_ORE = registerItemBlock(ModBlocks.ESSENCE_ORE);
    public static final Supplier<ItemBlock> PHANTOM_BLOCK = registerItemBlock(ModBlocks.PHANTOM_BLOCK);
    public static final Supplier<ItemBlock> ENTROPY_END_STONE = registerItemBlock(ModBlocks.END_STONE_ENTROPY_BLOCK);
    public static final Supplier<ItemBlock> LORMYTE_CRYSTAL = registerItemBlock(ModBlocks.LORMYTE_CRYSTAL_BLOCK);
    public static final Supplier<ItemBlock> DECORATIVE_LORMYTE = registerItemBlock(ModBlocks.DECORATIVE_LORMYTE_BLOCK);
    public static final Supplier<ItemBlock> TUNGSTEN_BLOCK = registerItemBlock(ModBlocks.TUNGSTEN_BLOCK);
    public static final Supplier<ItemBlock> TUNGSTEN_ORE = registerItemBlock(ModBlocks.TUNGSTEN_ORE);
    public static final Supplier<ItemBlock> TUNGSTEN_END_ORE = registerItemBlock(ModBlocks.TUNGSTEN_END_ORE);
    public static final Supplier<ItemBlock> BLOCK_RUNE = registerItemBlock(ModBlocks.RUNE_BLOCK);
    public static final Supplier<ItemBlock> BLOCK_END_MAGMA = registerItemBlock(ModBlocks.END_MAGMA_BLOCK);
    public static final Supplier<ItemBlock> BLOCK_END_FORGE = registerItemBlock(ModBlocks.BLOCK_END_FORGE);
    public static final Supplier<ItemBlock> XORCITE_BLOCK = registerItemBlock(ModBlocks.XORCITE_BLOCK);
    public static final Supplier<ItemBlock> BLOCK_E_USER = registerItemBlock(ModBlocks.MATERIALIZER_BLOCK);
    public static final Supplier<ItemBlock> END_STONE_CHISELED = registerItemBlock(ModBlocks.END_BRICKS_CHISELED);
    public static final Supplier<ItemBlock> COLD_FIRE = registerItemBlock(ModBlocks.END_FIRE);
    public static final Supplier<ItemBlock> STAIRS_END_BRICKS = registerItemBlock(ModBlocks.END_BRICKS_STAIRS);
    public static final Supplier<ItemBlock> STAIRS_SMOOTH_END_STONE = registerItemBlock(
            ModBlocks.END_STONE_SMOOTH_STAIRS);
    public static final Supplier<ItemBlock> WALL_END_BRICKS = registerItemBlock(ModBlocks.END_BRICKS_WALL);
    public static final Supplier<ItemBlock> WALL_PURPUR = registerItemBlock(ModBlocks.PURPUR_WALL);
    public static final Supplier<ItemBlock> WALL_SMOOTH_END_STONE = registerItemBlock(ModBlocks.END_STONE_SMOOTH_WALL);
    // TODO: 20/11/2023 add worldgen
    public static final Supplier<ItemBlock> END_CORAL = registerItemBlock(ModBlocks.END_CORAL);

    public static <I extends Item> Supplier<I> register(final com.google.common.base.Supplier<I> supplier) {
        var memorized = Suppliers.memoize(supplier);
        ITEMS.add(memorized);
        return memorized;
    }

    public static <I extends Item> Supplier<I> register(final BooleanSupplier enableCondition,
                                                        final com.google.common.base.Supplier<I> supplier) {
        var memorized = Suppliers.memoize(() -> {
            if (enableCondition.getAsBoolean()) return supplier.get();
            return null;
        });
        ITEMS.add(memorized);
        return memorized;
    }

    public static <I extends Item> Supplier<I> registerDisableable(final com.google.common.base.Supplier<I> supplier) {
        var memorized = Suppliers.memoize(() -> {
            var item = supplier.get();
            var blackList = DisabledContentConfig.getInstance().getLoadedData();
            if (Objects.nonNull(blackList)) {
                if (blackList.items.contains(item.getRegistryName().getPath())) return null;
            }
            return item;
        });
        ITEMS.add(memorized);
        return memorized;
    }

    public static <B extends Block> Supplier<ItemBlock> registerItemBlock(Supplier<B> supplier) {
        return registerItemBlock(ItemBlock::new, supplier);
    }

    public static <I extends ItemBlock, B extends Block> Supplier<I> registerItemBlock(Function<B, I> blockIngot,
                                                                                       Supplier<B> supplier) {
        var memorized = Suppliers.memoize(() -> {
            var block = supplier.get();
            if (Objects.isNull(block)) return null;
            var itemBlock = blockIngot.apply(block);
            itemBlock.setRegistryName(block.getRegistryName());
            return itemBlock;
        });
        ITEMS.add(memorized);
        return memorized;
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        var registry = event.getRegistry();
        ITEMS.stream().map(Supplier::get).filter(Objects::nonNull)
                .forEach(registry::register);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ITEMS.stream().map(Supplier::get)
                .filter(Objects::nonNull)
                .filter(item -> item instanceof IHasModel)
                .map(item -> (IHasModel) item)
                .forEach(IHasModel::registerModels);
    }
}
