package endreborn.common.items.tools;

import net.minecraft.item.ItemPickaxe;

import endreborn.EndReborn;
import endreborn.utils.IHasModel;

public class ToolPickaxe extends ItemPickaxe implements IHasModel {

    public ToolPickaxe(String name, ToolMaterial material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }

    @Override
    public void registerModels() {
        EndReborn.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
