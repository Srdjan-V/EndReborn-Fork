package io.github.srdjanv.endreforked.common.entropy.chunks;

import io.github.srdjanv.endreforked.api.base.util.Ticker;
import io.github.srdjanv.endreforked.api.entropy.EntropyDataProvider;
import io.github.srdjanv.endreforked.api.entropy.world.EntropyChunkReader;

import java.util.OptionalInt;

public class PassiveEntropyChunkInducer extends PassiveEntropyChunk {
    private final EntropyDataProvider.PassiveInducer passiveInducer;

    public PassiveEntropyChunkInducer(EntropyChunkReader wrapper, Ticker ticker, int entropy) {
        super(wrapper, ticker, entropy);
        passiveInducer = new EntropyDataProvider.PassiveInducer() {
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

    public EntropyDataProvider.PassiveInducer getPassiveInducer() {
        return passiveInducer;
    }
}
