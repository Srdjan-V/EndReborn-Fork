package io.github.srdjanv.endreforked.compat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraftforge.fml.common.Loader;

import io.github.srdjanv.endreforked.compat.tconstruct.TConstruct;
import io.github.srdjanv.endreforked.utils.Initializer;

public class CompatManger implements Initializer {

    private static CompatManger instance;

    public static CompatManger getInstance() {
        return instance == null ? new CompatManger() : instance;
    }

    public static void invalidate() {
        instance = null;
    }

    private final List<ModCompat> mods;

    private CompatManger() {
        this.mods = Stream.of(new TConstruct())
                .filter(mod -> Loader.isModLoaded(mod.modID())).collect(Collectors.toList());
    }

    @Override
    public void preInit() {
        for (ModCompat mod : mods) mod.preInit();
    }

    @Override
    public void init() {
        for (ModCompat mod : mods) mod.init();
    }

    @Override
    public void postInit() {
        for (ModCompat mod : mods) mod.postInit();
    }

    public interface ModCompat extends Initializer {

        String modID();
    }
}
