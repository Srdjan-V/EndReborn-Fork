package io.github.srdjanv.endreforked;

import java.util.Objects;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import io.github.srdjanv.endreforked.client.ClientProxy;
import io.github.srdjanv.endreforked.common.CommonProxy;
import io.github.srdjanv.endreforked.common.comands.EndRebornCommands;
import io.github.srdjanv.endreforked.compat.CompatManger;

@Mod(modid = Tags.MODID, name = Tags.MODNAME, version = Tags.VERSION, dependencies = Tags.MOD_DEPS)
public class EndReforked {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MODID);
    public static final CreativeTabs endertab = new CreativeTabs("endertab") {

        @Override
        public @NotNull ItemStack createIcon() {
            return new ItemStack(Items.ENDER_PEARL);
        }
    };

    public static EndReforked instance;

    private static CommonProxy proxy;

    public static CommonProxy getProxy() {
        return Objects.requireNonNull(proxy);
    }

    public EndReforked() {
        instance = this;
        proxy = FMLCommonHandler.instance().getSide().isClient() ? new ClientProxy() : new CommonProxy();
        proxy.registerEventBus();
        MinecraftForge.EVENT_BUS.register(EndReforked.class);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        CompatManger.invalidate();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent evt) {
        evt.registerServerCommand(new EndRebornCommands());
    }

    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Tags.MODID)) {
            ConfigManager.sync(Tags.MODID, Config.Type.INSTANCE);
        }
    }
}
