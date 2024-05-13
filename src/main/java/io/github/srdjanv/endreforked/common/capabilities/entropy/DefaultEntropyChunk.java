package io.github.srdjanv.endreforked.common.capabilities.entropy;

import io.github.srdjanv.endreforked.api.capabilities.entropy.EntropyStorage;
import io.github.srdjanv.endreforked.api.capabilities.entropy.EntropyChunk;
import io.github.srdjanv.endreforked.common.entropy.storage.DefaultEntropyStorageReference;
import io.github.srdjanv.endreforked.common.entropy.storage.DefaultWeakEntropyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

import java.util.Optional;

public class DefaultEntropyChunk implements EntropyChunk {
    private final ChunkPos chunkPos;
    private final DefaultWeakEntropyStorage storage;
    private final DefaultEntropyStorageReference storageReference;

    public DefaultEntropyChunk(Chunk chunk) {
        this.chunkPos = chunk.getPos();
        var rand = chunk.getWorld().rand;
        storage = new DefaultWeakEntropyStorage(
                Math.max(1000, rand.nextInt(1200)),
                Math.max(5, rand.nextInt(10)));
        storageReference = new DefaultEntropyStorageReference();
    }

    @Override public Optional<EntropyStorage> getEntropyStorageReference() {
        return storageReference.getEntropyStorageReference();
    }

    @Override public boolean setEntropyStorageReference(EntropyStorage reference, boolean force) {
        return storageReference.setEntropyStorageReference(reference, force);
    }

    @Override
    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    @Override public double getLoadFactor() {
        return storage.getLoadFactor();
    }

    @Override public void setLoadFactor(double loadFactor) {
        storage.setLoadFactor(loadFactor);
    }

    @Override public boolean isOverLoaded() {
        return storage.isOverLoaded();
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
        int ind = storage.induceEntropy(entropy, simulate);
        var ref = storageReference.getEntropyStorageReference();
        if (ref.isPresent() && entropy > ind) {
            ind += ref.get().induceEntropy(entropy - ind, simulate);
        }
        return ind;
    }

    @Override public int drainEntropy(int entropy, boolean simulate) {
        int drain = storage.drainEntropy(entropy, simulate);
        var ref = storageReference.getEntropyStorageReference();
        if (ref.isPresent() && entropy > drain) {
            drain += ref.get().drainEntropy(entropy - drain, simulate);
        }
        return drain;
    }

    @Override public NBTTagCompound serializeNBT() {
        return storage.serializeNBT();
    }

    @Override public void deserializeNBT(NBTTagCompound nbt) {
        storage.deserializeNBT(nbt);
    }

    @Override public Optional<EntropyStorage> getEntropyStorage() {
        return Optional.of(storage);
    }
}
