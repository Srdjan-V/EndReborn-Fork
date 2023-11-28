package io.github.srdjanv.endreforked.client.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.utils.models.InventoryItemModel;

public class ArmourBase extends ItemArmor implements InventoryItemModel {

    public ArmourBase(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
        super(materialIn, renderIndexIn, equipmentSlotIn);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReforked.endertab);
    }
}
