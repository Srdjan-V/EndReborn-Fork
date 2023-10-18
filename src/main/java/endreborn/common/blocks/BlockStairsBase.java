package endreborn.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import endreborn.EndReborn;
import endreborn.common.ModBlocks;
import endreborn.common.ModItems;
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

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void registerModels() {
        EndReborn.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
