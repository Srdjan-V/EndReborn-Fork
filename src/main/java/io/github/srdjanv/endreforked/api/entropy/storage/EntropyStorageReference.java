package io.github.srdjanv.endreforked.api.entropy.storage;

import io.github.srdjanv.endreforked.api.capabilities.entropy.EntropyStorage;

import java.util.Optional;

public interface EntropyStorageReference {
    Optional<EntropyStorage> getEntropyStorageReference();
    boolean setEntropyStorageReference(EntropyStorage reference, boolean force);
}
