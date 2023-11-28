package io.github.srdjanv.endreforked.common.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.utils.models.InventoryBlockModel;

public class BlockBase extends Block implements InventoryBlockModel {

    public BlockBase(String name, Material material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReforked.endertab);
    }
}
