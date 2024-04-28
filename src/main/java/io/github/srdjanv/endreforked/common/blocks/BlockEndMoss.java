package io.github.srdjanv.endreforked.common.blocks;

import io.github.srdjanv.endreforked.common.blocks.base.BlockBase;
import net.minecraft.block.material.Material;

public class BlockEndMoss extends BlockBase {
    public BlockEndMoss() {
        super("end_moss", Material.ROCK);
        setHarvestLevel("pickaxe", 4);
    }
}
