package io.github.srdjanv.endreforked.common.items.tools;

import net.minecraft.item.ItemPickaxe;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.utils.models.InventoryItemModel;

public class ToolPickaxe extends ItemPickaxe implements InventoryItemModel {

    public ToolPickaxe(String name, ToolMaterial material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReforked.ENDERTAB);
    }
}
