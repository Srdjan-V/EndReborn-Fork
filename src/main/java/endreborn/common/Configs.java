package endreborn.common;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.config.Config;

import endreborn.Reference;

@Config(modid = Reference.MODID, category = "")
public final class Configs {

    public static final MobsConfig MOBS_CONFIG = new MobsConfig();

    public static class MobsConfig {

        @Config.Name("End Guard Mob")
        public final RootMobConfig endGuard = new EndGuard();

        @Config.Name("Watcher Mob")
        public final RootMobConfig watcher = new Watcher();

        @Config.Name("Chronologist Mob")
        public final RootMobConfig chronologist = new Chronologist();

        @Config.Name("EndLord Mob")
        public final RootMobConfig endlord = new EndLord();

        public static class EndGuard extends RootMobConfig {

            {
                biomeSpawnConfig.put("sky", new int[] { 4, 1, 3 });
            }
        }

        public static class Watcher extends RootMobConfig {

            {
                biomeSpawnConfig.put("sky", new int[] { 10, 1, 3 });
                biomeSpawnConfig.put("plains", new int[] { 4, 1, 1 });
            }
        }

        public static class Chronologist extends RootMobConfig {

            {
                biomeSpawnConfig.put("sky", new int[] { 4, 1, 3 });
            }
        }

        public static class EndLord extends RootMobConfig {

            {
                registerSpawn = false;
            }
        }

        public static class RootMobConfig {

            @Config.Name("Register")
            @Config.RequiresMcRestart
            @Config.Comment({ "Register the mob" })
            public boolean register = true;

            @Config.Name("Register Spawn")
            @Config.RequiresMcRestart
            @Config.Comment({ "Register the mob to the spawn biome list" })
            public boolean registerSpawn = true;

            @Config.Name("Biome spawn config")
            @Config.RequiresMcRestart
            @Config.Comment({
                    "Key: biome name (minecraft:river), value: [0]: spawn probability, [1]: minimum spawn group, [2]: maximum spawn group" })
            public Map<String, int[]> biomeSpawnConfig = new HashMap<>(1);

            /*
             * public Biome[] getBiomesForMob() {
             * List<Biome> ret = new ArrayList<>();
             * for (String biomeKey : biomes) {
             * var biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biomeKey));
             * if (biome != null) ret.add(biome);
             * }
             * return ret.toArray(new Biome[0]);
             * }
             */
        }
    }

    public static final GeneralConfig GENERAL = new GeneralConfig();
    public static final BalanceConfig BALANCE = new BalanceConfig();

    public static class GeneralConfig {

        @Config.Name("Essence Ore")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn." })
        public boolean spawnEssenceOre = true;

        @Config.Name("End Ruines")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn." })
        public boolean spawnEndRuines = true;

        @Config.Name("New Villagers")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn." })
        public boolean spawnNewVillagers = true;

        @Config.Name("Wolframium Ore")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn." })
        public boolean spawnWolframiumOre = true;

        @Config.Name("Watcher Mob")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn." })
        public boolean spawnWatcher = true;

        @Config.Name("Chronologist Mob")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn." })
        public boolean spawnChronologist = false;

        @Config.Name("End Islands")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn." })
        public boolean spawnEndIsland = true;

        @Config.Name("Observatory")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn." })
        public boolean spawnObservatory = true;

        @Config.Name("Lormyte")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn." })
        public boolean spawnLormyte = true;

        @Config.Name("End Magma, Enropy End Stone")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn." })
        public boolean decoratorEnd = true;

        @Config.Name("Chest Loot")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to fill." })
        public boolean chestLoot = true;

        @Config.Name("Panorama")
        @Config.RequiresMcRestart
        @Config.Comment({ "Main menu panorama" })
        public boolean panorama = true;

        @Config.Name("End Void Teleporter")
        @Config.RequiresMcRestart
        @Config.Comment({ "When player falls to void in The End, he teleports to the Overworld" })
        public boolean teleporterEnd = true;

        private GeneralConfig() {}
    }

    public static class BalanceConfig {

        @Config.Name("Observatory Rarity")
        @Config.RangeInt(min = 1, max = 1000)
        @Config.Comment({ "The higher the value, the higher the rarity. To disable check the general config" })
        public int obsRare = 600;

        @Config.Name("Island Rarity")
        @Config.RangeInt(min = 1, max = 1000)
        @Config.Comment({ "The higher the value, the higher the rarity. To disable check the general config" })
        public int islandRare = 200;

        @Config.Name("End Ruines Rarity")
        @Config.RangeInt(min = 1, max = 1000)
        @Config.Comment({ "The higher the value, the higher the rarity. To disable check the general config" })
        public int ruinesRare = 300;

        @Config.Name("Essence Rarity In The End")
        @Config.RangeInt(min = 1, max = 1000)
        @Config.Comment({ "The lower the value, the higher the rarity. To disable check the general config" })
        public int essenceRareEnd = 200;

        @Config.Name("Essence Rarity In Overworld")
        @Config.RangeInt(min = 1, max = 1000)
        @Config.Comment({ "The lower the value, the higher the rarity. To disable check the general config" })
        public int essenceRareOver = 80;

        @Config.Name("Wolframium Rarity")
        @Config.RangeInt(min = 1, max = 1000)
        @Config.Comment({ "The lower the value, the higher the rarity. To disable check the general config" })
        public int wolframiumRare = 25;

        @Config.Name("End Guard Spawn Rarity")
        @Config.RangeInt(min = 1, max = 1000)
        @Config.Comment({ "Chance to spawn = 1/(this number). To disable check the general config" })
        public int guardRare = 50;

        @Config.Name("Chronologist Spawn Rarity")
        @Config.RangeInt(min = 1, max = 1000)
        @Config.Comment({ "Chance to spawn = 1/(this number). To disable check the general config" })
        public int chronRare = 200;

        @Config.Name("Watcher Spawn Rarity")
        @Config.RangeInt(min = 1, max = 1000)
        @Config.Comment({ "Chance to spawn = 1/(this number). To disable check the general config" })
        public int watcherRare = 50;

        private BalanceConfig() {}
    }
}
