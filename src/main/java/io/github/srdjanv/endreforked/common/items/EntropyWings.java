package io.github.srdjanv.endreforked.common.items;

import io.github.srdjanv.endreforked.api.entropy.EntropyRange;
import io.github.srdjanv.endreforked.api.entropy.IEntropyWings;
import io.github.srdjanv.endreforked.common.items.base.ItemBase;

public class EntropyWings extends ItemBase implements IEntropyWings {
    public EntropyWings() {
        super("entropy_wings");
    }
    public EntropyWings(String name) {
        super(name);
    }

    @Override public int getEntropyCost() {
        return 250;
    }

    @Override public EntropyRange getEntropyRange() {
        return EntropyRange.TWO;
    }

    @Override public int getFlightDuration() {
        return 25 * 20;
    }
}
