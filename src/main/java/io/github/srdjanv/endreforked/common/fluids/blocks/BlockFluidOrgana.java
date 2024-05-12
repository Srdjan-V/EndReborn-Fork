package io.github.srdjanv.endreforked.common.fluids.blocks;

import io.github.srdjanv.endreforked.common.ModFluids;
import io.github.srdjanv.endreforked.common.tiles.passiveinducers.FluidEntropyTile;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import org.jetbrains.annotations.Nullable;

public class BlockFluidOrgana extends BlockFluidFinite {
    public BlockFluidOrgana() {
        super(ModFluids.ORGANA.get(), Material.WATER);

        setTranslationKey("organa_fluid");
        setRegistryName("organa_fluid");
    }

    @Override public boolean hasTileEntity(IBlockState state) {
        return state.getValue(LEVEL) == 0;
    }

    @Nullable @Override public TileEntity createTileEntity(World world, IBlockState state) {
        return state.getValue(LEVEL) == 0 ? new FluidEntropyTile() : null;
    }
}
