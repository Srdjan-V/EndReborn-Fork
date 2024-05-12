package io.github.srdjanv.endreforked.common.fluids;

import net.minecraftforge.fluids.Fluid;

public class FluidOrgana extends Fluid {
    public FluidOrgana() {
        super("organa",
                FluidUtils.getStill("organa"),
                FluidUtils.getFlowing("organa"),
                0xff321782);
    }
}
