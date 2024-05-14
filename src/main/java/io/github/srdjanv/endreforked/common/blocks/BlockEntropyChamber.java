package io.github.srdjanv.endreforked.common.blocks;

import com.cleanroommc.modularui.factory.TileEntityGuiFactory;
import io.github.srdjanv.endreforked.common.ModBlocks;
import io.github.srdjanv.endreforked.common.blocks.base.BlockBase;
import io.github.srdjanv.endreforked.common.tiles.EntropyChamberTile;
import io.github.srdjanv.endreforked.common.tiles.MaterializerTile;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BlockEntropyChamber extends BlockBase {
    public BlockEntropyChamber() {
        super("entropy_chamber", Material.IRON);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable @Override public TileEntity createTileEntity(World world, IBlockState state) {
        return new EntropyChamberTile();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (world.getTileEntity(pos) instanceof MaterializerTile tile) {
            tile.dopItems();
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(ModBlocks.ENTROPY_CHAMBER.get());
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) TileEntityGuiFactory.open(playerIn, pos);
        return true;
    }
}
