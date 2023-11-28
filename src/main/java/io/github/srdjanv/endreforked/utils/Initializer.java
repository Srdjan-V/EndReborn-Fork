package io.github.srdjanv.endreforked.utils;

import net.minecraftforge.common.MinecraftForge;

public interface Initializer {

    default void registerEventBus() {}

    default void registerThisToEventBus() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    default void preInit() {}

    default void init() {}

    default void postInit() {}
}
