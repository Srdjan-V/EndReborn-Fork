package endreborn.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import org.apache.commons.lang3.tuple.Pair;

import endreborn.EndReborn;
import endreborn.Reference;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

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

    public static final WorldOreGenConfig WORLD_ORE_GEN_CONFIG = new WorldOreGenConfig();

    public static class WorldOreGenConfig {

        public final WorldGen essenceOre = new EssenceOre();

        public final WorldGen tungstenOre = new TungstenOre();

        public final WorldGen lormyte = new Lormyte();
        public final WorldGen endMagma = new EndMagma();
        public final WorldGen entropyEndStone = new EntropyEndStone();
        public final WorldGen endCoral = new EndCoral();
        public final WorldGen endFlower = new EndFlower();

        public static class EssenceOre extends WorldGen {

            {
                oreSpawnConfig.put("0", new int[] { 80, 0, 256 });
                oreSpawnConfig.put("1", new int[] { 60, 0, 256 });
            }
        }

        public static class TungstenOre extends WorldGen {

            {
                oreSpawnConfig.put("0", new int[] { 80, 0, 48 });
            }
        }

        public static class Lormyte extends WorldGen {

            {
                oreSpawnConfig.put("1", new int[] { 1, 35, 52 });
            }
        }

        public static class EndMagma extends WorldGen {

            {
                oreSpawnConfig.put("1", new int[] { 2, 0, 22 });
            }
        }

        public static class EntropyEndStone extends WorldGen {

            {
                oreSpawnConfig.put("1", new int[] { 2, 0, 22 });
            }
        }

        public static class EndCoral extends WorldGen {

            {
                oreSpawnConfig.put("1", new int[] { 2, 50, 90 });
            }
        }

        public static class EndFlower extends WorldGen {

            {
                oreSpawnConfig.put("1", new int[] { 2, 50, 90 });
            }
        }

        public static class WorldGen {

            @Config.Name("Enable generator")
            @Config.RequiresMcRestart
            public boolean enableGeneration = true;

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

        public List<WorldGen> getOreGensForDim(int dim) {
            return Stream.of(essenceOre, tungstenOre, lormyte, endMagma, entropyEndStone, endCoral, endFlower)
                    .filter(oreGen -> oreGen.enableGeneration)
                    .filter(oreGen -> oreGen.oreSpawnConfig.containsKey(String.valueOf(dim)))
                    .collect(Collectors.toList());
        }
    }

    public static final WorldGenStructureConfig WORLD_GEN_STRUCTURE_CONFIG = new WorldGenStructureConfig();

    public static class WorldGenStructureConfig {

        public final StructGen endRuins = new EndRuins();

        public final StructGen endIslands = new EndIslands();

        public final StructGen observatory = new Observatory();

        public static class EndRuins extends StructGen {

            {
                structureSpawnConfig.put("sky", 300);
            }
        }

        public static class EndIslands extends StructGen {

            {
                structureSpawnConfig.put("sky", 200);
                structureSpawnConfig.put("plains", 200);
                structureSpawnConfig.put("desert", 200);
                structureSpawnConfig.put("ocean", 200);
                structureSpawnConfig.put("deep_ocean", 200);
                structureSpawnConfig.put("savanna", 200);
            }
        }

        public static class Observatory extends StructGen {

            {
                structureSpawnConfig.put("desert", 600);
                structureSpawnConfig.put("ocean", 600);
                structureSpawnConfig.put("deep_ocean", 600);
                structureSpawnConfig.put("forest", 600);
                structureSpawnConfig.put("birch_forest", 600);
                structureSpawnConfig.put("swampland", 600);
            }
        }

        public static class StructGen {

            @Config.Name("Enable structure gen")
            @Config.RequiresMcRestart
            public boolean enableGeneration = true;

            @Config.Name("Dims to spawn structures")
            @Config.RequiresMcRestart
            @Config.Comment({
                    "Key: biome id (minecraft:river, minecraft:sky)",
                    "Value: spawn rarity(The lower the value, the higher the spawn chance)" })
            public Map<String, Integer> structureSpawnConfig = new HashMap<>(1);
        }

        public List<Pair<StructGen, Object2IntMap<Biome>>> getStructGensForBiome(Biome biome) {
            return Stream.of(endRuins, endIslands, observatory)
                    .filter(structGen -> structGen.enableGeneration)
                    .map(structGen -> {
                        Object2IntMap<Biome> biomeIntegerMap = null;
                        for (var spawnEntry : structGen.structureSpawnConfig.entrySet()) {
                            var resBiome = Biome.REGISTRY.getObject(new ResourceLocation(spawnEntry.getKey()));
                            if (Objects.isNull(resBiome)) {
                                EndReborn.LOGGER.warn("Unable to find biome `{}` for structure gen",
                                        spawnEntry.getKey());
                                continue;
                            }
                            if (resBiome.equals(biome)) {
                                if (biomeIntegerMap == null) biomeIntegerMap = new Object2IntArrayMap<>();
                                biomeIntegerMap.put(resBiome, spawnEntry.getValue());
                            }
                        }
                        if (biomeIntegerMap == null) return null;
                        return Pair.of(structGen, biomeIntegerMap);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    public static final GeneralConfig GENERAL = new GeneralConfig();

    public static class GeneralConfig {

        @Config.Name("Register endorium tools")
        @Config.RequiresMcRestart
        public boolean registerEndoriumTools = true;

        @Config.Name("Register tungsten tools")
        @Config.RequiresMcRestart
        public boolean registerTungstenTools = true;

        @Config.Name("Register obsidian armor")
        @Config.RequiresMcRestart
        public boolean registerObsidianArmor = true;

        @Config.Name("Register dragon armor")
        @Config.RequiresMcRestart
        public boolean registerDragonArmor = true;

        @Config.Name("ender")
        @Config.RequiresMcRestart
        @Config.Comment({ "EnchantBoost" })
        public int[] enchantBoost = { 1 };

        @Config.Name("EntropyWand tool damage per conversion")
        @Config.RequiresMcRestart
        @Config.Comment({ "EnchantBoost" })
        public int entropyWandUseDamage = 2;

        @Config.Name("Show broken textures in jei for the EntropyWand")
        @Config.RequiresMcRestart
        @Config.Comment({ "EnchantBoost" })
        public boolean entropyWandRenderBrokenTextures = false;

        @Config.Name("New Villagers")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn." })
        public boolean spawnNewVillagers = true;

        @Config.Name("Chest Loot")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to fill." })
        public boolean chestLoot = true;

        @Config.Name("Entity Loot")
        @Config.RequiresMcRestart
        public boolean entityLoot = true;

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
}
