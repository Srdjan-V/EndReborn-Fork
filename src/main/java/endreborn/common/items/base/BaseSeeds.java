package endreborn.common.items.base;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSeeds;

import endreborn.EndReborn;

public class BaseSeeds extends ItemSeeds {

    public BaseSeeds(String name, Block crops, Block soil) {
        super(crops, soil);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }
}
