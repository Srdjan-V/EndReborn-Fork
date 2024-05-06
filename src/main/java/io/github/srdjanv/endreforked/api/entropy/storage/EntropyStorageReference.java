package io.github.srdjanv.endreforked.api.entropy.storage;

import io.github.srdjanv.endreforked.api.capabilities.entropy.EntropyStorage;
import org.jetbrains.annotations.Nullable;

public interface EntropyStorageReference {
    default boolean hasEntropyStorageReference() {
        return getEntropyStorageReference() != null;
    }

    @Nullable
    EntropyStorage getEntropyStorageReference();

    boolean setEntropyStorageReference(EntropyStorage reference, boolean force);
}
