package endreborn.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import endreborn.EndReborn;
import endreborn.utils.IHasModel;

public class BlockBase extends Block implements IHasModel {

    public BlockBase(String name, Material material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }

    @Override
    public void registerModels() {
        EndReborn.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
