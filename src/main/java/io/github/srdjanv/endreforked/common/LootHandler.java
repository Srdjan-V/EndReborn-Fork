package io.github.srdjanv.endreforked.common;

import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import io.github.srdjanv.endreforked.Tags;
import io.github.srdjanv.endreforked.utils.Initializer;

public final class LootHandler implements Initializer {

    public static final ResourceLocation END_GUARD = new ResourceLocation(Tags.MODID, "entities/endguard");
    public static final ResourceLocation WATCHER = new ResourceLocation(Tags.MODID, "entities/watcher");
    public static final ResourceLocation LORD = new ResourceLocation(Tags.MODID, "entities/lord");

    public static final List<String> INJECTED_CHEST_TABLES = ImmutableList.of(
            "abandoned_mineshaft",
            "desert_pyramid",
            "end_city_treasure",
            "jungle_temple",
            "nether_bridge",
            "village_blacksmith");

    public static final List<String> INJECTED_ENTITIES_TABLES = ImmutableList.of("ender_dragon");

    @Override
    public void registerEventBus() {
        registerThisToEventBus();
    }

    public void preInit() {
        for (ResourceLocation table : Lists.newArrayList(END_GUARD, WATCHER, LORD))
            LootTableList.register(table);

        // TODO: 04/11/2023 check if needed
        /*
         * if (Configs.GENERAL.chestLoot)
         * for (String s : INJECTED_CHEST_TABLES)
         * LootTableList.register(new ResourceLocation(Reference.MODID, "inject/chests/" + s));
         * 
         * if (Configs.GENERAL.entityLoot) {
         * LootTableList.register(new ResourceLocation(Reference.MODID, "inject/entities/ender_dragon"));
         * }
         */
    }

    @SubscribeEvent
    public void lootLoad(LootTableLoadEvent evt) {
        if (Configs.GENERAL.chestLoot)
            inject(evt, "minecraft:chests/", INJECTED_CHEST_TABLES);
        if (Configs.GENERAL.entityLoot)
            inject(evt, "minecraft:entities/", INJECTED_ENTITIES_TABLES);
    }

    private static void inject(LootTableLoadEvent event, String lootTableCategory, List<String> lootTablesInjectors) {
        final String name = event.getName().toString();
        if (name.startsWith(lootTableCategory) &&
                lootTablesInjectors.contains(name.substring(lootTableCategory.length()))) {
            String file = name.substring("minecraft:".length());
            event.getTable().addPool(getInjectPool(file));
        }
    }

    private static LootPool getInjectPool(String entryName) {
        return new LootPool(new LootEntry[] { getInjectEntry(entryName) }, new LootCondition[0],
                new RandomValueRange(1), new RandomValueRange(0, 1), "endreborn_inject_pool");
    }

    private static LootEntryTable getInjectEntry(String name) {
        return new LootEntryTable(new ResourceLocation(Tags.MODID, "inject/" + name), 1, 0, new LootCondition[0],
                "endreborn_inject_entry");
    }
}
