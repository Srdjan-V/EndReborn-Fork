package io.github.srdjanv.endreforked.api.worldgen;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.jetbrains.annotations.Unmodifiable;

import io.github.srdjanv.endreforked.common.Configs;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;

public final class WorldGenHandler implements IWorldGenerator {

    private static WorldGenHandler instance;

    public static WorldGenHandler getInstance() {
        if (Objects.isNull(instance)) {
            instance = new WorldGenHandler();
            GameRegistry.registerWorldGenerator(instance, 0);
        }
        return instance;
    }

    private final Set<GenConfig> genericGenerators = new ObjectLinkedOpenHashSet<>();
    private final Set<GenConfig> structureGenerators = new ObjectLinkedOpenHashSet<>();
    private final List<Runnable> mutators = new ObjectArrayList<>();

    private void mutate() {
        if (mutators.isEmpty()) return;
        synchronized (this) {
            mutators.forEach(Runnable::run);
            mutators.clear();
        }
    }

    public void clearGenerators() {
        mutators.add(() -> {
            genericGenerators.clear();
            structureGenerators.clear();
        });
    }

    public void registerGeneratorsFromConfig() {
        mutators.add(() -> {
            Configs.WORLD_ORE_GEN_CONFIG.registerToHandler(this);
            Configs.WORLD_GEN_STRUCTURE_CONFIG.registerToHandler(this);
        });
    }

    public void registerGenericGenerator(GenConfig genConfig) {
        mutators.add(() -> genericGenerators.add(genConfig));
    }

    public void unregisterGenericGenerator(GenConfig genConfig) {
        mutators.add(() -> genericGenerators.remove(genConfig));
    }

    public void unregisterGenericGenerator(Predicate<GenConfig> filter) {
        mutators.add(() -> genericGenerators.removeIf(filter));
    }

    public <G extends WorldGenerator> void registerStructureGenerators(GenConfig genConfig) {
        mutators.add(() -> structureGenerators.add(genConfig));
    }

    public <G extends WorldGenerator> void unregisterStructureGenerators(GenConfig genConfig) {
        mutators.add(() -> structureGenerators.add(genConfig));
    }

    public <G extends WorldGenerator> void unregisterStructureGenerators(Predicate<GenConfig> filter) {
        mutators.add(() -> structureGenerators.removeIf(filter));
    }

    private final Set<GenConfig> unmodifiableGenericGenerators = Collections
            .unmodifiableSet(genericGenerators);

    @Unmodifiable
    public Set<GenConfig> getGenericGenerators() {
        return unmodifiableGenericGenerators;
    }

    private final Set<GenConfig> unmodifiableStructureGenerators = Collections
            .unmodifiableSet(structureGenerators);

    @Unmodifiable
    public Set<GenConfig> getStructureGenerators() {
        return unmodifiableStructureGenerators;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
                         IChunkProvider chunkProvider) {
        // deterministic worldgen
        final long seed = random.nextLong();
        final var biome = world.getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8));

        for (var gen : getApplicableGeneratorsForChunk(genericGenerators, world, biome)) {
            random.setSeed(seed);
            runChunkGenerator(gen, biome, world, random, chunkX, chunkZ);
        }

        for (var gen : getApplicableGeneratorsForChunk(structureGenerators, world, biome)) {
            random.setSeed(seed);
            runChunkGenerator(gen, biome, world, random, chunkX, chunkZ);
        }
        mutate();
    }

    private void runChunkGenerator(GenConfig config, Biome biome, World world,
                                   Random rand, int chunkX, int chunkZ) {
        final var dimConfig = config.getDimConfig(world.provider.getDimension(), biome);
        if (Objects.isNull(dimConfig)) return;
        if (dimConfig.rarity() >= 1 && rand.nextInt(dimConfig.rarity()) != 0) return;
        final var gen = config.getGenerator(world, biome, dimConfig);
        if (Objects.isNull(gen)) return;

        if (gen instanceof WorldGenMinable) {
            int heightDiff = dimConfig.maxHeight() - dimConfig.minHeight();
            var pos = new BlockPos(chunkX * 16, dimConfig.minHeight() + rand.nextInt(Math.max(1, heightDiff)),
                    chunkZ * 16);
            gen.generate(world, rand, pos.add(8, 0, 8));
        } else {
            gen.generate(world, rand, new BlockPos(chunkX * 16, world.getSeaLevel(), chunkZ * 16));
        }
    }

    public static Set<GenConfig> getApplicableGeneratorsForChunk(Set<GenConfig> generatorMap, World world,
                                                                 Biome biome) {
        final var dim = world.provider.getDimension();

        return generatorMap.stream()
                .filter(entry -> {
                    if (!entry.getDimBlackList().isEmpty())
                        if (entry.getDimBlackList().contains(dim))
                            return false;

                    if (!entry.getDimConfigs().isEmpty())
                        return entry.getDimConfigs().containsKey(dim);

                    return true;
                })
                .filter(entry -> {
                    if (!entry.getBiomeBlackList().isEmpty())
                        if (entry.getBiomeBlackList().contains(biome))
                            return false;

                    if (!entry.getBiomeConfigs().isEmpty())
                        return entry.getBiomeConfigs().containsKey(biome);

                    return true;
                })
                .collect(Collectors.toSet());
    }
}
