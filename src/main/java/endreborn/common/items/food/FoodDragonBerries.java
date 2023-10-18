package endreborn.common.items.food;

import net.minecraft.item.ItemFood;

import endreborn.EndReborn;
import endreborn.common.ModItems;
import endreborn.utils.IHasModel;

public class FoodDragonBerries extends ItemFood implements IHasModel {

    public FoodDragonBerries(String name) {
        super(1, 0.1F, false);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);

        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        EndReborn.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
