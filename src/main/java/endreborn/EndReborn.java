package endreborn;

import endreborn.compat.CompatManger;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import endreborn.handlers.ConfigsHandler;
import endreborn.handlers.EndVillagerHandler;
import endreborn.handlers.RegistryHandler;
import endreborn.init.RecipesInit;
import endreborn.proxy.CommonProxy;
import endreborn.utils.GuiMainMenuEnd;
import org.jetbrains.annotations.NotNull;

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

    public static EndReborn getInstance() {
        return instance;
    }

    @SidedProxy(clientSide = Reference.CLIENTPROXY, serverSide = Reference.COMMONPROXY)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        RegistryHandler.preInitRegistries(event);
        CompatManger.getInstance().preInit();
    }

    @EventHandler
    public static void init(FMLInitializationEvent event) {
        CompatManger.getInstance().init();
        RegistryHandler.initRegistries(event);
        if (event.getSide() == Side.CLIENT && ConfigsHandler.GENERAL.spawnNewVillagers) {
            EndVillagerHandler.initIEVillagerTrades();
            EndVillagerHandler.initIEVillagerHouse();
        }
        RecipesInit.init();

        if (event.getSide() == Side.CLIENT && ConfigsHandler.GENERAL.panorama) {
            GuiMainMenuEnd.endMainMenu();
        }
    }

    @EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        CompatManger.getInstance().postInit();
        CompatManger.invalidate();
    }
}
