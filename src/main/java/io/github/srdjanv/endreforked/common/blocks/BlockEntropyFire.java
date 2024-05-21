package io.github.srdjanv.endreforked.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.utils.models.InventoryBlockModel;

import java.util.Random;

public class BlockEntropyFire extends BlockFire implements InventoryBlockModel {

    public BlockEntropyFire() {
        super();
        var name = "entropy_fire";
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReforked.ENDERTAB);
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityWalk(worldIn, pos, entityIn);
    }
}
