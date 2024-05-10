package io.github.srdjanv.endreforked.common.comands.dev;

import io.github.srdjanv.endreforked.Tags;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class DevConfigs {
    public final static Configuration CONFIGURATION = new Configuration(new File("config/" + Tags.MODID + "/dev.cfg"));

    public final static Property DISABLE_REGION_LOADING = CONFIGURATION.get("dev", "disable_region_loading", false);

    static {
        sync();
    }

    public static void sync() {
        if (CONFIGURATION.hasChanged()) CONFIGURATION.save();
    }
}
