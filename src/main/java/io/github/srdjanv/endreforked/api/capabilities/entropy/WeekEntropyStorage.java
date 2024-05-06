package io.github.srdjanv.endreforked.api.capabilities.entropy;

public interface WeekEntropyStorage extends EntropyStorage {
    /**
     * Each inversion/extraction will decrease entropy in the storage
     */
    int getDecay();
    void setDecay(int decay);
}
