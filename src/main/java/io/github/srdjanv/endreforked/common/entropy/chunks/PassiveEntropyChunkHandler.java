package io.github.srdjanv.endreforked.common.entropy.chunks;

import java.util.concurrent.ThreadLocalRandom;

abstract class PassiveEntropyChunkHandler<D> {
    protected final EntropyChunkDataWrapper<D> wrapper;
    protected final int frequency;
    protected final int entropy;
    protected final D data;
    protected int tick;

    public PassiveEntropyChunkHandler(
            D data, EntropyChunkDataWrapper<D> wrapper,
            int frequency, int entropy) {
        this.wrapper = wrapper;
        this.frequency = frequency;
        this.entropy = entropy;
        tick = ThreadLocalRandom.current().nextInt(frequency);
        this.data = data;
    }
}
