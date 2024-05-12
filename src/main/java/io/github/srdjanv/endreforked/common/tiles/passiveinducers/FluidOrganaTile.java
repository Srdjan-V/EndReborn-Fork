package io.github.srdjanv.endreforked.common.tiles.passiveinducers;

import io.github.srdjanv.endreforked.api.entropy.EntropyRange;
import io.github.srdjanv.endreforked.common.tiles.base.BasePassiveInducer;

public class FluidOrganaTile extends BasePassiveInducer {

    public FluidOrganaTile() {
        super(EntropyRange.TWO, 5 * 20, 30);
    }

}
