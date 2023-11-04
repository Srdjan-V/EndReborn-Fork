package endreborn.common.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import endreborn.EndReborn;

public class BlockBase extends Block {

    public BlockBase(String name, Material material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }
}
