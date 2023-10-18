package endreborn.mod.items;

import net.minecraft.item.Item;

import endreborn.EndReborn;
import endreborn.init.ItemInit;
import endreborn.utils.IHasModel;

public class ItemBase extends Item implements IHasModel {

    public ItemBase(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);

        ItemInit.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        EndReborn.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
