package endreborn.common.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

import endreborn.EndReborn;
import endreborn.utils.models.InventoryBlockModel;

public class BlockStairsBase extends BlockStairs implements InventoryBlockModel {

    public BlockStairsBase(String name, Block block) {
        super(block.getDefaultState());
        setTranslationKey(name);
        setHardness(block.getBlockHardness(block.getDefaultState(), null, null));
        setSoundType(block.getSoundType());
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
        useNeighborBrightness = true;
    }
}
