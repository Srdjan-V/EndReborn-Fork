package endreborn.common.items.base;

import net.minecraft.item.Item;

import endreborn.EndReborn;
import endreborn.utils.models.InventoryItemModel;

public class ItemBase extends Item implements InventoryItemModel {

    public ItemBase(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }
}
