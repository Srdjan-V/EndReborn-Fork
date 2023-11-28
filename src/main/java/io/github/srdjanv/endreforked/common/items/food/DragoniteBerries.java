package io.github.srdjanv.endreforked.common.items.food;

import net.minecraft.item.ItemFood;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.utils.models.InventoryItemModel;

public class DragoniteBerries extends ItemFood implements InventoryItemModel {

    public DragoniteBerries(String name) {
        super(1, 0.8F, false);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReforked.endertab);
    }
}
