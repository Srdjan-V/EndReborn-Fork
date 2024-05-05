package io.github.srdjanv.endreforked.common.capabilities.entropy;

import io.github.srdjanv.endreforked.EndReforked;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;

public class ChunkEntropy implements INBTSerializable<NBTTagCompound> {
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
        maxEntropy = 100;
    }

    public void induceEntropy(int entropy) {
        inducedNumberOfEntropies++;
        entropyIn += entropy;

        if (nextCheck < EndReforked.getWorldTick()) {
            nextCheck = EndReforked.getWorldTick() + 40;
            var newEntropy = Math.max(entropyIn / inducedNumberOfEntropies, currentEntropy - decay);
            currentEntropy = Math.min(Math.max(newEntropy, minEntropy), maxEntropy);
            inducedNumberOfEntropies = 0;
            entropyIn = 0;
        }
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

    public int getMaxEntropy() {
        return maxEntropy;
    }

    public int getMinEntropy() {
        return minEntropy;
    }

    public int getCurrentEntropy() {
        return currentEntropy;
    }

    public int getDecay() {
        return decay;
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
