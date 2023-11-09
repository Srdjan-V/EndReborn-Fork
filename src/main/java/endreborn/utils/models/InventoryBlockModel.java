package endreborn.utils.models;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import endreborn.EndReborn;

public interface InventoryBlockModel extends IHasModel {

    @Override
    default void registerModels() {
        EndReborn.proxy.registerItemRenderer(Item.getItemFromBlock((Block) this), 0, "inventory");
    }
}
