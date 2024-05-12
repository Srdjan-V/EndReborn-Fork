package io.github.srdjanv.endreforked.common.fluids.blocks;

import io.github.srdjanv.endreforked.common.ModFluids;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidFinite;

public class BlockFluidEndMagma extends BlockFluidFinite {
    public BlockFluidEndMagma() {
        super(ModFluids.END_MAGMA.get(), Material.LAVA);

        setTranslationKey("end_magma_fluid");
        setRegistryName("end_magma_fluid");

        setHardness(100.0F).setLightLevel(1.0F);
        enableStats = false;
    }

}
