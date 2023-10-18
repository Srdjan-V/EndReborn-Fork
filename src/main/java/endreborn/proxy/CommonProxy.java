package endreborn.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import endreborn.common.CommonHandler;
import endreborn.common.EventHandler;
import endreborn.common.LootTableHandler;

public class CommonProxy {

    public void registerEventBus() {
        MinecraftForge.EVENT_BUS.register(CommonHandler.class);
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
        MinecraftForge.EVENT_BUS.register(LootTableHandler.class);
    }

    public void preInit(FMLPreInitializationEvent event) {
        CommonHandler.preInit();
    }

    public void init(FMLInitializationEvent event) {
        CommonHandler.init();
    }

    public void postInit(FMLPostInitializationEvent event) {
        CommonHandler.postInit();
    }

    public void registerItemRenderer(Item item, int meta, String id) {}

    public void registerVariantRenderer(Item item, int meta, String filename, String id) {}
}
