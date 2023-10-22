package endreborn.common.items.tools;

import net.minecraft.item.ItemHoe;

import endreborn.EndReborn;
import endreborn.utils.IHasModel;

public class ToolHoe extends ItemHoe implements IHasModel {

    public ToolHoe(String name, ToolMaterial material) {
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
