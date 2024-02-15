package io.github.srdjanv.endreforked.utils.models;

import net.minecraft.item.Item;

import io.github.srdjanv.endreforked.EndReforked;

public interface InventoryItemModel extends IHasModel {

    @Override
    default void registerModels() {
        EndReforked.getProxy().registerItemRenderer((Item) this, 0, "inventory");
    }
}
