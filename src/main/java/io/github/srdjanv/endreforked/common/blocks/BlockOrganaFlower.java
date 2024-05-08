package io.github.srdjanv.endreforked.common.blocks;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.common.ModBlocks;
import io.github.srdjanv.endreforked.common.ModItems;
import io.github.srdjanv.endreforked.common.blocks.base.BlockBase;
import io.github.srdjanv.endreforked.common.tiles.OrganaFlowerTile;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockOrganaFlower extends BlockBase {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 10);

    public BlockOrganaFlower() {
        super("organa_flower", Material.PLANTS, MapColor.PURPLE);
        setTickRandomly(true);
        setDefaultState(
                blockState.getBaseState()
                        .withProperty(AGE, 0));
        setHarvestLevel("pickaxe", 2);
        setHardness(1.25f);
    }

    @Override public void registerModels() {
        EndReforked.getProxy().registerItemRenderer(ModItems.ORGANA_FLOWER.get(), 0, "inventory");
        EndReforked.getProxy().registerItemRenderer(ModItems.ORGANA_FLOWER.get(), 10, "inventory");
    }

    @Override public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 8));
        items.add(new ItemStack(this, 1, 10));
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.isRemote) return;
        if (!canSurvive(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
            return;
        }
        BlockPos upPos = pos.up();

        if (!worldIn.isAirBlock(upPos) || upPos.getY() >= 256) return;
        int age = state.getValue(AGE);

        if (age >= 10 || !net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, upPos, state, true)) return;
        if (age >= 8) {
            switch (age) {
                case 8 -> {
                    if (rand.nextDouble() < 0.2)
                        worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, 9), 2);
                }
                case 9 -> placeBigDeadFlower(worldIn, pos);
            }
            return;
        }

        boolean growOnlyUp = false;
        boolean flag1 = false;
        IBlockState downBlockState = worldIn.getBlockState(pos.down());
        Block downBlock = downBlockState.getBlock();

        if (downBlock == ModBlocks.END_MOSS_GRASS_BLOCK.get()) {
            growOnlyUp = true;
        } else if (downBlock == ModBlocks.ORGANA_PLANT_BLOCK.get()) {
            int j = 1;

            for (int k = 0; k < 4; ++k) {
                Block block1 = worldIn.getBlockState(pos.down(j + 1)).getBlock();
                if (block1 != ModBlocks.ORGANA_PLANT_BLOCK.get()) {
                    if (block1 == ModBlocks.END_MOSS_GRASS_BLOCK.get()) flag1 = true;
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
            placeGrownFlower(worldIn, upPos, age + 1);
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
            } else placeSmallDeadFlower(worldIn, pos);
        } else placeSmallDeadFlower(worldIn, pos);
        net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
    }

    private void placePlant(World worldIn, BlockPos pos) {
        worldIn.setBlockState(pos, ModBlocks.ORGANA_PLANT_BLOCK.get().getDefaultState(), 2);
    }

    private void placeGrownFlower(World worldIn, BlockPos pos, int age) {
        worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, age), 2);
        worldIn.playEvent(1033, pos, 0);
    }

    private void placeBigDeadFlower(World worldIn, BlockPos pos) {
        worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, 10), 2);
        worldIn.playEvent(1034, pos, 0);
    }

    private void placeSmallDeadFlower(World worldIn, BlockPos pos) {
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

        if (belowBlock == ModBlocks.ORGANA_PLANT_BLOCK.get() || belowBlock == ModBlocks.END_MOSS_GRASS_BLOCK.get())
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

    public static void generatePlant(World worldIn, BlockPos pos, Random rand, int age) {
        worldIn.setBlockState(pos, ModBlocks.ORGANA_PLANT_BLOCK.get().getDefaultState(), 2);
        growPlantRecursive(worldIn, pos, rand, pos, age, 0);
    }

    private static void growPlantRecursive(World worldIn, BlockPos pos, Random rand, BlockPos pos2, int age, int iteration) {
        int i = rand.nextInt(4) + 1;
        if (iteration == 0) ++i;

        for (int j = 0; j < i; ++j) {
            BlockPos blockpos = pos.up(j + 1);

            if (!areAllNeighborsEmpty(worldIn, blockpos, null)) {
                return;
            }

            worldIn.setBlockState(blockpos, ModBlocks.ORGANA_PLANT_BLOCK.get().getDefaultState(), 2);
        }

        boolean grown = false;
        if (iteration < 4) {
            int l = rand.nextInt(4);

            if (iteration == 0) {
                ++l;
            }

            for (int k = 0; k < l; ++k) {
                EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
                BlockPos blockpos1 = pos.up(i).offset(enumfacing);

                if (Math.abs(blockpos1.getX() - pos2.getX()) < age && Math.abs(blockpos1.getZ() - pos2.getZ()) < age && worldIn.isAirBlock(blockpos1) && worldIn.isAirBlock(blockpos1.down()) && areAllNeighborsEmpty(worldIn, blockpos1, enumfacing.getOpposite())) {
                    grown = true;
                    worldIn.setBlockState(blockpos1, ModBlocks.ORGANA_PLANT_BLOCK.get().getDefaultState(), 2);
                    growPlantRecursive(worldIn, blockpos1, rand, pos2, age, iteration + 1);
                }
            }
        }

        if (!grown)
            worldIn.setBlockState(pos.up(i), ModBlocks.ORGANA_FLOWER_BLOCK.get().getDefaultState().withProperty(AGE, 10), 2);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }


    @Override public boolean hasTileEntity(IBlockState state) {
        return state.getValue(AGE) == 10;
    }

    @org.jetbrains.annotations.Nullable @Override public TileEntity createTileEntity(World world, IBlockState state) {
        if (state.getValue(AGE) == 10) return new OrganaFlowerTile();
        return null;
    }

    //todo remove
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        updateTick(worldIn, pos, state, worldIn.rand);
        return true;
    }
}
