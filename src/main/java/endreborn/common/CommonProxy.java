package endreborn.common;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import endreborn.common.datafixers.Fixer;

public class CommonProxy {

    public void registerEventBus() {
        MinecraftForge.EVENT_BUS.register(ModBlocks.class);
        MinecraftForge.EVENT_BUS.register(ModItems.class);
        MinecraftForge.EVENT_BUS.register(ModEnchants.class);
        MinecraftForge.EVENT_BUS.register(ModSounds.class);
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
        MinecraftForge.EVENT_BUS.register(LootTableHandler.class);
    }

    public void preInit(FMLPreInitializationEvent event) {
        Fixer.init();
        CommonHandler.preInit();
        LootTableHandler.preInit();
    }

    public void init(FMLInitializationEvent event) {
        CommonHandler.init();
    }

    public void postInit(FMLPostInitializationEvent event) {
        CommonHandler.postInit();
    }

    public void registerItemRenderer(Item item, int meta, String id) {}
}
