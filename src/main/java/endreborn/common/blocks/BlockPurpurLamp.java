package endreborn.common.blocks;

import net.minecraft.block.material.Material;

import endreborn.common.blocks.base.BlockBase;
import endreborn.utils.models.InventoryBlockModel;

public class BlockPurpurLamp extends BlockBase implements InventoryBlockModel {

    public BlockPurpurLamp(String name) {
        super(name, Material.ROCK);
        setLightLevel(1.0F);
    }
}
