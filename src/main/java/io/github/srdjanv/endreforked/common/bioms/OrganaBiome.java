package io.github.srdjanv.endreforked.common.bioms;

import git.jbredwards.nether_api.api.biome.IEndBiome;
import git.jbredwards.nether_api.api.world.INetherAPIChunkGenerator;
import io.github.srdjanv.endreforked.common.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeEnd;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.chunk.ChunkPrimer;

import javax.annotation.Nonnull;

public class OrganaBiome extends BiomeEnd implements IEndBiome {
    public OrganaBiome() {
        super(new BiomeProperties("Organa").setRainDisabled());
        setRegistryName("Organa");

        topBlock = ModBlocks.END_MOSS_BLOCK.get().getDefaultState();
        fillerBlock = Blocks.END_STONE.getDefaultState();
        decorator = new BiomeEndDecorator();
    }

    public void buildSurface(
            @Nonnull final INetherAPIChunkGenerator chunkGenerator,
            final int chunkX, final int chunkZ,
            @Nonnull final ChunkPrimer primer,
            final int x, final int z,
            final double terrainNoise) {
    }


    public static class Decorator extends BiomeEndDecorator {
        public Decorator() {

        }


    }
}
