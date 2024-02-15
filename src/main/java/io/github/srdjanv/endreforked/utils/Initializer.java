package io.github.srdjanv.endreforked.utils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface Initializer {

    default void registerEventBus() {}

    default void registerThisToEventBus() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    default void preInit(FMLPreInitializationEvent event) {}

    default void init(FMLInitializationEvent event) {}

    default void postInit(FMLPostInitializationEvent event) {}

    default boolean dispose() {
        return false;
    }

    default void onDispose() {}
}
