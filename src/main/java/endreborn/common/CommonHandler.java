package endreborn.common;

import static endreborn.common.LootTableHandler.CHEST_TABLES;
import static endreborn.common.ModEnchants.*;
import static endreborn.common.ModSounds.THE_VOID;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import endreborn.EndReborn;
import endreborn.Reference;
import endreborn.common.entity.*;
import endreborn.common.gui.GuiHandler;
import endreborn.common.tiles.MaterializerTile;
import endreborn.common.world.OreGen;
import endreborn.common.world.WorldGenCustomStructures;

public final class CommonHandler {

    @SubscribeEvent
    public static void onEvent(final RegistryEvent.Register<Enchantment> event) {
        final IForgeRegistry<Enchantment> registry = event.getRegistry();

        registry.register(ender_core);
        registry.register(ender_killer);
        registry.register(shulker_core);
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> register) {
        register.getRegistry().register(THE_VOID);
    }

    public static void preInit() {
        {
            int id = 1;
            EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "endguard"), EntityEGuard.class,
                    "endguard", id++, EndReborn.instance, 64, 3, false, 9654933, 11237052);
            EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "watcher"), EntityWatcher.class,
                    "watcher", id++, EndReborn.instance, 64, 3, false, 461076, 2236447);
            EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "endlord"), EntityLord.class,
                    "endlord", id++, EndReborn.instance, 64, 3, false, 461076, 681365);
            EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "chronologist"),
                    EntityChronologist.class, "chronologist", id++, EndReborn.instance, 64, 3, false, 461076, 13680725);
            EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "end_fireball"),
                    EntityColdFireball.class,
                    Reference.MODID + "." + "end_fireball", id, EndReborn.instance, 30, 1,
                    true);
        }

        GameRegistry.registerTileEntity(MaterializerTile.class,
                new ResourceLocation(Reference.MODID + ":entropy_user"));

        GameRegistry.registerWorldGenerator(new OreGen(), 0);
        GameRegistry.registerWorldGenerator(new WorldGenCustomStructures(), 0);
    }

    public static void init() {
        GameRegistry.addSmelting(ModBlocks.TUNGSTEN_ORE.get(), new ItemStack(ModItems.TUNGSTEN_INGOT.get(), 1), 1.5f);

        if (Configs.GENERAL.chestLoot) {
            for (String s : CHEST_TABLES) {
                LootTableList.register(new ResourceLocation(Reference.MODID, "inject/chests/" + s));
            }
        }

        {// Banner pattern
            Class<? extends Enum<?>> clazz = BannerPattern.class;
            addPattern(clazz, "rune", "run", new ItemStack(ModItems.END_RUNE.get()));
            addPattern(clazz, "end", "end", new ItemStack(Items.CHORUS_FRUIT_POPPED));
            addPattern(clazz, "pearl", "prl", new ItemStack(Items.ENDER_PEARL));
        }

       // NetworkRegistry.INSTANCE.registerGuiHandler(EndReborn.instance, new GuiHandler());

        OreDictionary.registerOre("ingotEndorium", ModItems.INGOT_ENDORIUM.get());
        OreDictionary.registerOre("ingotTungsten", ModItems.TUNGSTEN_INGOT.get());
        OreDictionary.registerOre("nuggetTungsten", ModItems.TUNGSTEN_NUGGET.get());
        OreDictionary.registerOre("oreTungsten", ModBlocks.TUNGSTEN_ORE.get());
        OreDictionary.registerOre("tungstenIngot", ModItems.TUNGSTEN_INGOT.get());
        OreDictionary.registerOre("dustObsidian", ModItems.CATALYST.get());
        OreDictionary.registerOre("shardObsidian", ModItems.SHARD_OBSIDIAN.get());
        OreDictionary.registerOre("hammerIron", ModItems.HAMMER_IRON.get());
        OreDictionary.registerOre("hammer", ModItems.HAMMER_IRON.get());
        OreDictionary.registerOre("shardLormyte", ModItems.LORMYTE_CRYSTAL.get());
        OreDictionary.registerOre("essence", ModItems.END_ESSENCE.get());
    }

    public static void postInit() {}

    private CommonHandler() {}

    public static void addPattern(Class<? extends Enum<?>> clazz, String name, String id, ItemStack craftingItem) {
        name = "endreborn_" + name;
        id = "er_" + id;
        EnumHelper.addEnum(clazz, name.toUpperCase(), new Class[] { String.class, String.class, ItemStack.class }, name,
                id, craftingItem);
    }
}
