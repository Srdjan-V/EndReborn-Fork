package endreborn.common.blocks.base;

import net.minecraft.block.BlockCrops;

import endreborn.EndReborn;

public class BaseBlockCrops extends BlockCrops {

    public BaseBlockCrops(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }
}
