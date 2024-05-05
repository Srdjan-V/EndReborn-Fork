package io.github.srdjanv.endreforked.api.entropy.storage;

public interface WeekEntropyStorage extends EntropyStorage {
    int genDecayFrequency();
    int getDecay();
}
