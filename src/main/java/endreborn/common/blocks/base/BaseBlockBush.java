package endreborn.common.blocks.base;

import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;

import endreborn.EndReborn;
import endreborn.utils.models.InventoryBlockModel;

public class BaseBlockBush extends BlockBush implements InventoryBlockModel {

    public BaseBlockBush(String name, Material material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }
}
