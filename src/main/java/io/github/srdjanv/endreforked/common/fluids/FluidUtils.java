package io.github.srdjanv.endreforked.common.fluids;

import io.github.srdjanv.endreforked.Tags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;

public class FluidUtils {
    public static ResourceLocation getStill(String fluidName) {
        return new ResourceLocation(Tags.MODID, String.format("blocks/fluids/%s", fluidName));
    }

    public static ResourceLocation getFlowing(String fluidName) {
        return new ResourceLocation(Tags.MODID, String.format("blocks/fluids/%s_flow", fluidName));
    }

}
