package endreborn.common.items.tools;

import net.minecraft.item.ItemPickaxe;

import endreborn.EndReborn;
import endreborn.utils.models.InventoryItemModel;

public class ToolPickaxe extends ItemPickaxe implements InventoryItemModel {

    public ToolPickaxe(String name, ToolMaterial material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }
}
