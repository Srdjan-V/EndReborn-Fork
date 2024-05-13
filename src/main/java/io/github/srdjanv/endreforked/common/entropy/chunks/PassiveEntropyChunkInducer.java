package io.github.srdjanv.endreforked.common.entropy.chunks;

import io.github.srdjanv.endreforked.api.base.util.Ticker;
import io.github.srdjanv.endreforked.api.entropy.IEntropyDataProvider;
import io.github.srdjanv.endreforked.api.entropy.world.EntropyChunkReader;

import java.util.OptionalInt;

public class PassiveEntropyChunkInducer extends PassiveEntropyChunk {
    private final IEntropyDataProvider.PassiveInducer passiveInducer;

    public PassiveEntropyChunkInducer(EntropyChunkReader wrapper, Ticker ticker, int entropy) {
        super(wrapper, ticker, entropy);
        passiveInducer = new IEntropyDataProvider.PassiveInducer() {
            @Override public OptionalInt getFrequency() {
                return OptionalInt.of(ticker.getFrequency());
            }

            @Override public int getInduced() {
                return entropy;
            }
        };
    }

    public void induce() {
        if (ticker.tick()) {
            wrapper.getEntropyView().induceEntropy(entropy, false);
        }
    }

    public IEntropyDataProvider.PassiveInducer getPassiveInducer() {
        return passiveInducer;
    }
}
