package io.github.srdjanv.endreforked.common;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Lists;

import io.github.srdjanv.endreforked.Tags;
import io.github.srdjanv.endreforked.api.base.groupings.FluidToItemGrouping;
import io.github.srdjanv.endreforked.api.endforge.EndForgeHandler;
import io.github.srdjanv.endreforked.api.endforge.EndForgeRecipe;
import io.github.srdjanv.endreforked.api.entropywand.Conversion;
import io.github.srdjanv.endreforked.api.entropywand.EntropyWandHandler;
import io.github.srdjanv.endreforked.api.materializer.ItemCatalyst;
import io.github.srdjanv.endreforked.api.materializer.MaterializerHandler;
import io.github.srdjanv.endreforked.api.materializer.MaterializerRecipe;
import io.github.srdjanv.endreforked.api.materializer.WorldEvent;
import io.github.srdjanv.endreforked.api.util.Structure;
import io.github.srdjanv.endreforked.common.capabilities.timedflight.CapabilityTimedFlightHandler;
import io.github.srdjanv.endreforked.common.configs.Configs;
import io.github.srdjanv.endreforked.common.tiles.EndForgeTile;
import io.github.srdjanv.endreforked.common.tiles.MaterializerTile;
import io.github.srdjanv.endreforked.common.village.EndVillagerHandler;
import io.github.srdjanv.endreforked.utils.Initializer;

final class Registration implements Initializer {

    public void preInit(FMLPreInitializationEvent event) {
        CapabilityTimedFlightHandler.register();
        registerMobs();

        GameRegistry.registerTileEntity(MaterializerTile.class,
                new ResourceLocation(Tags.MODID, "materializerTile"));
        GameRegistry.registerTileEntity(EndForgeTile.class,
                new ResourceLocation(Tags.MODID, "endForgeTile"));
    }

    public void init(FMLInitializationEvent event) {
        registerMaterializerRecipes();
        registerEndForgeRecipes();
        registerEntropyWandRecipes();
        registerBannerPatterns();

        // todo remove or improve
        if (Configs.SERVER_SIDE_CONFIGS.spawnNewVillagers) {
            EndVillagerHandler.initVillagerTrades();
            EndVillagerHandler.initVillagerHouse();
        }

        GameRegistry.addSmelting(ModBlocks.TUNGSTEN_ORE.get(), new ItemStack(ModItems.TUNGSTEN_INGOT.get(), 1), 1.5f);
        handleOreDictionary();
    }

    private static void registerEntropyWandRecipes() {
        EntropyWandHandler.registerDefaultStateConversion(Blocks.END_STONE, ModBlocks.END_STONE_ENTROPY_BLOCK.get());
        EntropyWandHandler.registerDefaultStateConversion(Blocks.STONE, Blocks.COBBLESTONE);
        EntropyWandHandler.registerDefaultStateConversion(Blocks.COBBLESTONE, Blocks.GRAVEL);
        EntropyWandHandler.registerDefaultStateConversion(Blocks.GRAVEL, Blocks.SAND);
        EntropyWandHandler.registerDefaultStateConversion(Blocks.SANDSTONE, Blocks.SAND);
        EntropyWandHandler.registerConversions(
                Lists.newArrayList(Blocks.TALLGRASS, Blocks.RED_FLOWER, Blocks.YELLOW_FLOWER),
                Conversion.builder()
                        .matcherAny()
                        .newState(Blocks.DEADBUSH)
                        .playFlintSound()
                        .build());
        EntropyWandHandler.registerConversions(
                Lists.newArrayList(Blocks.LOG, Blocks.LOG2),
                Conversion.builder()
                        .matcherAny()
                        .newState(Blocks.COAL_BLOCK)
                        .addItemDamage(8)
                        .playFlintSound()
                        .build());
    }

    private static void registerEndForgeRecipes() {
        var lavaGroup = new FluidToItemGrouping<EndForgeRecipe>(new FluidStack(FluidRegistry.LAVA, 1000));
        EndForgeHandler.getInstance().registerRecipeGrouping(lavaGroup);

        lavaGroup.registerRecipe(new EndForgeRecipe(new ItemStack(ModItems.INGOT_ENDORIUM.get()), 120,
                (lava, item) -> new ItemStack(ModItems.INFUSED_METALL.get())));

        lavaGroup.registerRecipe(new EndForgeRecipe(new ItemStack(Items.SLIME_BALL), 200,
                (lava, item) -> new ItemStack(Items.MAGMA_CREAM)));

        lavaGroup.registerRecipe(new EndForgeRecipe(new ItemStack(ModItems.END_ESSENCE.get()), 200,
                (lava, item) -> new ItemStack(Items.BLAZE_POWDER)));

        lavaGroup.registerRecipe(new EndForgeRecipe(new ItemStack(ModItems.ENTROPY_END_STONE.get()), 200,
                (lava, item) -> new ItemStack(ModBlocks.END_MAGMA_BLOCK.get())));
    }

