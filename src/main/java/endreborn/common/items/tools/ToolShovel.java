package endreborn.common.items.tools;

import net.minecraft.item.ItemSpade;

import endreborn.EndReborn;
import endreborn.utils.models.InventoryItemModel;

public class ToolShovel extends ItemSpade implements InventoryItemModel {

    public ToolShovel(String name, ToolMaterial material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }
}
