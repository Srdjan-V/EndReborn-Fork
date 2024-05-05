package io.github.srdjanv.endreforked.common.capabilities.entropy;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.api.entropy.storage.WeekEntropyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;

public class ChunkEntropy implements INBTSerializable<NBTTagCompound>, WeekEntropyStorage {
    private final ChunkPos chunkPos;

    private int maxEntropy;
    private int minEntropy;
    private int currentEntropy;
    private int decay;
    private int entropyIn;
    private int inducedNumberOfEntropies;
    private int nextCheck;

    public ChunkEntropy(Chunk chunk) {
        this.chunkPos = chunk.getPos();
        maxEntropy = 1000;
    }

    @Override public int induceEntropy(int entropy, boolean simulate) {
        var remainder = maxEntropy - (entropyIn + entropy);
        if (simulate) return remainder;

        inducedNumberOfEntropies++;
        entropyIn += entropy;

        if (nextCheck < EndReforked.getWorldTick()) {
            nextCheck = EndReforked.getWorldTick() + genDecayFrequency();
            var newEntropy = Math.max(entropyIn / inducedNumberOfEntropies, currentEntropy - decay);
            currentEntropy = Math.min(Math.max(newEntropy, minEntropy), maxEntropy);
            inducedNumberOfEntropies = 0;
            entropyIn = 0;
        }
        return remainder;
    }

    @Override public int drainEntropy(int entropy, boolean simulate) {
        if (currentEntropy >= entropy) {
            if (!simulate) this.currentEntropy -= entropy;
            return entropy;
        }
        if (!simulate) this.currentEntropy = 0;
        int missing = this.currentEntropy;
        missing -= entropy;
        return entropy - missing;
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    public void setMaxEntropy(int maxEntropy) {
        this.maxEntropy = maxEntropy;
    }

    public void setMinEntropy(int minEntropy) {
        this.minEntropy = minEntropy;
    }

    public void setDecay(int decay) {
        this.decay = decay;
    }

    @Override public int genDecayFrequency() {
        return 40;
    }

    @Override public int getDecay() {
        return decay;
    }

    @Override public int getMaxEntropy() {
        return maxEntropy;
    }

    @Override public int getMinEntropy() {
        return minEntropy;
    }

    @Override public int getCurrentEntropy() {
        return currentEntropy;
    }

    @Override public NBTTagCompound serializeNBT() {
        var tag = new NBTTagCompound();
        tag.setInteger("maxEntropy", maxEntropy);
        tag.setInteger("minEntropy", minEntropy);
        tag.setInteger("currentEntropy", currentEntropy);
        tag.setInteger("decay", decay);
        return tag;
    }

    @Override public void deserializeNBT(NBTTagCompound nbt) {
        maxEntropy = nbt.getInteger("maxEntropy");
        minEntropy = nbt.getInteger("minEntropy");
        currentEntropy = nbt.getInteger("currentEntropy");
        decay = nbt.getInteger("decay");
    }

    @Override public String toString() {
        return "ChunkEntropy{" +
                "maxEntropy=" + maxEntropy +
                ", minEntropy=" + minEntropy +
                ", currentEntropy=" + currentEntropy +
                ", decay=" + decay +
                '}';
    }
}
