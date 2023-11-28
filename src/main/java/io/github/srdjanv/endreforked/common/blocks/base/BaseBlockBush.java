package io.github.srdjanv.endreforked.common.blocks.base;

import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.utils.models.InventoryBlockModel;

public class BaseBlockBush extends BlockBush implements InventoryBlockModel {

    public BaseBlockBush(String name, Material material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReforked.endertab);
    }
}
