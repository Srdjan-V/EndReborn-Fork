package endreborn.common.blocks.base;

import endreborn.common.ModItems;
import net.minecraft.block.BlockCrops;

import endreborn.EndReborn;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BaseBlockCrops extends BlockCrops {

    public BaseBlockCrops(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }

    protected boolean handleRightClick(World worldIn, BlockPos pos, IBlockState state, ItemStack drop) {
        if (worldIn.isRemote) return false;
        if (isMaxAge(state)) {
            spawnAsEntity(worldIn, pos, drop);
            worldIn.setBlockState(pos, withAge(0));
            return true;
        }
        return false;
    }

}
