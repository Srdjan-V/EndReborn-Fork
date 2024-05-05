package io.github.srdjanv.endreforked.common.capabilities.entropy;

import org.jetbrains.annotations.Nullable;

public interface IEntropyDataProvider {
    @Nullable
    ChunkEntropy getEntropy();
}
