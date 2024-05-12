package io.github.srdjanv.endreforked.common.items.base;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.utils.models.InventoryItemModel;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSeedFood;

public class ItemSeedFoodBase extends ItemSeedFood implements InventoryItemModel {
    public ItemSeedFoodBase(String name, int healAmount, float saturation, Block crops, Block soil) {
        super(healAmount, saturation, crops, soil);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReforked.ENDERTAB);
    }

}
