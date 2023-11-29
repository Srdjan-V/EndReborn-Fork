package io.github.srdjanv.endreforked.common;

import java.util.*;
import java.util.stream.Stream;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import org.jetbrains.annotations.Nullable;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.Tags;
import io.github.srdjanv.endreforked.api.worldgen.DimConfig;
import io.github.srdjanv.endreforked.api.worldgen.GenConfig;
import io.github.srdjanv.endreforked.api.worldgen.GeneratorBuilder;
import io.github.srdjanv.endreforked.api.worldgen.WorldGenHandler;
import io.github.srdjanv.endreforked.api.worldgen.features.SurfaceGenerator;
import io.github.srdjanv.endreforked.api.worldgen.features.WorldGenStructure;
import io.github.srdjanv.endreforked.common.blocks.BlockEnderCrop;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

@Config(modid = Tags.MODID, category = "")
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

        public final WorldGenConfigBase essenceOre = new EssenceOre();

        public final WorldGenConfigBase tungstenOre = new TungstenOre();
        public final WorldGenConfigBase tungstenEndOre = new TungstenEndOre();

        public final WorldGenConfigBase lormyte = new Lormyte();
        public final WorldGenConfigBase endMagma = new EndMagma();
        public final WorldGenConfigBase entropyEndStone = new EntropyEndStone();
        public final WorldGenConfigBase endCoral = new EndCoral();
        public final WorldGenConfigBase endFlower = new EndFlower();

        public static class EssenceOre extends WorldGenConfigBase {

            public EssenceOre() {
                dimWhiteList.put("0", new int[] { 80, 20, 0, 256 });
                dimWhiteList.put("1", new int[] { 60, 40, 0, 256 });
            }

            @Override
            public GeneratorBuilder<WorldGenerator> getGeneratorBuilder() {
                return ((world, biome, config) -> new WorldGenMinable(ModBlocks.ESSENCE_ORE.get().getDefaultState(),
                        config.count(), BlockMatcher.forBlock(Blocks.OBSIDIAN)));
            }
        }

        public static class TungstenOre extends WorldGenConfigBase {

            public TungstenOre() {
                dimWhiteList.put("0", new int[] { 80, 10, 0, 48 });
            }

            @Override
            public GeneratorBuilder<WorldGenerator> getGeneratorBuilder() {
                return ((world, biome, config) -> new WorldGenMinable(ModBlocks.TUNGSTEN_ORE.get().getDefaultState(),
                        config.count(), input -> {
                            if (input != null && input.getBlock() == Blocks.STONE) {
                                return input.getValue(BlockStone.VARIANT).equals(BlockStone.EnumType.DIORITE);
                            } else return false;
                        }));
            }
        }

        public static class TungstenEndOre extends WorldGenConfigBase {

            public TungstenEndOre() {
                dimWhiteList.put("1", new int[] { 80, 25, 0, 48 });
            }

            @Override
            public GeneratorBuilder<WorldGenerator> getGeneratorBuilder() {
                return ((world, biome, config) -> new WorldGenMinable(
                        ModBlocks.TUNGSTEN_END_ORE.get().getDefaultState(),
                        config.count(), BlockMatcher.forBlock(Blocks.END_STONE)));
            }
        }

        public static class Lormyte extends WorldGenConfigBase {

            public Lormyte() {
                dimWhiteList.put("1", new int[] { 80, 120, 35, 52 });
            }

            @Override
            public GeneratorBuilder<WorldGenerator> getGeneratorBuilder() {
                return ((world, biome, config) -> new WorldGenMinable(
                        ModBlocks.LORMYTE_CRYSTAL_BLOCK.get().getDefaultState(),
                        config.count(), input -> input != null && input.getBlock() == Blocks.END_STONE));
            }
        }

        public static class EndMagma extends WorldGenConfigBase {

            public EndMagma() {
                dimWhiteList.put("1", new int[] { 80, 50, 0, 22 });
            }

            @Override
            public GeneratorBuilder<WorldGenerator> getGeneratorBuilder() {
                return ((world, biome, config) -> new WorldGenMinable(ModBlocks.END_MAGMA_BLOCK.get().getDefaultState(),
                        config.count(), BlockMatcher.forBlock(Blocks.END_STONE)));
            }
        }

        public static class EntropyEndStone extends WorldGenConfigBase {

            public EntropyEndStone() {
                dimWhiteList.put("1", new int[] { 60, 30, 0, 22 });
            }

            @Override
            public GeneratorBuilder<WorldGenerator> getGeneratorBuilder() {
                return ((world, biome, config) -> new WorldGenMinable(
                        ModBlocks.END_STONE_ENTROPY_BLOCK.get().getDefaultState(), config.count(),
                        BlockMatcher.forBlock(Blocks.END_STONE)));
            }
        }

        public static class EndCoral extends WorldGenConfigBase {

            public EndCoral() {
                dimWhiteList.put("1", new int[] { 0, 20, 50, 90 });
                weight = -10;
            }

            @Override
            public GeneratorBuilder<WorldGenerator> getGeneratorBuilder() {
                return ((world, biome, config) -> new SurfaceGenerator(config, ModBlocks.END_CORAL.get()));
            }
        }

        public static class EndFlower extends WorldGenConfigBase {

            public EndFlower() {
                dimWhiteList.put("1", new int[] { 5, 6, 50, 90 });
                weight = -10;
            }

            @Override
            public GeneratorBuilder<WorldGenerator> getGeneratorBuilder() {
                return ((world, biome, config) -> new SurfaceGenerator(config, ModBlocks.ENDER_FLOWER_CROP.get()) {

                    @Override
                    public IBlockState getState(World world, BlockPos pos, Random random) {
                        return block.getDefaultState().withProperty(BlockEnderCrop.AGE,
                                random.nextInt(BlockEnderCrop.AGE.getAllowedValues().size() - 1));
                    }
                });
            }
        }

        public void registerToHandler(final WorldGenHandler handler) {
            Stream.of(essenceOre, tungstenOre, tungstenEndOre, lormyte, endMagma, entropyEndStone, endCoral, endFlower)
                    .filter(genConfigBase -> genConfigBase.enableGeneration)
                    .forEach(genConfigBase -> handler.registerGenericGenerator(genConfigBase.buildGenConfig()));
        }
    }

    public static final WorldGenStructureConfig WORLD_GEN_STRUCTURE_CONFIG = new WorldGenStructureConfig();

    public static class WorldGenStructureConfig {

        public final WorldGenConfigBase endRuins = new EndRuins();

        public final WorldGenConfigBase endIslands = new EndIslands();

        public final WorldGenConfigBase observatory = new Observatory();

        public static class EndRuins extends WorldGenConfigBase {

            public EndRuins() {
                biomeWhiteList.put("minecraft:sky", new int[0]);

                defaultDimConfig = new int[] { 600, 1, 85, 100 };
            }

            @Override
            public GeneratorBuilder<WorldGenerator> getGeneratorBuilder() {
                return ((world, biome, config) -> new WorldGenStructure("end_ruins", config));
            }
        }

        public static class EndIslands extends WorldGenConfigBase {

            public EndIslands() {
                biomeWhiteList.put("sky", new int[0]);
                biomeWhiteList.put("plains", new int[0]);
                biomeWhiteList.put("desert", new int[0]);
                biomeWhiteList.put("ocean", new int[0]);
                biomeWhiteList.put("deep_ocean", new int[0]);
                biomeWhiteList.put("savanna", new int[0]);

                defaultDimConfig = new int[] { 600, 1, 90, 115 };
            }

            @Override
            public GeneratorBuilder<WorldGenerator> getGeneratorBuilder() {
                return ((world, biome, config) -> new WorldGenStructure("end_island", config));
            }
        }

        public static class Observatory extends WorldGenConfigBase {

            public Observatory() {
                biomeWhiteList.put("desert", new int[0]);
                biomeWhiteList.put("ocean", new int[0]);
                biomeWhiteList.put("deep_ocean", new int[0]);
                biomeWhiteList.put("forest", new int[0]);
                biomeWhiteList.put("birch_forest", new int[0]);
                biomeWhiteList.put("swampland", new int[0]);

                defaultDimConfig = new int[] { 600, 1, 3, 3 };
            }

            @Override
            public GeneratorBuilder<WorldGenerator> getGeneratorBuilder() {
                return ((world, biome, config) -> new WorldGenStructure("observ", config));
            }
        }

        public void registerToHandler(final WorldGenHandler handler) {
            Stream.of(endRuins, endIslands, observatory)
                    .filter(genConfigBase -> genConfigBase.enableGeneration)
                    .forEach(genConfigBase -> handler.registerStructureGenerators(genConfigBase.buildGenConfig()));
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
        public int entropyWandUseDamage = 2;

        @Config.Name("Show broken textures in jei for the EntropyWand")
        @Config.RequiresMcRestart
        public boolean entropyWandRenderBrokenTextures = false;

        @Config.Name("New Villagers")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn" })
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

    public abstract static class WorldGenConfigBase {

        @Config.Name("Enable generator")
        @Config.RequiresMcRestart
        public boolean enableGeneration = true;

        @Config.Name("Generator weight")
        @Config.RequiresMcRestart
        public int weight = 0;

        @Config.Name("Dim white list")
        @Config.RequiresMcRestart
        @Config.Comment({
                "Key: dim id",
                "Value: dimension config, if empty the default will be used",
                "   [0]: rarity, a higher number will increase the rarity(0 == always spawning)",
                "   [1]: maximum count per-chunk",
                "   [2]: min height",
                "   [3]: max height" })
        public Map<String, int[]> dimWhiteList = new Object2ObjectOpenHashMap<>(1);

        @Config.Name("Dim black list")
        @Config.RequiresMcRestart
        @Config.Comment({ "Dim id" })
        public String[] dimBlackList = new String[0];

        @Config.Name("Biome white list")
        @Config.RequiresMcRestart
        @Config.Comment({
                "Key: Biome id (minecraft:river, minecraft:sky)",
                "Value: dimension config, if empty the default will be used",
                "   [0]: rarity, a higher number will increase the rarity(0 == always spawning)",
                "   [1]: maximum count per-chunk",
                "   [2]: min height",
                "   [3]: max height" })
        public Map<String, int[]> biomeWhiteList = new Object2ObjectOpenHashMap<>(1);

        @Config.Name("Biome black list")
        @Config.RequiresMcRestart
        @Config.Comment({ "Biome id (minecraft:river, minecraft:sky)" })
        public String[] biomeBlackList = new String[0];

        @Config.Name("Default dimension config")
        @Config.RequiresMcRestart
        @Config.Comment({
                "[0]: rarity, a higher number will increase the rarity(0 == always spawning)",
                "[1]: maximum count per-chunk",
                "[2]: min height",
                "[3]: max height" })
        public int[] defaultDimConfig = new int[0];

        public abstract <G extends WorldGenerator> GeneratorBuilder<G> getGeneratorBuilder();

        public <G extends WorldGenerator> GenConfig<G> buildGenConfig() {
            GenConfig.Builder<G> gen = GenConfig.builder();
            gen.setGeneratorName(getClass().getSimpleName())
                    .setGeneratorWeight(weight)
                    .setGeneratorBuilder(getGeneratorBuilder());
            parseBiomes(gen);
            parseDims(gen);
            parseDefaultDimConfig(gen);
            return gen.build();
        }

        public <G extends WorldGenerator> void parseDefaultDimConfig(GenConfig.Builder<G> gen) {
            var dimConfig = parseDimConfig(defaultDimConfig);
            gen.setDefaultDimConfig(dimConfig);
        }

        public <G extends WorldGenerator> void parseDims(GenConfig.Builder<G> gen) {
            for (String dim : dimBlackList) {
                final Integer dimID = resolveDimID(dim);
                if (Objects.isNull(dimID)) continue;
                gen.addDimToBlackList(dimID);
            }

            for (var configEntry : dimWhiteList.entrySet()) {
                final Integer dimID = resolveDimID(configEntry.getKey());
                if (Objects.isNull(dimID)) continue;
                gen.addDimToWhiteList(dimID, parseDimConfig(configEntry.getValue()));
            }
        }

        @Nullable
        public DimConfig parseDimConfig(int[] dimConfigArr) {
            if (dimConfigArr.length != 4) return null;

            final DimConfig.Builder dimConfig;
            try {
                dimConfig = DimConfig.builder()
                        .setRarity(dimConfigArr[0])
                        .setCount(dimConfigArr[1])
                        .setMinHeight(dimConfigArr[2])
                        .setMaxHeight(dimConfigArr[3]);
            } catch (ArrayIndexOutOfBoundsException e) {
                EndReforked.LOGGER.error("Missing parameter for generator: {}", getClass().getSimpleName());
                EndReforked.LOGGER.error(e);
                return null;
            }
            return dimConfig.build();
        }

        @Nullable
        public Integer resolveDimID(String dim) {
            final int dimID;
            try {
                dimID = Integer.parseInt(dim);
            } catch (NumberFormatException e) {
                EndReforked.LOGGER.error("Unable to parse dim id for generator: {}", getClass().getSimpleName());
                EndReforked.LOGGER.error(e);
                return null;
            }

            return dimID;
        }

        public <G extends WorldGenerator> void parseBiomes(GenConfig.Builder<G> gen) {
            for (String biome : biomeBlackList) {
                final var res = resolveBiome(biome);
                if (Objects.isNull(res)) continue;
                gen.addBiomeToBlockList(res);
            }

            for (Map.Entry<String, int[]> entry : biomeWhiteList.entrySet()) {
                final var biomeRes = resolveBiome(entry.getKey());
                if (Objects.isNull(biomeRes)) continue;
                var dimRes = parseDimConfig(entry.getValue());
                gen.addBiomeToWhiteList(biomeRes, dimRes);
            }
        }

        @Nullable
        public Biome resolveBiome(String biome) {
            var resBiome = Biome.REGISTRY.getObject(new ResourceLocation(biome));
            if (Objects.isNull(resBiome)) {
                EndReforked.LOGGER.warn("Unable to find biome `{}`", biome);
                return null;
            }
            return resBiome;
        }
    }
}