    private static void registerMaterializerRecipes() {
        var itemCatalyst = new ItemCatalyst(new ItemStack(ModItems.CATALYST.get()));
        MaterializerHandler.getInstance().registerRecipeGrouping(itemCatalyst);

        {
            var ironToIronRecipe = new MaterializerRecipe(
                    new ItemStack(Items.IRON_INGOT), 200,
                    (stack, catalyst) -> new ItemStack(Items.IRON_INGOT, 2));

            itemCatalyst.registerRecipe(ironToIronRecipe);
            ironToIronRecipe.registerWorldEvent(30, WorldEvent.create(15,
                    Structure.builder().aisle("S").where('S', Blocks.STONE).build(),
                    WorldEvent.replaceEachPosWithDefaultBlockState(Blocks.IRON_BLOCK)));

            ironToIronRecipe.registerWorldEvent(60, WorldEvent.create(15,
                    Structure.builder().aisle("I").where('I', Blocks.IRON_BLOCK).build(),
                    WorldEvent.replaceEachPosWithDefaultBlockState(Blocks.GOLD_BLOCK)));

            ironToIronRecipe.registerWorldEvent(90, WorldEvent.create(15,
                    Structure.builder().aisle("G").where('G', Blocks.GOLD_BLOCK).build(),
                    WorldEvent.replaceEachPosWithDefaultBlockState(Blocks.STONE)));
        }

        var diaToDiaRecipe = new MaterializerRecipe(
                new ItemStack(Items.DIAMOND), 600,
                (stack, catalyst) -> new ItemStack(Items.DIAMOND, 1));
        itemCatalyst.registerRecipe(diaToDiaRecipe);

        diaToDiaRecipe.registerWorldEvent(30, WorldEvent.create(2,
                Structure.builder()
                        .aisle("S")
                        .aisle("S")
                        .aisle("S")
                        .aisle("S")
                        .aisle("S")
                        .aisle("S")
                        .where('S', Blocks.STONE).build(),
                WorldEvent.replaceEachPosWithDefaultBlockState(Blocks.IRON_BLOCK)));

        diaToDiaRecipe.registerWorldEvent(60, WorldEvent.create(2,
                Structure.builder()
                        .aisle("IIIIIII")
                        .aisle("I      ")
                        .aisle("I      ")
                        .aisle("I      ")
                        .aisle("I      ")
                        .aisle("I      ")
                        .aisle("I      ")
                        .where('I', Blocks.IRON_BLOCK).build(),
                WorldEvent.replaceEachPosWithDefaultBlockState(Blocks.GOLD_BLOCK)));

        diaToDiaRecipe.registerWorldEvent(90, WorldEvent.create(2,
                Structure.builder()
                        .aisle("GGGGGGG")
                        .where('G', Blocks.GOLD_BLOCK).build(),
                WorldEvent.replaceEachPosWithItem(Items.DIAMOND)));
    }

    private static void registerBannerPatterns() {
        Class<? extends Enum<?>> clazz = BannerPattern.class;
        addPattern(clazz, "rune", "run", new ItemStack(ModItems.END_RUNE.get()));
        addPattern(clazz, "end", "end", new ItemStack(Items.CHORUS_FRUIT_POPPED));
        addPattern(clazz, "pearl", "prl", new ItemStack(Items.ENDER_PEARL));
    }

    private static void handleOreDictionary() {
        OreDictionary.registerOre("ingotEndorium", ModItems.INGOT_ENDORIUM.get());

        OreDictionary.registerOre("ingotTungsten", ModItems.TUNGSTEN_INGOT.get());
        OreDictionary.registerOre("nuggetTungsten", ModItems.TUNGSTEN_NUGGET.get());
        OreDictionary.registerOre("blockTungsten ", ModBlocks.TUNGSTEN_BLOCK.get());
        OreDictionary.registerOre("oreTungsten", ModBlocks.TUNGSTEN_ORE.get());
        OreDictionary.registerOre("oreTungsten", ModBlocks.TUNGSTEN_END_ORE.get());

        OreDictionary.registerOre("dustObsidian", ModItems.CATALYST.get());
        OreDictionary.registerOre("shardObsidian", ModItems.SHARD_OBSIDIAN.get());
        OreDictionary.registerOre("hammerIron", ModItems.HAMMER_IRON.get());
        OreDictionary.registerOre("hammer", ModItems.HAMMER_IRON.get());
        OreDictionary.registerOre("shardLormyte", ModItems.LORMYTE_CRYSTAL.get());
        OreDictionary.registerOre("essence", ModItems.END_ESSENCE.get());
    }

    public static void addPattern(Class<? extends Enum<?>> clazz, String name, String id, ItemStack craftingItem) {
        name = "endreborn_" + name;
        id = "er_" + id;
        EnumHelper.addEnum(clazz, name.toUpperCase(), new Class[] { String.class, String.class, ItemStack.class }, name,
                id, craftingItem);
    }

    private static void registerMobs() {}
}
