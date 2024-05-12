package io.github.srdjanv.endreforked.common.entropy.chunks;

import io.github.srdjanv.endreforked.api.entropy.IEntropyDataProvider;

public class PassiveEntropyChunkInducer<D>  extends PassiveEntropyChunkHandler<D>{
    private final IEntropyDataProvider.PassiveInducer passiveInducer;


    public PassiveEntropyChunkInducer(D data, EntropyChunkDataWrapper<D> wrapper, int frequency, int entropy) {
        super(data, wrapper, frequency, entropy);
        passiveInducer = new IEntropyDataProvider.PassiveInducer() {

            @Override public int getInduced() {
                return entropy;
            }

            @Override public int getFrequency() {
                return frequency;
            }
        };
    }

    public void induce() {
        induce(data);
    }

    public void induce(D data) {
        if (++tick % frequency != 0) return;
        wrapper.getEntropyView(data).induceEntropy(entropy, false);
    }

    public IEntropyDataProvider.PassiveInducer getPassiveInducer() {
        return passiveInducer;
    }
}
