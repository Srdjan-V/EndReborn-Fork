package io.github.srdjanv.endreforked.common.entropy.chunks;

import io.github.srdjanv.endreforked.api.base.util.Ticker;
import io.github.srdjanv.endreforked.api.entropy.EntropyDataProvider;
import io.github.srdjanv.endreforked.api.entropy.world.EntropyChunkReader;

import java.util.OptionalInt;

public class PassiveEntropyChunkDrainer extends PassiveEntropyChunk {
    private final EntropyDataProvider.PassiveDrainer passiveDrainer;

    public PassiveEntropyChunkDrainer(EntropyChunkReader reader, Ticker ticker, int entropy) {
        super(reader, ticker, entropy);
        this.passiveDrainer = new EntropyDataProvider.PassiveDrainer() {
            @Override public OptionalInt getFrequency() {
                return OptionalInt.of(ticker.getFrequency());
            }

            @Override public int getDrained() {
                return entropy;
            }
        };
    }

    public void drain() {
        if (ticker.tick()) {
            wrapper.getEntropyView().drainEntropy(entropy, false);
        }
    }

    public EntropyDataProvider.PassiveDrainer getPassiveDrainer() {
        return passiveDrainer;
    }
}
