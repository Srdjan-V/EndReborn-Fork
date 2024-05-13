package io.github.srdjanv.endreforked.common.entropy.chunks;

import io.github.srdjanv.endreforked.api.entropy.IEntropyDataProvider;

import java.util.OptionalInt;

public class PassiveEntropyChunkDrainer<D> extends PassiveEntropyChunkHandler<D> {
    private final IEntropyDataProvider.PassiveDrainer passiveDrainer;

    public PassiveEntropyChunkDrainer(D data, EntropyChunkDataWrapper<D> wrapper, int frequency, int entropy) {
        super(data, wrapper, frequency, entropy);
        this.passiveDrainer = new IEntropyDataProvider.PassiveDrainer() {
            @Override public OptionalInt getFrequency() {
                return OptionalInt.of(frequency);
            }

            @Override public int getDrained() {
                return entropy;
            }
        };
    }

    public void drain() {
        drain(data);
    }

    public void drain(D data) {
        if (++tick % frequency != 0) return;
        wrapper.getEntropyView(data).drainEntropy(entropy, false);
    }

    public IEntropyDataProvider.PassiveDrainer getPassiveDrainer() {
        return passiveDrainer;
    }
}
