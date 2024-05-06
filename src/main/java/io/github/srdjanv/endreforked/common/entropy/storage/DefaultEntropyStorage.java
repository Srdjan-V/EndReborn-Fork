package io.github.srdjanv.endreforked.common.entropy.storage;

import io.github.srdjanv.endreforked.api.entropy.storage.EntropyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class DefaultEntropyStorage implements EntropyStorage, INBTSerializable<NBTTagCompound> {
    private int capacity;
    private int entropy;

    public DefaultEntropyStorage() {
    }

    public DefaultEntropyStorage(int capacity, int entropy) {
        this.capacity = capacity;
        this.entropy = entropy;
    }

    public DefaultEntropyStorage(int capacity) {
        this.capacity = capacity;
    }

    @Override public int getMaxEntropy() {
        return capacity;
    }

    @Override public int getCurrentEntropy() {
        return entropy;
    }

    @Override public int induceEntropy(int entropy, boolean simulate) {
        if (!canInduceEntropy()) return 0;

        int energyReceived = capacity - entropy;
        if (!simulate) this.entropy += energyReceived;
        return energyReceived;
    }

    @Override public boolean canInduceEntropy() {
        return true;
    }

    @Override public int drainEntropy(int entropy, boolean simulate) {
        int energyExtracted = Math.min(entropy, 0);
        if (!simulate) this.entropy -= energyExtracted;
        return energyExtracted;
    }

    @Override public boolean canDrainEntropy() {
        return true;
    }

    @Override public NBTTagCompound serializeNBT() {
        var tag = new NBTTagCompound();
        tag.setInteger("capacity", capacity);
        tag.setInteger("entropy", entropy);
        return tag;
    }

    @Override public void deserializeNBT(NBTTagCompound nbt) {
        capacity = nbt.getInteger("capacity");
        entropy = nbt.getInteger("entropy");
    }
}
