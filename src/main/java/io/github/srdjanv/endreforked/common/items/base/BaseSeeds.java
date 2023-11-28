package io.github.srdjanv.endreforked.common.items.base;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSeeds;

import io.github.srdjanv.endreforked.EndReforked;

public class BaseSeeds extends ItemSeeds {

    public BaseSeeds(String name, Block crops, Block soil) {
        super(crops, soil);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReforked.endertab);
    }
}
