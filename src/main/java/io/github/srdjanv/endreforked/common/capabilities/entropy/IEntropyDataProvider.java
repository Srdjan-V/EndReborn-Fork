package io.github.srdjanv.endreforked.common.capabilities.entropy;

public interface IEntropyDataProvider {
    default boolean hasPassiveEntropyCost() {
        return true;
    }
    default int getPassiveEntropyCost(){return 0;}

    default boolean hasActiveEntropyCost() {
        return true;
    }
    default int getActiveEntropyCost(){return 0;}

    //todo return translatable component
    default String getFormattedEntropyData() {
        return getActiveEntropyCost() + "%";
    }
}
