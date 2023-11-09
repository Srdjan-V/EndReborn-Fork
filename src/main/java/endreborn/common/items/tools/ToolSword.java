package endreborn.common.items.tools;

import net.minecraft.item.ItemSword;

import endreborn.EndReborn;
import endreborn.utils.models.InventoryItemModel;

public class ToolSword extends ItemSword implements InventoryItemModel {

    public ToolSword(String name, ToolMaterial material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }
}
