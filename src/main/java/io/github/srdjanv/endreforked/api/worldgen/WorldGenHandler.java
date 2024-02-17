package io.github.srdjanv.endreforked.api.worldgen;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import io.github.srdjanv.endreforked.EndReforked;
import it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.jetbrains.annotations.Unmodifiable;

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

    private final Set<GenConfig> oreGenerators = new ObjectAVLTreeSet<>();
    private final Set<GenConfig> genericGenerators = new ObjectAVLTreeSet<>();
    private final Set<GenConfig> structureGenerators = new ObjectAVLTreeSet<>();
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
            oreGenerators.clear();
            genericGenerators.clear();
            structureGenerators.clear();
        });
    }

    public void registerOreGenerator(GenConfig genConfig) {
        mutators.add(() -> {
            if (!oreGenerators.add(genConfig)) {
                EndReforked.LOGGER.warn("Ignoring GenConfig: '{}'", genConfig.getGeneratorName());
            }
        });
    }

    public void unregisterOreGenerator(GenConfig genConfig) {
        mutators.add(() -> oreGenerators.remove(genConfig));
    }

    public void unregisterOreGenerator(Predicate<GenConfig> filter) {
        mutators.add(() -> oreGenerators.removeIf(filter));
    }


    public void registerGenericGenerator(GenConfig genConfig) {
        mutators.add(() -> {
            if (!genericGenerators.add(genConfig)) {
                EndReforked.LOGGER.warn("Ignoring GenConfig: '{}'", genConfig.getGeneratorName());
            }
        });
    }

    public void unregisterGenericGenerator(GenConfig genConfig) {
        mutators.add(() -> genericGenerators.remove(genConfig));
    }

    public void unregisterGenericGenerator(Predicate<GenConfig> filter) {
        mutators.add(() -> genericGenerators.removeIf(filter));
    }

    public void registerStructureGenerator(GenConfig genConfig) {
        mutators.add(() -> {
            if (!structureGenerators.add(genConfig)) {
                EndReforked.LOGGER.warn("Ignoring GenConfig: '{}'", genConfig.getGeneratorName());
            }
        });
    }

    public void unregisterStructureGenerator(GenConfig genConfig) {
        mutators.add(() -> structureGenerators.remove(genConfig));
    }

    public void unregisterStructureGenerator(Predicate<GenConfig> filter) {
        mutators.add(() -> structureGenerators.removeIf(filter));
    }

    private final Set<GenConfig> unmodifiableOreGenerators = Collections
            .unmodifiableSet(oreGenerators);

    @Unmodifiable
    public Set<GenConfig> getOreGenerators() {
        return unmodifiableOreGenerators;
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
        mutate();

        // deterministic worldgen
        final long seed = random.nextLong();
        final var biome = world.getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8));

        for (var gen : getApplicableGeneratorsForChunk(oreGenerators, world, biome)) {
            random.setSeed(seed);
            runChunkGenerator(gen, biome, world, random, chunkX, chunkZ);
        }

        for (var gen : getApplicableGeneratorsForChunk(genericGenerators, world, biome)) {
            random.setSeed(seed);
            runChunkGenerator(gen, biome, world, random, chunkX, chunkZ);
        }

        for (var gen : getApplicableGeneratorsForChunk(structureGenerators, world, biome)) {
            random.setSeed(seed);
            runChunkGenerator(gen, biome, world, random, chunkX, chunkZ);
        }
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
