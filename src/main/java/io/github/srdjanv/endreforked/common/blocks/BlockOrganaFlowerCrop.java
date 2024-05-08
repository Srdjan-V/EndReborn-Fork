package io.github.srdjanv.endreforked.common.blocks;

import io.github.srdjanv.endreforked.common.ModBlocks;
import io.github.srdjanv.endreforked.common.ModItems;
import io.github.srdjanv.endreforked.common.blocks.base.BaseBlockCrops;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockOrganaFlowerCrop extends BaseBlockCrops {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 4);

    public BlockOrganaFlowerCrop() {
        super("organa_flower_crop");
        sustainableBlocks.add(ModBlocks.END_MOSS_GRASS_BLOCK.get());
    }

    @Override protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    @Override
    protected PropertyInteger getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 4;
    }

    @Override
    protected Item getSeed() {
        return ModItems.ORGANA_FLOWER_SEED.get();
    }

    @Override
    protected Item getCrop() {
        return ModItems.ORGANA_FLOWER.get();
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state,
                             @Nullable TileEntity te, ItemStack stack) {
    }

    @Override public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    }

    @Override public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }

    @Override public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        if (state.getBlock() == this) //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
        {
            IBlockState soil = world.getBlockState(pos.down());
            return soil.getBlock().canSustainPlant(soil, world, pos.down(), net.minecraft.util.EnumFacing.UP, this);
        }
        return this.canSustainBush(world.getBlockState(pos.down()));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) return;
        checkAndDropBlock(worldIn, pos, state);

        // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (!worldIn.isAreaLoaded(pos, 1)) return;

        int i = getAge(state);
        if (i < getMaxAge()) {
            float f = getGrowthChance(this, worldIn, pos);

            if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state,
                    rand.nextInt((int) (2.0F / f) + 1) == 0)) {
                worldIn.setBlockState(pos, withAge(i + 1), 2);
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state,
                        worldIn.getBlockState(pos));
            }
        } if (i == getMaxAge()) {
            worldIn.setBlockState(pos, ModBlocks.ORGANA_FLOWER_BLOCK.get().getDefaultState());
        }
    }

}
