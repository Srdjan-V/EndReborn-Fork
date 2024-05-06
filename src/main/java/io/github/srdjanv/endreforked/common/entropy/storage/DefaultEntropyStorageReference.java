package io.github.srdjanv.endreforked.common.entropy.storage;

import io.github.srdjanv.endreforked.api.capabilities.entropy.EntropyStorage;
import io.github.srdjanv.endreforked.api.entropy.storage.EntropyStorageReference;
import org.jetbrains.annotations.Nullable;

public class DefaultEntropyStorageReference implements EntropyStorageReference {
    private EntropyStorage reference;

    @Override public @Nullable EntropyStorage getEntropyStorageReference() {
        return reference;
    }

    @Override public boolean setEntropyStorageReference(EntropyStorage reference, boolean force) {
        if (reference != null && this.reference == reference) return true;
        if (this.reference == null || force) {
            this.reference = reference;
            return true;
        }
        return false;
    }
}
