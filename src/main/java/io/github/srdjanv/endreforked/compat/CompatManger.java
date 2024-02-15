package io.github.srdjanv.endreforked.compat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
    public void preInit(FMLPreInitializationEvent event) {
        for (ModCompat mod : mods) mod.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        for (ModCompat mod : mods) mod.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        for (ModCompat mod : mods) mod.postInit(event);
    }

    public interface ModCompat extends Initializer {

        String modID();
    }
}
