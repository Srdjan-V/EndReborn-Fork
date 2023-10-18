package endreborn.compat;

import endreborn.compat.tconstruct.TConstruct;
import net.minecraftforge.fml.common.Loader;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompatManger {
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
                .filter(mod-> Loader.isModLoaded(mod.modID())).collect(Collectors.toList());
    }

    public void preInit() {
        for (ModCompat mod : mods) mod.preInit();
    }

    public void init() {
        for (ModCompat mod : mods) mod.init();
    }

    public void postInit() {
        for (ModCompat mod : mods) mod.postInit();
    }

    public interface ModCompat {

        String modID();

        default void preInit() {}

        default void init() {}

        default void postInit() {}
    }
}
