package io.github.srdjanv.endreforked.api.worldgen;

import java.util.Objects;

public class DimConfig {

    private final int minHeight;
    private final int maxHeight;
    private final int rarity;
    private final int amountModifier;

    private DimConfig(int minHeight, int maxHeight, int rarity, int amountModifier) {
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.rarity = rarity;
        this.amountModifier = amountModifier;
    }

    public int minHeight() {
        return minHeight;
    }

    public int maxHeight() {
        return maxHeight;
    }

    public int rarity() {
        return rarity;
    }

    public int amountModifier() {
        return amountModifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DimConfig dimConfig = (DimConfig) o;
        return minHeight == dimConfig.minHeight && maxHeight == dimConfig.maxHeight && rarity == dimConfig.rarity &&
                amountModifier == dimConfig.amountModifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minHeight, maxHeight, rarity, amountModifier);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Integer minHeight;
        private Integer maxHeight;
        private Integer rarity;
        private Integer amountModifier;

        private Builder() {}

        public Builder setMinHeight(int minHeight) {
            this.minHeight = minHeight;
            return this;
        }

        public Builder setMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
            return this;
        }

        public Builder setRarity(int rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder setAmountModifier(int amountModifier) {
            this.amountModifier = amountModifier;
            return this;
        }

        public DimConfig build() {
            return new DimConfig(
                    Objects.requireNonNull(minHeight),
                    Objects.requireNonNull(maxHeight),
                    Objects.requireNonNull(rarity),
                    Objects.requireNonNull(amountModifier));
        }
    }
}
