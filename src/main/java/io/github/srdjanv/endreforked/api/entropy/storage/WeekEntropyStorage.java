package io.github.srdjanv.endreforked.api.entropy.storage;

public interface WeekEntropyStorage extends EntropyStorage {
    int getDecay();
    void setDecay(int decay);
}
