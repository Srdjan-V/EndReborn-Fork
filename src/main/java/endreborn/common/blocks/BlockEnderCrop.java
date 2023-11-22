package endreborn.common.blocks;

import java.util.Random;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
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

import endreborn.common.ModItems;
import endreborn.common.blocks.base.BaseBlockCrops;

public class BlockEnderCrop extends BaseBlockCrops {

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 4);

    public BlockEnderCrop(String name) {
        super(name);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    @Override
    protected PropertyInteger getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return false;
        if (this.isMaxAge(state)) {
            worldIn.spawnEntity(
                    new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(),
                            new ItemStack(Items.ENDER_PEARL, worldIn.rand.nextInt(3))));
            worldIn.setBlockState(pos,
                    getDefaultState().withProperty(BlockEnderCrop.AGE, 4));
            return true;
        }
        return false;
    }

    @Override
    protected Item getSeed() {
        return ModItems.ENDER_FLOWER_CROP.get();
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

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        return world.getBlockState(pos.down()).getBlock() == Blocks.END_STONE;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
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
        }
    }
}
