package endreborn;

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

import endreborn.common.CommonProxy;
import endreborn.compat.CompatManger;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class EndReborn {

    public static final Logger LOGGER = LogManager.getLogger(Reference.MODID);
    public static final CreativeTabs endertab = new CreativeTabs("endertab") {

        @Override
        public @NotNull ItemStack createIcon() {
            return new ItemStack(Items.ENDER_PEARL);
        }
    };

    @Instance
    public static EndReborn instance;

    @SidedProxy(clientSide = Reference.CLIENTPROXY, serverSide = Reference.COMMONPROXY)
    public static CommonProxy proxy;

    public EndReborn() {
        MinecraftForge.EVENT_BUS.register(EndReborn.class);
        proxy.registerEventBus();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
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
        if (event.getModID().equals(Reference.MODID)) {
            ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
        }
    }
}
