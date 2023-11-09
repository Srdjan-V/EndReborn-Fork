package endreborn.common.items.base;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import endreborn.EndReborn;
import endreborn.utils.models.InventoryItemModel;

public class ItemLegendary extends Item implements InventoryItemModel {

    public ItemLegendary(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }
}
