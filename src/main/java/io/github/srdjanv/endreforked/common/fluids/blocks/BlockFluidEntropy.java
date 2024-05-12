package io.github.srdjanv.endreforked.common.fluids.blocks;

import io.github.srdjanv.endreforked.common.ModFluids;
import io.github.srdjanv.endreforked.common.tiles.passiveinducers.FluidEntropyTile;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import org.jetbrains.annotations.Nullable;

public class BlockFluidEntropy extends BlockFluidFinite {
    public BlockFluidEntropy() {
        super(ModFluids.ENTROPY.get(), Material.WATER);

        setTranslationKey("entropy_fluid");
        setRegistryName("entropy_fluid");

        setHardness(100.0F).setLightLevel(1.0F);
        enableStats = false;
    }

    @Override public boolean hasTileEntity(IBlockState state) {
        return state.getValue(LEVEL) == 0;
    }

    @Nullable @Override public TileEntity createTileEntity(World world, IBlockState state) {
        return state.getValue(LEVEL) == 0 ? new FluidEntropyTile() : null;
    }
}
