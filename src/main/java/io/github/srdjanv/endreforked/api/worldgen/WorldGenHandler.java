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
            GameRegistry.registerWorldGenerator(
                    (random, chunkX, chunkZ, world, chunkGenerator, chunkProvider) -> instance.mutate(),
                    Integer.MIN_VALUE);
        }
        return instance;
    }

    private final Set<GenConfig<? extends WorldGenerator>> genericGenerators = new ObjectLinkedOpenHashSet<>();
    private final Set<GenConfig<? extends WorldGenerator>> unmodifiableGenericGenerators = Collections
            .unmodifiableSet(genericGenerators);

    private final Set<GenConfig<? extends WorldGenerator>> structureGenerators = new ObjectLinkedOpenHashSet<>();
    private final Set<GenConfig<? extends WorldGenerator>> unmodifiableStructureGenerators = Collections
            .unmodifiableSet(structureGenerators);
    private final List<Runnable> mutators = new ObjectArrayList<>();

    private void mutate() {
        mutators.forEach(Runnable::run);
        mutators.clear();
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

    public void registerGenericGenerator(GenConfig<? extends WorldGenerator> genConfig) {
        mutators.add(() -> genericGenerators.add(genConfig));
    }

    public void unregisterGenericGenerator(GenConfig<? extends WorldGenerator> genConfig) {
        mutators.add(() -> genericGenerators.remove(genConfig));
    }

    public void unregisterGenericGenerator(Predicate<GenConfig<? extends WorldGenerator>> filter) {
        mutators.add(() -> genericGenerators.removeIf(filter));
    }

    public <G extends WorldGenerator> void registerStructureGenerators(GenConfig<G> genConfig) {
        mutators.add(() -> structureGenerators.add(genConfig));
    }

    public <G extends WorldGenerator> void unregisterStructureGenerators(GenConfig<G> genConfig) {
        mutators.add(() -> structureGenerators.add(genConfig));
    }

    public <G extends WorldGenerator> void unregisterStructureGenerators(Predicate<GenConfig<? extends WorldGenerator>> filter) {
        mutators.add(() -> structureGenerators.removeIf(filter));
    }

    @Unmodifiable
    public Set<GenConfig<? extends WorldGenerator>> getGenericGenerators() {
        return unmodifiableGenericGenerators;
    }

    @Unmodifiable
    public Set<GenConfig<? extends WorldGenerator>> getStructureGenerators() {
        return unmodifiableStructureGenerators;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
                         IChunkProvider chunkProvider) {
        final var biome = world.getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8));
        for (var gen : getApplicableGeneratorsForChunk(genericGenerators, world, biome))
            runChunkGenerator(gen, biome, world, random, chunkX, chunkZ);

        for (var gen : getApplicableGeneratorsForChunk(structureGenerators, world, biome))
            runChunkGenerator(gen, biome, world, random, chunkX, chunkZ);
    }

    private <G extends WorldGenerator> void runChunkGenerator(GenConfig<G> config, Biome biome, World world,
                                                              Random rand, int chunkX, int chunkZ) {
        final var dimConfig = config.getDimConfig(world.provider.getDimension(), biome);
        if (Objects.isNull(dimConfig)) return;
        final G gen = config.getGenerator(world, biome, dimConfig);
        if (Objects.isNull(gen)) return;
        if (dimConfig.rarity() >= 1 && rand.nextInt(dimConfig.rarity()) != 0) return;

        int heightDiff = dimConfig.maxHeight() - dimConfig.minHeight();
        var pos = new BlockPos(chunkX * 16, dimConfig.minHeight() + rand.nextInt(Math.max(1, heightDiff)), chunkZ * 16);
        if (gen instanceof WorldGenMinable) {
            gen.generate(world, rand, pos.add(8, 0, 8));
        } else gen.generate(world, rand, pos);
    }

    public static Set<GenConfig<? extends WorldGenerator>> getApplicableGeneratorsForChunk(
                                                                                           Set<GenConfig<? extends WorldGenerator>> generatorMap,
                                                                                           World world, Biome biome) {
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
