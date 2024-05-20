package io.github.srdjanv.endreforked.common.fluids.blocks;

import io.github.srdjanv.endreforked.common.ModFluids;
import io.github.srdjanv.endreforked.common.fluids.base.BaseBlockFluid;
import io.github.srdjanv.endreforked.common.tiles.passiveinducers.FluidEntropyTile;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BlockFluidOrgana extends BaseBlockFluid {
    public BlockFluidOrgana() {
        super("organa", ModFluids.ORGANA.get(), Material.WATER, MapColor.PURPLE);
    }

    @Override public boolean hasTileEntity(IBlockState state) {
        return state.getValue(LEVEL) == 0;
    }

    @Nullable @Override public TileEntity createTileEntity(World world, IBlockState state) {
        return state.getValue(LEVEL) == 0 ? new FluidEntropyTile() : null;
    }

    @Override public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (worldIn.isRemote) return;
        entityIn.fallDistance = 0;
        if (entityIn instanceof EntityPlayer player) {
            if (player.isSneaking()) return;
            player.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 40, 2));
            player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 120, 2));
            return;
        }
        if (entityIn instanceof EntityLivingBase livingBase) {
            livingBase.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 40));
            livingBase.addPotionEffect(new PotionEffect(MobEffects.SPEED, 60));
        }
    }
}
