package endreborn.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.item.Item;

import endreborn.EndReborn;
import endreborn.utils.IHasModel;

public class BlockStairsBase extends BlockStairs implements IHasModel {

    public BlockStairsBase(String name, Block block) {
        super(block.getDefaultState());
        setTranslationKey(name);
        setHardness(block.getBlockHardness(block.getDefaultState(), null, null));
        setSoundType(block.getSoundType());
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
        useNeighborBrightness = true;
    }

    @Override
    public void registerModels() {
        EndReborn.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
