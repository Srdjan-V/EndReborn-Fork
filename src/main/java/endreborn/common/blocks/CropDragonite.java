package endreborn.common.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import endreborn.common.ModItems;
import endreborn.common.blocks.base.BaseBlockCrops;
import endreborn.utils.models.InventoryBlockModel;

public class CropDragonite extends BaseBlockCrops implements InventoryBlockModel {

    public CropDragonite(String name) {
        super(name);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return handleRightClick(worldIn, pos, state, new ItemStack(ModItems.DRAGONITE_BERRIES.get(), 2));
    }

    @Override
    protected Item getSeed() {
        return ModItems.DRAGONITE_SEEDS.get();
    }

    @Override
    protected Item getCrop() {
        return ModItems.DRAGONITE_BERRIES.get();
    }
}
