package io.github.srdjanv.endreforked.common.blocks;

import io.github.srdjanv.endreforked.common.ModBlocks;
import io.github.srdjanv.endreforked.common.blocks.base.BaseBlockBush;
import io.github.srdjanv.endreforked.common.tiles.OrganaWeedTile;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static io.github.srdjanv.endreforked.common.blocks.BlockEndCoral.END_BUSH_AABB;

public class BlockOrganaWeed extends BaseBlockBush implements IShearable {

    public BlockOrganaWeed() {
        super("ogana_weed", Material.VINE);
    }

    @Override public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @org.jetbrains.annotations.Nullable @Override public TileEntity createTileEntity(World world, IBlockState state) {
        return new OrganaWeedTile();
    }

    @Override public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (worldIn.isRemote) return;
        entityIn.fallDistance = 0;
        if (entityIn instanceof EntityLivingBase livingBase) {
            livingBase.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 10));
        }
        if (entityIn instanceof EntityPlayer player) {
            if (player.isSneaking()) return;
            player.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 20, 2));
        }
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.PURPLE;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return END_BUSH_AABB;
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == ModBlocks.END_MOSS_GRASS_BLOCK.get();
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state,
                             @Nullable TileEntity te, ItemStack stack) {}

    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos,
                                               int fortune) {
        return Collections.singletonList(new ItemStack(ModBlocks.ORGANA_WEED_BLOCK.get()));
    }
}
