package io.github.srdjanv.endreforked.common.fluids.base;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.utils.models.IAsset;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BaseBlockFluid extends BlockFluidClassic implements IAsset {
    public BaseBlockFluid(String name, Fluid fluid, Material material, MapColor mapColor) {
        super(fluid, material, mapColor);
        setRegistryName(name);
        setTranslationKey(name);
    }

    public BaseBlockFluid(String name, Fluid fluid, Material material) {
        super(fluid, material);
        setRegistryName(name);
        setTranslationKey(name);
    }

    @Override public void handleAssets() {
        EndReforked.getProxy()
                .registerStateMapper(
                        this,
                        "fluids",
                        getRegistryName().getPath());
    }


}
