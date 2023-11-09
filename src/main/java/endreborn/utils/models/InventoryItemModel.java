package endreborn.utils.models;

import net.minecraft.item.Item;

import endreborn.EndReborn;

public interface InventoryItemModel extends IHasModel {

    @Override
    default void registerModels() {
        EndReborn.proxy.registerItemRenderer((Item) this, 0, "inventory");
    }
}
