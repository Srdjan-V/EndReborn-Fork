package io.github.srdjanv.endreforked.common;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.google.common.collect.Lists;

import io.github.srdjanv.endreforked.common.capabilities.timedflight.CapabilityTimedFlightHandler;
import io.github.srdjanv.endreforked.common.datafixers.Fixer;
import io.github.srdjanv.endreforked.compat.CompatManger;
import io.github.srdjanv.endreforked.utils.Initializer;

public class CommonProxy {

    protected final List<Initializer> components = Lists.newArrayList();

    public CommonProxy() {
        components.add(CompatManger.getInstance());
        components.add(Fixer.getInstance());
        components.add(new Registration());
        components.add(new LootHandler());
    }

    public void registerEventBus() {
        for (Initializer component : components) component.registerEventBus();
        MinecraftForge.EVENT_BUS.register(CapabilityTimedFlightHandler.class);
        MinecraftForge.EVENT_BUS.register(ModBlocks.class);
        MinecraftForge.EVENT_BUS.register(ModItems.class);
        MinecraftForge.EVENT_BUS.register(ModEnchants.class);
        MinecraftForge.EVENT_BUS.register(ModSounds.class);
        MinecraftForge.EVENT_BUS.register(ModPotions.class);
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
    }

    public void preInit(FMLPreInitializationEvent event) {
        for (Initializer component : components) component.preInit();
    }

    public void init(FMLInitializationEvent event) {
        for (Initializer component : components) component.init();
    }

    public void postInit(FMLPostInitializationEvent event) {
        for (Initializer component : components) component.postInit();
    }

    public void registerItemRenderer(Item item, int meta, String id) {}
}
