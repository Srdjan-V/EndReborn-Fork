package io.github.srdjanv.endreforked.common.entropy.chunks;

import java.util.concurrent.ThreadLocalRandom;

public class EntropyChunkInducer<D> {
    private final EntropyChunkDataWrapper<D> wrapper;
    private final int frequency;
    private final int entropy;
    private int tick;

    public EntropyChunkInducer(EntropyChunkDataWrapper<D> wrapper, int frequency, int entropy) {
        this.wrapper = wrapper;
        this.frequency = frequency;
        this.entropy = entropy;
        tick = ThreadLocalRandom.current().nextInt(frequency);
    }

    public void induce(D data) {
        if (++tick % frequency != 0) return;
        wrapper.getEntropyView(data).induceEntropy(entropy, false);
    }
}
