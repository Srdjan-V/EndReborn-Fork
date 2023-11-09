package endreborn.common.items.tools;

import net.minecraft.item.ItemHoe;

import endreborn.EndReborn;
import endreborn.utils.models.InventoryItemModel;

public class ToolHoe extends ItemHoe implements InventoryItemModel {

    public ToolHoe(String name, ToolMaterial material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }
}
