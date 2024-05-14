package io.github.srdjanv.endreforked.common.items;

import io.github.srdjanv.endreforked.api.entropy.EntropyRadius;
import io.github.srdjanv.endreforked.api.entropy.IEntropyWings;
import io.github.srdjanv.endreforked.common.items.base.ItemBase;

public class ItemEntropyWings extends ItemBase implements IEntropyWings {
    public ItemEntropyWings() {
        super("entropy_wings");
    }
    public ItemEntropyWings(String name) {
        super(name);
    }

    @Override public int getEntropyCost() {
        return 250;
    }

    @Override public EntropyRadius getEntropyRange() {
        return EntropyRadius.TWO;
    }

    @Override public int getFlightDuration() {
        return 25 * 20;
    }
}
