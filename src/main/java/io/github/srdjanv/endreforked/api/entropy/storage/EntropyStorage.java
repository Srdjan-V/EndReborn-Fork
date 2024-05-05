package io.github.srdjanv.endreforked.api.entropy.storage;

public interface EntropyStorage {
    int getMaxEntropy();

    int getMinEntropy();

    int getCurrentEntropy();

    /**
     * @param entropy  Entropy amount to induce.
     * @param simulate If false, the fill will only be simulated.
     * @return Amount of entropy that cant be induced.
     */
    int induceEntropy(int entropy, boolean simulate);

    /**
     * Drains entropy
     *
     * @param entropy  Maximum amount of entropy to drain.
     * @param simulate If false, drain will only be simulated.
     * @return The amount that was (or would have been, if
     * simulated) drained.
     */
    int drainEntropy(int entropy, boolean simulate);
}
