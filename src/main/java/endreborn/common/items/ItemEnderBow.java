package endreborn.common.items;

import net.minecraft.item.ItemStack;

import endreborn.common.ModItems;
import endreborn.common.items.tools.ToolBow;
import endreborn.utils.models.InventoryItemModel;

public class ItemEnderBow extends ToolBow implements InventoryItemModel {

    public ItemEnderBow(String name) {
        super(name);
        setMaxDamage(1024);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == ModItems.INGOT_ENDORIUM.get() || super.getIsRepairable(toRepair, repair);
    }
}
