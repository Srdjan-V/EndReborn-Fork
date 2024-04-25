package io.github.srdjanv.endreforked.common.fluids;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import java.awt.*;

public class FluidEndMagma extends Fluid {
    public FluidEndMagma() {
        super("endMagma",
                new ResourceLocation("endMagmaStill"),
                new ResourceLocation("endMagmaFlowing"),
                0xff321782);

        this.setLuminosity(15).setDensity(3500).setViscosity(4500).setTemperature(1600);
    }
}
