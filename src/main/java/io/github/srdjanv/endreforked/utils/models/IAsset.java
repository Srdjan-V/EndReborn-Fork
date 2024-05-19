package io.github.srdjanv.endreforked.utils.models;

import io.github.srdjanv.endreforked.EndReforked;

public interface IAsset {
    default boolean shouldBind() {
        return EndReforked.getProxy().SIDE.isClient();
    }

    void handleAssets();
}
