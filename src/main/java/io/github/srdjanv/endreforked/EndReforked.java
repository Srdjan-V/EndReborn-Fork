package io.github.srdjanv.endreforked;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import io.github.srdjanv.endreforked.common.CommonProxy;
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

    @Instance
    public static EndReforked instance;

    @SidedProxy(clientSide = Tags.CLIENTPROXY, serverSide = Tags.COMMONPROXY)
    public static CommonProxy proxy;

    public EndReforked() {
        MinecraftForge.EVENT_BUS.register(EndReforked.class);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.registerEventBus();
        proxy.preInit(event);
    }

    @EventHandler
    public static void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        CompatManger.invalidate();
    }

    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Tags.MODID)) {
            ConfigManager.sync(Tags.MODID, Config.Type.INSTANCE);
        }
    }
}
