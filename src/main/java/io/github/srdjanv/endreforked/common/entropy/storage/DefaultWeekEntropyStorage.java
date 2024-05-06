package io.github.srdjanv.endreforked.common.entropy.storage;

import io.github.srdjanv.endreforked.api.entropy.storage.WeekEntropyStorage;
import net.minecraft.nbt.NBTTagCompound;

public class DefaultWeekEntropyStorage extends DefaultEntropyStorage implements WeekEntropyStorage {
    private int decay;

    public DefaultWeekEntropyStorage(int capacity, int entropy, int decay) {
        super(capacity, entropy);
        this.decay = decay;
    }

    public DefaultWeekEntropyStorage(int capacity, int decay) {
        super(capacity);
        this.decay = decay;
    }

    @Override public int induceEntropy(int entropy, boolean simulate) {
        var ind = super.induceEntropy(entropy, simulate);
        if (!simulate) super.drainEntropy(decay, false);
        return ind;
    }

    @Override public int drainEntropy(int entropy, boolean simulate) {
        var drn = super.drainEntropy(entropy, simulate);
        if (!simulate) super.drainEntropy(decay, false);
        return drn;
    }

    @Override public int getDecay() {
        return decay;
    }

    @Override public void setDecay(int decay) {
        this.decay = decay;
    }

    @Override public NBTTagCompound serializeNBT() {
        var tag = super.serializeNBT();
        tag.setInteger("decay", decay);
        return tag;
    }

    @Override public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        decay = nbt.getInteger("decay");
    }
}
