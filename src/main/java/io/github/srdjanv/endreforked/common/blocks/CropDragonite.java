package io.github.srdjanv.endreforked.common.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import io.github.srdjanv.endreforked.common.ModItems;
import io.github.srdjanv.endreforked.common.blocks.base.BaseBlockCrops;

// TODO: 20/11/2023 fix age
public class CropDragonite extends BaseBlockCrops {

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
