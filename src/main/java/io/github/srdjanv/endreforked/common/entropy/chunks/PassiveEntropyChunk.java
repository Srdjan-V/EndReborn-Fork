package io.github.srdjanv.endreforked.common.entropy.chunks;

import io.github.srdjanv.endreforked.api.base.util.Ticker;
import io.github.srdjanv.endreforked.api.entropy.world.EntropyChunkReader;

abstract class PassiveEntropyChunk {
    protected final EntropyChunkReader wrapper;
    protected final Ticker ticker;
    protected final int entropy;

    PassiveEntropyChunk(EntropyChunkReader wrapper, Ticker ticker, int entropy) {
        this.wrapper = wrapper;
        this.ticker = ticker;
        this.entropy = entropy;
    }
}
