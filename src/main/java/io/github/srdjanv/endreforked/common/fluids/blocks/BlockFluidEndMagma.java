package io.github.srdjanv.endreforked.common.fluids.blocks;

import io.github.srdjanv.endreforked.common.ModFluids;
import io.github.srdjanv.endreforked.common.fluids.base.BaseBlockFluid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockFluidEndMagma extends BaseBlockFluid {
    public BlockFluidEndMagma() {
        super("end_magma",ModFluids.END_MAGMA.get(), Material.LAVA, MapColor.RED);

        setHardness(100.0F).setLightLevel(1.0F);
        enableStats = false;
    }

}
