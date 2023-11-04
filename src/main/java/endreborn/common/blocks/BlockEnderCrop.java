package endreborn.common.blocks;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import endreborn.EndReborn;
import endreborn.common.ModBlocks;
import endreborn.common.blocks.base.BaseBlockCrops;
import endreborn.utils.IHasModel;

public class BlockEnderCrop extends BaseBlockCrops implements IHasModel {

    public BlockEnderCrop(String name) {
        super(name);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            if (this.isMaxAge(state)) {
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(Items.ENDER_PEARL, 1)));
                worldIn.setBlockState(pos, ModBlocks.BROKEN_FLOWER.get().getDefaultState());
                return true;
            }
        }
        return false;
    }

    @Override
    protected Item getSeed() {
        return null;
    }

    @Override
    protected Item getCrop() {
        return Items.ENDER_PEARL;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().isReplaceable(world, pos) &&
                world.getBlockState(pos.down()).getBlock() == Blocks.END_STONE;
    }

    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return !this.isMaxAge(state);
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        return world.getBlockState(pos.down()).getBlock() == Blocks.END_STONE;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);

        if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's
                                                   // light
        {
            int i = this.getAge(state);

            if (i < this.getMaxAge()) {
                float f = getGrowthChance(this, worldIn, pos);

                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state,
                        rand.nextInt((int) (2.0F / f) + 1) == 0)) {
                    worldIn.setBlockState(pos, this.withAge(i + 1), 2);
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state,
                            worldIn.getBlockState(pos));
                }
            }
        }
    }

    @Override
    public void registerModels() {
        EndReborn.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
