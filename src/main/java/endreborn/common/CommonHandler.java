package endreborn.common;

import static endreborn.common.LootTableHandler.CHEST_TABLES;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import endreborn.EndReborn;
import endreborn.Reference;
import endreborn.common.entity.*;
import endreborn.common.tiles.MaterializerTile;
import endreborn.common.world.OreGen;
import endreborn.common.world.WorldGenCustomStructures;

public final class CommonHandler {

    public static void preInit() {
        {
            if (Configs.MOBS_CONFIG.endGuard.register) {
                registerMobEntity("endguard", EntityEGuard.class, 0, 64, 3, false, 9654933, 11237052);
                registerEntity("end_fireball", EntityColdFireball.class, 4, 30, 1, true);
                registerSpawn(Configs.MOBS_CONFIG.endGuard, EntityEGuard.class);
            }

            if (Configs.MOBS_CONFIG.watcher.register) {
                registerMobEntity("watcher", EntityWatcher.class, 1, 64, 3, false, 461076, 2236447);
                registerSpawn(Configs.MOBS_CONFIG.watcher, EntityWatcher.class);
            }

            if (Configs.MOBS_CONFIG.endlord.register) {
                registerMobEntity("endlord", EntityLord.class, 2, 64, 3, false, 461076, 681365);
                registerSpawn(Configs.MOBS_CONFIG.endlord, EntityLord.class);
            }

            if (Configs.MOBS_CONFIG.chronologist.register) {
                registerMobEntity("chronologist", EntityChronologist.class, 3, 64, 3, false, 461076, 13680725);
                registerSpawn(Configs.MOBS_CONFIG.chronologist, EntityChronologist.class);
            }

        }

        GameRegistry.registerTileEntity(MaterializerTile.class,
                new ResourceLocation(Reference.MODID + ":entropy_user"));

        GameRegistry.registerWorldGenerator(new OreGen(), 0);
        GameRegistry.registerWorldGenerator(new WorldGenCustomStructures(), 0);
    }

    public static void init() {
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

        GameRegistry.addSmelting(ModBlocks.TUNGSTEN_ORE.get(), new ItemStack(ModItems.TUNGSTEN_INGOT.get(), 1), 1.5f);
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

    private static void registerMobEntity(String name, Class<? extends Entity> entityClass, int id, int trackingRange,
                                          int updateFrequency, boolean sendsVelocityUpdates, int eggPrimary,
                                          int eggSecondary) {
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, name), entityClass, name,
                id, EndReborn.instance, trackingRange, updateFrequency, sendsVelocityUpdates, eggPrimary, eggSecondary);
    }

    private static void registerEntity(String name, Class<? extends Entity> entityClass, int id, int trackingRange,
                                       int updateFrequency, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, name), entityClass, name,
                id, EndReborn.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    }

    private static void registerSpawn(Configs.MobsConfig.RootMobConfig config,
                                      Class<? extends EntityLiving> entityClass) {
        /*
         * if (config.registerSpawn) {
         * EntityRegistry.addSpawn(entityClass,
         * config.weightedProb,
         * config.min, config.max, EnumCreatureType.MONSTER,
         * config.getBiomesForMob());// might be too early to que biomes
         * }
         */
    }
}
