package endreborn.common.items;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import endreborn.EndReborn;
import endreborn.common.ModItems;
import endreborn.utils.IHasModel;

public class ItemLegendary extends Item implements IHasModel {

    public ItemLegendary(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);

        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        EndReborn.proxy.registerItemRenderer(this, 0, "inventory");
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }
}