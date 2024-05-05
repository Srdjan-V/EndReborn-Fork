package io.github.srdjanv.endreforked.common.blocks;

import io.github.srdjanv.endreforked.common.ModItems;
import io.github.srdjanv.endreforked.common.blocks.base.BlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockEndMoss extends BlockBase {
    public BlockEndMoss() {
        super("end_moss", Material.ROCK, MapColor.PURPLE);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 3);
        setHardness(1.5F);
    }

    @Override public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ModItems.END_MOSS_BLOCK.get();
    }
}
