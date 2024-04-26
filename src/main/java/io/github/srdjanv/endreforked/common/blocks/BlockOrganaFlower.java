package io.github.srdjanv.endreforked.common.blocks;

import io.github.srdjanv.endreforked.common.ModBlocks;
import io.github.srdjanv.endreforked.common.blocks.base.BlockBase;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockOrganaFlower extends BlockBase {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 8);

    public BlockOrganaFlower() {
        super("organa_flower", Material.PLANTS, MapColor.PURPLE);
        setTickRandomly(true);
        setDefaultState(
                blockState.getBaseState()
                        .withProperty(AGE, 0));
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!canSurvive(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
            return;
        }
        BlockPos upPos = pos.up();

        if (!worldIn.isAirBlock(upPos) || upPos.getY() >= 256) return;
        int age = state.getValue(AGE);

        if (age >= 8 || !net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, upPos, state, true)) return;
        boolean growOnlyUp = false;
        boolean flag1 = false;
        IBlockState downBlockState = worldIn.getBlockState(pos.down());
        Block downBlock = downBlockState.getBlock();

        if (downBlock == ModBlocks.END_MOSS_BLOCK.get()) {
            growOnlyUp = true;
        } else if (downBlock == ModBlocks.ORGANA_PLANT_BLOCK.get()) {
            int j = 1;

            for (int k = 0; k < 4; ++k) {
                Block block1 = worldIn.getBlockState(pos.down(j + 1)).getBlock();
                if (block1 != ModBlocks.ORGANA_PLANT_BLOCK.get()) {
                    if (block1 == ModBlocks.END_MOSS_BLOCK.get()) flag1 = true;
                    break;
                }

                ++j;
            }

            int i1 = 4;
            if (flag1) ++i1;
            if (j < 2 || rand.nextInt(i1) >= j) growOnlyUp = true;
        } else if (downBlockState.getMaterial() == Material.AIR) growOnlyUp = true;

        if (growOnlyUp && areAllNeighborsEmpty(worldIn, upPos, null) && worldIn.isAirBlock(pos.up(2))) {
            placePlant(worldIn, pos);
            placeGrownFlower(worldIn, upPos, age);
        } else if (age < 7) {
            int l = rand.nextInt(4);
            boolean flag2 = false;

            if (flag1) ++l;
            for (int j1 = 0; j1 < l; ++j1) {
                EnumFacing facing = EnumFacing.Plane.HORIZONTAL.random(rand);
                BlockPos facingPos = pos.offset(facing);

                if (worldIn.isAirBlock(facingPos) && worldIn.isAirBlock(facingPos.down()) && areAllNeighborsEmpty(worldIn, facingPos, facing.getOpposite())) {
                    placeGrownFlower(worldIn, facingPos, age + 1);
                    flag2 = true;
                }
            }

            if (flag2) {
                placePlant(worldIn, pos);
            } else placeDeadFlower(worldIn, pos);
        } else placeDeadFlower(worldIn, pos);
        net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
    }

    private void placePlant(World worldIn, BlockPos pos) {
        worldIn.setBlockState(pos, ModBlocks.ORGANA_PLANT_BLOCK.get().getDefaultState(), 2);
        boolean placed = false;
        List<EnumFacing> facings = new ObjectArrayList<>(EnumFacing.VALUES);
        Collections.shuffle(facings);
        for (EnumFacing value : facings) {
            if (placed) break;
            var flowerPos = pos.offset(value);
            if (worldIn.isAirBlock(flowerPos)) {
                worldIn.setBlockState(flowerPos, ModBlocks.ORGANA_SMALL_FLOWER_BLOCK.get().getDefaultState(), 2);
                placed = true;
            }
        }

    }

    private void placeGrownFlower(World worldIn, BlockPos pos, int age) {
        worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, age), 2);
        worldIn.playEvent(1033, pos, 0);
    }

    private void placeDeadFlower(World worldIn, BlockPos pos) {
        worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, 8), 2);
        worldIn.playEvent(1034, pos, 0);
    }

    private static boolean areAllNeighborsEmpty(World worldIn, BlockPos pos, EnumFacing excludingSide) {
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (enumfacing != excludingSide && !worldIn.isAirBlock(pos.offset(enumfacing))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canSurvive(worldIn, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canSurvive(worldIn, pos)) worldIn.scheduleUpdate(pos, this, 1);
    }

    public boolean canSurvive(World worldIn, BlockPos pos) {
        IBlockState belowState = worldIn.getBlockState(pos.down());
        Block belowBlock = belowState.getBlock();

        if (belowBlock == ModBlocks.ORGANA_PLANT_BLOCK.get() || belowBlock == ModBlocks.END_MOSS_BLOCK.get())
            return true;
        if (belowState.getMaterial() != Material.AIR) return false;
        int i = 0;

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            IBlockState facingState = worldIn.getBlockState(pos.offset(enumfacing));
            Block facingBlock = facingState.getBlock();

            if (facingBlock == ModBlocks.ORGANA_PLANT_BLOCK.get()) {
                ++i;
            } else if (facingState.getMaterial() != Material.AIR) {
                return false;
            }
        }

        return i == 1;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this)));
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AGE);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    public static void generatePlant(World worldIn, BlockPos pos, Random rand, int p_185603_3_) {
        worldIn.setBlockState(pos, Blocks.CHORUS_PLANT.getDefaultState(), 2);
        growTreeRecursive(worldIn, pos, rand, pos, p_185603_3_, 0);
    }

    private static void growTreeRecursive(World worldIn, BlockPos pos, Random rand, BlockPos p_185601_3_, int p_185601_4_, int p_185601_5_) {
        int i = rand.nextInt(4) + 1;

        if (p_185601_5_ == 0) {
            ++i;
        }

        for (int j = 0; j < i; ++j) {
            BlockPos blockpos = pos.up(j + 1);

            if (!areAllNeighborsEmpty(worldIn, blockpos, null)) {
                return;
            }

            worldIn.setBlockState(blockpos, Blocks.CHORUS_PLANT.getDefaultState(), 2);
        }

        boolean flag = false;

        if (p_185601_5_ < 4) {
            int l = rand.nextInt(4);

            if (p_185601_5_ == 0) {
                ++l;
            }

            for (int k = 0; k < l; ++k) {
                EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
                BlockPos blockpos1 = pos.up(i).offset(enumfacing);

                if (Math.abs(blockpos1.getX() - p_185601_3_.getX()) < p_185601_4_ && Math.abs(blockpos1.getZ() - p_185601_3_.getZ()) < p_185601_4_ && worldIn.isAirBlock(blockpos1) && worldIn.isAirBlock(blockpos1.down()) && areAllNeighborsEmpty(worldIn, blockpos1, enumfacing.getOpposite())) {
                    flag = true;
                    worldIn.setBlockState(blockpos1, Blocks.CHORUS_PLANT.getDefaultState(), 2);
                    growTreeRecursive(worldIn, blockpos1, rand, p_185601_3_, p_185601_4_, p_185601_5_ + 1);
                }
            }
        }

        if (!flag) {
            worldIn.setBlockState(pos.up(i), Blocks.CHORUS_FLOWER.getDefaultState().withProperty(AGE, 5), 2);
        }
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        updateTick(worldIn, pos, state, worldIn.rand);
        return true;
    }
}