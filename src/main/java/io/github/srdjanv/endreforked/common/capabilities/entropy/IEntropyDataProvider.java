package io.github.srdjanv.endreforked.common.capabilities.entropy;

public interface IEntropyDataProvider {
    default boolean hasPassiveEntropyCost() {
        return true;
    }
    int getPassiveEntropyCost();

    default boolean hasActiveEntropyCost() {
        return true;
    }
    int getActiveEntropyCost();

    //todo return translatable component
    default String getFormattedEntropyData() {
        return getActiveEntropyCost() + "%";
    }
}
