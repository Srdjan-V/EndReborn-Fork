package endreborn.common.items.food;

import net.minecraft.item.ItemFood;

import endreborn.EndReborn;
import endreborn.utils.models.InventoryItemModel;

public class DragoniteBerries extends ItemFood implements InventoryItemModel {

    public DragoniteBerries(String name) {
        super(1, 0.8F, false);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }
}
