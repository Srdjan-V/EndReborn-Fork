package io.github.srdjanv.endreforked.common.configs.bioms;

import java.util.Objects;

public class BiomesSchema {
    private final boolean enabled;
    private final int weight;

    private BiomesSchema(boolean enabled, int weight) {
        this.enabled = enabled;
        this.weight = weight;
    }

    private BiomesSchema() {
        this.enabled = false;
        this.weight = 0;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getWeight() {
        return weight;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Boolean enabled;
        private Integer weight;

        private Builder() {
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public BiomesSchema build() {
            return new BiomesSchema(Objects.requireNonNull(enabled), Objects.requireNonNull(weight));
        }
    }
}
