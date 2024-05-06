package io.github.srdjanv.endreforked.common.capabilities.entropy;

import io.github.srdjanv.endreforked.api.entropy.storage.WeekEntropyStorage;
import io.github.srdjanv.endreforked.common.entropy.storage.DefaultWeekEntropyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;

public class ChunkEntropy implements INBTSerializable<NBTTagCompound>, WeekEntropyStorage {
    private final ChunkPos chunkPos;
    private final DefaultWeekEntropyStorage storage;

    public ChunkEntropy(Chunk chunk) {
        this.chunkPos = chunk.getPos();
        var rand = chunk.getWorld().rand;
        storage = new DefaultWeekEntropyStorage(
                Math.max(1000, rand.nextInt(1200)),
                Math.max(5, rand.nextInt(10)));
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    @Override public int getDecay() {
        return storage.getDecay();
    }

    @Override public void setDecay(int decay) {
        storage.setDecay(decay);
    }

    @Override public int getMaxEntropy() {
        return storage.getMaxEntropy();
    }

    @Override public int getCurrentEntropy() {
        return storage.getCurrentEntropy();
    }

    @Override public int induceEntropy(int entropy, boolean simulate) {
        return storage.induceEntropy(entropy, simulate);
    }

    @Override public int drainEntropy(int entropy, boolean simulate) {
        return storage.drainEntropy(entropy, simulate);
    }

    @Override public NBTTagCompound serializeNBT() {
        return storage.serializeNBT();
    }

    @Override public void deserializeNBT(NBTTagCompound nbt) {
        storage.deserializeNBT(nbt);
    }

    @Override public String toString() {
        return "ChunkEntropy{" +
                "maxEntropy=" + storage.getMaxEntropy() +
                ", currentEntropy=" + storage.getCurrentEntropy() +
                ", decay=" + storage.getDecay() +
                '}';
    }
}
