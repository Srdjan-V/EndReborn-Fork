package endreborn.common.items.food;

import net.minecraft.item.ItemFood;

import endreborn.EndReborn;
import endreborn.utils.IHasModel;

public class DragoniteBerries extends ItemFood implements IHasModel {

    public DragoniteBerries(String name) {
        super(1, 0.8F, false);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }

    @Override
    public void registerModels() {
        EndReborn.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
