package endreborn.common.blocks;

import java.util.Collections;

import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import endreborn.common.ModBlocks;
import endreborn.common.blocks.base.BaseBlockBush;

public class BlockEndCoral extends BaseBlockBush implements net.minecraftforge.common.IShearable {

    protected static final AxisAlignedBB END_BUSH_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D,
            0.09999999403953552D, 0.8999999761581421D, 0.400000011920929D, 0.8999999761581421D);

    public BlockEndCoral(String name, Material material) {
        super(name, material);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return END_BUSH_AABB;
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.YELLOW;
    }

    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == net.minecraft.init.Blocks.END_BRICKS ||
                state.getBlock() == net.minecraft.init.Blocks.END_STONE;
    }

    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state,
                             @Nullable TileEntity te, ItemStack stack) {}

    @Override
    public boolean isShearable(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public java.util.List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos,
                                               int fortune) {
        return Collections.singletonList(new ItemStack(ModBlocks.END_MAGMA_BLOCK.get()));
    }
}
