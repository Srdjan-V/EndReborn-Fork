package endreborn.api.worldgen;

import java.util.Objects;

public class DimConfig {

    private final int minHeight;
    private final int maxHeight;
    private final int rarity;
    private final int count;

    private DimConfig(int minHeight, int maxHeight, int rarity, int count) {
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.rarity = rarity;
        this.count = count;
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

    public int count() {
        return count;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Integer minHeight;
        private Integer maxHeight;
        private Integer rarity;
        private Integer count;

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

        public Builder setCount(int count) {
            this.count = count;
            return this;
        }

        public DimConfig build() {
            return new DimConfig(
                    Objects.requireNonNull(minHeight),
                    Objects.requireNonNull(maxHeight),
                    Objects.requireNonNull(rarity),
                    Objects.requireNonNull(count));
        }
    }
}
