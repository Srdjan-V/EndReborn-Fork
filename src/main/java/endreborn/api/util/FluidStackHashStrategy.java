package endreborn.api.util;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Suppliers;

import it.unimi.dsi.fastutil.Hash;

public interface FluidStackHashStrategy extends Hash.Strategy<FluidStack> {

    /**
     * @return a builder object for producing a custom ItemStackHashStrategy.
     */
    static Builder builder() {
        return new Builder();
    }

    Supplier<FluidStackHashStrategy> memorizedComparingAll = Suppliers.memoize(FluidStackHashStrategy::comparingAll);

    static FluidStackHashStrategy memorizedComparingAll() {
        return memorizedComparingAll.get();
    }

    /**
     * Generates an ItemStackHash configured to compare every aspect of ItemStacks.
     *
     * @return the ItemStackHashStrategy as described above.
     */
    static FluidStackHashStrategy comparingAll() {
        return builder().compareFuild(true)
                .compareAmount(true)
                .compareTag(true)
                .build();
    }

    Supplier<FluidStackHashStrategy> memorizedComparingAllButAmount = Suppliers
            .memoize(FluidStackHashStrategy::comparingAllButAmount);

    static FluidStackHashStrategy memorizedComparingAllButAmount() {
        return memorizedComparingAllButAmount.get();
    }

    /**
     * Generates an ItemStackHash configured to compare every aspect of ItemStacks except the number
     * of items in the stack.
     *
     * @return the ItemStackHashStrategy as described above.
     */
    static FluidStackHashStrategy comparingAllButAmount() {
        return builder().compareFuild(true)
                .compareTag(true)
                .build();
    }

    class Builder {

        private boolean fluid, amount, tag;
        private BiPredicate<FluidStack, FluidStack> customCheck;

        private Builder() {}

        /**
         * Defines whether the Item type should be considered for equality.
         *
         * @param choice {@code true} to consider this property, {@code false} to ignore it.
         * @return {@code this}
         */
        public Builder compareFuild(boolean choice) {
            fluid = choice;
            return this;
        }

        /**
         * Defines whether stack size should be considered for equality.
         *
         * @param choice {@code true} to consider this property, {@code false} to ignore it.
         * @return {@code this}
         */
        public Builder compareAmount(boolean choice) {
            amount = choice;
            return this;
        }

        /**
         * Defines whether NBT Tags should be considered for equality.
         *
         * @param choice {@code true} to consider this property, {@code false} to ignore it.
         * @return {@code this}
         */
        public Builder compareTag(boolean choice) {
            tag = choice;
            return this;
        }

        public Builder setCustomCheck(BiPredicate<FluidStack, FluidStack> customCheck) {
            this.customCheck = customCheck;
            return this;
        }

        public Builder addCustomCheck(BiPredicate<FluidStack, FluidStack> customCheck) {
            if (Objects.nonNull(this.customCheck)) {
                this.customCheck = this.customCheck.and(customCheck);
            } else this.customCheck = customCheck;
            return this;
        }

        public FluidStackHashStrategy build() {
            return new FluidStackHashStrategy() {

                @Override
                public int hashCode(@Nullable FluidStack o) {
                    return o == null || o.amount == 0 ? 0 : Objects.hash(
                            fluid ? o.getFluid() : null,
                            amount ? o.amount : null,
                            tag ? o.tag : null);
                }

                @Override
                public boolean equals(@Nullable FluidStack a, @Nullable FluidStack b) {
                    if (a == null || a.amount == 0) return b == null || b.amount == 0;
                    if (b == null || b.amount == 0) return false;

                    return (!Objects.nonNull(customCheck) || customCheck.test(a, b)) &&
                            (!fluid || a.getFluid() == b.getFluid() &&
                                    (!amount || a.amount == b.amount) &&
                                    (!tag || Objects.equals(a.tag, b.tag)));
                }
            };
        }
    }
}
