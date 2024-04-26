package io.github.srdjanv.endreforked.common.blocks;

import io.github.srdjanv.endreforked.common.ModBlocks;
import io.github.srdjanv.endreforked.common.blocks.base.BlockBase;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOrganaSmallFlower extends BlockBase {
    public static final AxisAlignedBB BB = new AxisAlignedBB(0.4D, 0.4D, 0, 0.6D, 0.6D, 0.6D);

    public BlockOrganaSmallFlower() {
        super("organa_small_flower", Material.PLANTS, MapColor.PURPLE);
    }

    @Override public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BB;
    }


    @Override public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        if (canPlaceBlockAt(worldIn, pos)) {
            return worldIn.getBlockState(pos.offset(side)).getBlock() == ModBlocks.ORGANA_PLANT_BLOCK.get();
        }
        return false;
    }

    @Override public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
