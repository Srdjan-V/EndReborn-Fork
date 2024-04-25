package io.github.srdjanv.endreforked.common;

import com.google.common.base.Suppliers;
import io.github.srdjanv.endreforked.common.fluids.FluidEndMagma;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.function.Supplier;

public class ModFluids {
    public static final Supplier<FluidEndMagma> END_MAGMA = register(FluidEndMagma::new);

    public static <F extends Fluid> Supplier<F> register(com.google.common.base.Supplier<F> supplier) {
        Supplier<F> memorized = Suppliers.memoize(()-> {
            var fluid = supplier.get();
            FluidRegistry.registerFluid(fluid);
            return fluid;
        });
        return memorized;
    }

}
