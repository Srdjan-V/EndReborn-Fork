package io.github.srdjanv.endreforked;

import java.util.Objects;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import io.github.srdjanv.endreforked.client.ClientProxy;
import io.github.srdjanv.endreforked.common.CommonProxy;
import io.github.srdjanv.endreforked.common.comands.EndRebornCommands;
import io.github.srdjanv.endreforked.common.network.servertoclient.PlayerParticlePacket;
import io.github.srdjanv.endreforked.common.network.servertoclient.TpParticlePacket;
import io.github.srdjanv.endreforked.compat.CompatManger;

@Mod(modid = Tags.MODID, name = Tags.MODNAME, version = Tags.VERSION, dependencies = Tags.MOD_DEPS)
public class EndReforked {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MODID);
    public static final CreativeTabs ENDERTAB = new CreativeTabs("endertab") {

        @Override
        public @NotNull ItemStack createIcon() {
            return new ItemStack(Items.ENDER_PEARL);
        }
    };

    static {
        FluidRegistry.enableUniversalBucket();
    }

    public static SimpleNetworkWrapper NET = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MODID + "Chanel");
    private int netId = 0;

    public static EndReforked instance;

    private static CommonProxy proxy;

    public static CommonProxy getProxy() {
        return Objects.requireNonNull(proxy);
    }

    public EndReforked() {
        instance = this;
        proxy = FMLCommonHandler.instance().getSide().isClient() ? new ClientProxy(Side.CLIENT) :
                new CommonProxy(Side.SERVER);
        proxy.registerEventBus();
        MinecraftForge.EVENT_BUS.register(EndReforked.class);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        NET.registerMessage(PlayerParticlePacket.Handler.INSTANCE, PlayerParticlePacket.class, netId++, Side.CLIENT);
        NET.registerMessage(TpParticlePacket.Handler.INSTANCE, TpParticlePacket.class, netId++, Side.CLIENT);
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
    public void onServerStarted(FMLServerStartedEvent event) {
        proxy.onServerStarted(event);
    }

    @EventHandler
    public void onServerStopped(FMLServerStoppedEvent event) {
        proxy.onServerStopped(event);
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
