package io.github.srdjanv.endreforked.common.fluids;

import io.github.srdjanv.endreforked.Tags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidEntropy extends Fluid {
    public FluidEntropy() {
        super("entropy",
                FluidUtils.getStill("entropy"),
                FluidUtils.getFlowing("entropy"),
                0xff321782);
    }
}
