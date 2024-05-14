package io.github.srdjanv.endreforked.common.tiles.passiveinducers;

import io.github.srdjanv.endreforked.api.entropy.EntropyRadius;
import io.github.srdjanv.endreforked.common.tiles.base.BasePassiveInducer;

public class EntropyCropTile extends BasePassiveInducer {

    public EntropyCropTile() {
        super(EntropyRadius.TWO, 5 * 20, 25);
    }

}
