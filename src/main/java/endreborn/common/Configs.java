package endreborn.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
                    "Key: biome id (minecraft:river, minecraft:sky)",
                    "Value: ",
                    "   [0]: spawn probability",
                    "   [1]: minimum spawn group",
                    "   [2]: maximum spawn group" })
            public Map<String, int[]> biomeSpawnConfig = new HashMap<>(1);

            public Map<Biome, int[]> getBiomesForMob() {
                Map<Biome, int[]> ret = new HashMap<>();
                for (var entityConfig : biomeSpawnConfig.entrySet()) {
                    var biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(entityConfig.getKey()));
                    if (biome != null) ret.put(biome, entityConfig.getValue());
                }
                return ret;
            }
        }
    }

    public static final WorldGenConfig WORLD_GEN_CONFIG = new WorldGenConfig();

    public static class WorldGenConfig {

        public final OreGen essenceOre = new EssenceOre();

        public final OreGen tungstenOre = new TungstenOre();

        public final OreGen lormyte = new Lormyte();
        public final OreGen endMagma = new EndMagma();
        public final OreGen entropyEndStone = new EntropyEndStone();

        public static class EssenceOre extends OreGen {

            {
                oreSpawnConfig.put("0", new int[] { 80, 0, 256 });
                oreSpawnConfig.put("1", new int[] { 60, 0, 256 });
            }
        }

        public static class TungstenOre extends OreGen {

            {
                oreSpawnConfig.put("0", new int[] { 80, 0, 48 });
            }
        }

        public static class Lormyte extends OreGen {

            {
                oreSpawnConfig.put("1", new int[] { 1, 35, 52 });
            }
        }

        public static class EndMagma extends OreGen {

            {
                oreSpawnConfig.put("1", new int[] { 2, 0, 22 });
            }
        }

        public static class EntropyEndStone extends OreGen {

            {
                oreSpawnConfig.put("1", new int[] { 2, 0, 22 });
            }
        }

        public static class OreGen {

            @Config.Name("Enable ore spawn")
            @Config.RequiresMcRestart
            public boolean enableSpawning = true;

            @Config.Name("Dims to spawn ores")
            @Config.RequiresMcRestart
            @Config.Comment({
                    "Key: dim id",
                    "Value:",
                    "   [0]: spawn rarity(The lower the value, the higher the spawn chance)",
                    "   [1]: min height",
                    "   [2]: max height" })
            public Map<String, int[]> oreSpawnConfig = new HashMap<>(1);
        }

        public List<OreGen> getOreGensForDim(int dim) {
            return Stream.of(essenceOre, tungstenOre, lormyte, endMagma, entropyEndStone)
                    .filter(oreGen -> oreGen.enableSpawning)
                    .filter(oreGen -> oreGen.oreSpawnConfig.containsKey(String.valueOf(dim)))
                    .collect(Collectors.toList());
        }
    }

    public static final GeneralConfig GENERAL = new GeneralConfig();
    public static final BalanceConfig BALANCE = new BalanceConfig();

    public static class GeneralConfig {

        @Config.Name("End Ruines")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn." })
        public boolean spawnEndRuines = true;

        @Config.Name("New Villagers")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn." })
        public boolean spawnNewVillagers = true;

        @Config.Name("End Islands")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn." })
        public boolean spawnEndIsland = true;

        @Config.Name("Observatory")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn." })
        public boolean spawnObservatory = true;

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
