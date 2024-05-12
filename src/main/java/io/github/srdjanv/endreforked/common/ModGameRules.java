package io.github.srdjanv.endreforked.common;

import io.github.srdjanv.endreforked.Tags;
import io.github.srdjanv.endreforked.utils.Initializer;
import net.minecraft.world.GameRules;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

public final class ModGameRules implements Initializer {

    @Override public void onServerStarted(FMLServerStartedEvent event) {
        var rules = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0).getGameRules();

        if (FMLLaunchHandler.isDeobfuscatedEnvironment()) {
            rules.addGameRule(Tags.MODID + "_dev_disable_region_loading", String.valueOf(false), GameRules.ValueType.BOOLEAN_VALUE);
        }
    }
}
