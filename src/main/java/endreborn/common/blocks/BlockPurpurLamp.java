package endreborn.common.blocks;

import net.minecraft.block.material.Material;

import endreborn.common.blocks.base.BlockBase;

public class BlockPurpurLamp extends BlockBase {

    public BlockPurpurLamp(String name) {
        super(name, Material.ROCK);
        setLightLevel(1.0F);
    }
}
