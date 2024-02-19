package io.github.srdjanv.endreforked.common.blocks;

import java.util.Random;

import io.github.srdjanv.endreforked.common.entity.EntityChronologist;
import io.github.srdjanv.endreforked.common.entity.EntityWatcher;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import io.github.srdjanv.endreforked.common.ModBlocks;
import io.github.srdjanv.endreforked.common.blocks.base.BlockBase;

public class BlockEndMoss extends BlockBase implements IGrowable {

    public BlockEndMoss(String name) {
        super(name, Material.GRASS);
        setHarvestLevel("pickaxe", 3);
    }

    @Override public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (entityIn.isSneaking()) {
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        } else entityIn.fall(fallDistance, 0.0F);
    }

    @Override public void onLanded(World worldIn, Entity entityIn) {
        if (entityIn.isSneaking()) {
            super.onLanded(worldIn, entityIn);
        } else if (entityIn.motionY < 0.0D) {
            entityIn.motionY = -entityIn.motionY;

            if (!(entityIn instanceof EntityLivingBase)) {
                entityIn.motionY *= 0.8D;
            }
        }
    }

    @Override public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (Math.abs(entityIn.motionY) < 0.1D && !entityIn.isSneaking()) {
            double d0 = 0.4D + Math.abs(entityIn.motionY) * 0.2D;
            entityIn.motionX *= d0;
            entityIn.motionZ *= d0;
        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos startPos, IBlockState state) {
        BlockPos upPos = startPos.up();

        for (int i = 0; i < 64; ++i) {
            BlockPos pos = upPos;
            int j = 0;

            while (true) {
                if (j >= i / 16) {
                    if (worldIn.isAirBlock(pos)) {
                        if (ModBlocks.ORGANA_WEED.get().canPlaceBlockAt(worldIn, pos)) {
                            worldIn.setBlockState(pos, ModBlocks.ORGANA_WEED.get().getDefaultState());
                        } else {
                            var downPos = pos.down();
                            if (worldIn.getBlockState(downPos).getBlock() == Blocks.END_STONE) {
                                worldIn.setBlockState(downPos, getDefaultState());
                            }
                        }
                    }
                    break;
                }

                pos = pos.add(
                        rand.nextInt(3) - 1,
                        (rand.nextInt(3) - 1) * rand.nextInt(3) / 2,
                        rand.nextInt(3) - 1);

                var downPosBlock = worldIn.getBlockState(pos.down()).getBlock();
                if (downPosBlock == this || downPosBlock == Blocks.END_STONE) {
                    ++j;
                } else break;
            }
        }
    }
}
