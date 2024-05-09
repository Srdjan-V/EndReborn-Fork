package io.github.srdjanv.endreforked.api.worldgen;

import java.util.*;

public class GenConfig {
    //Cant use EnumMap, gson type
    private final Map<Modifier, Integer> modifiers;

    @SuppressWarnings("unused")
    private GenConfig() {
        this.modifiers = new EnumMap<>(Modifier.class);
    }

    private GenConfig(EnumMap<Modifier, Integer> modifiers) {
        this.modifiers = new EnumMap<>(modifiers);
    }

    public int maxHeight() {
        return modifier(Modifier.MAX_HEIGHT).orElseThrow(() -> new NoSuchElementException("GenConfig: Max height was not set"));
    }

    public int minHeight() {
        return modifier(Modifier.MIN_HEIGHT).orElseThrow(() -> new NoSuchElementException("GenConfig: Min height was not set"));
    }

    public int rarity() {
        return modifier(Modifier.RARITY).orElseThrow(() -> new NoSuchElementException("GenConfig: Rarity was not set"));
    }

    public int amount() {
        return modifier(Modifier.AMOUNT).orElseThrow(() -> new NoSuchElementException("GenConfig: Amount was not set"));
    }

    public int radius() {
        return modifier(Modifier.RADIUS).orElseThrow(() -> new NoSuchElementException("GenConfig: Radius was not set"));
    }

    public int sphereFillRatio() {
        return modifier(Modifier.SPHERE_FILL_RATIO).orElseThrow(() -> new NoSuchElementException("GenConfig: Sphere fill ratio was not set"));
    }

    public int uniqueGeneratorId() {
        return modifier(Modifier.UNIQUE_GENERATOR_ID).orElseThrow(() -> new NoSuchElementException("GenConfig: Unique generator id was not set"));
    }

    public int spacing() {
        return modifier(Modifier.SPACING).orElseThrow(() -> new NoSuchElementException("GenConfig: Spacing was not set"));
    }

    public int separation() {
        return modifier(Modifier.SEPARATION).orElseThrow(() -> new NoSuchElementException("GenConfig: Separation was not set"));
    }

    public OptionalInt modifier(Modifier modifier) {
        var value = modifiers.get(modifier);
        if (value == null) return OptionalInt.empty();
        return OptionalInt.of(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenConfig genConfig = (GenConfig) o;
        return modifiers.equals(genConfig.modifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modifiers);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final EnumMap<Modifier, Integer> modifiers = new EnumMap<>(Modifier.class);

        private Builder() {}

        public Builder setMinHeight(int minHeight) {
            return setModifier(Modifier.MIN_HEIGHT, minHeight);
        }

        public Builder setMaxHeight(int maxHeight) {
            return setModifier(Modifier.MAX_HEIGHT, maxHeight);
        }

        public Builder setRarity(int rarity) {
            return setModifier(Modifier.RARITY, rarity);
        }

        public Builder setAmount(int amount) {
            return setModifier(Modifier.AMOUNT, amount);
        }

        public Builder setRadius(int radius) {
            return setModifier(Modifier.RADIUS, radius);
        }

        public Builder setSphereFillRatio(int ratio) {
            return setModifier(Modifier.SPHERE_FILL_RATIO, ratio);
        }

        public Builder setUniqueGeneratorId(int uniqueGeneratorId) {
            return setModifier(Modifier.UNIQUE_GENERATOR_ID, uniqueGeneratorId);
        }

        public Builder setSpacing(int spacing) {
            return setModifier(Modifier.SPACING, spacing);
        }

        public Builder setSeparation(int separation) {
            return setModifier(Modifier.SEPARATION, separation);
        }

        public Builder setModifier(Modifier modifier, int rarity) {
            modifiers.put(modifier, rarity);
            return this;
        }

        public GenConfig build() {
            return new GenConfig(modifiers);
        }
    }

}
