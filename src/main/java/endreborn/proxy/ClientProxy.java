package endreborn.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import endreborn.client.ClientHandler;
import endreborn.common.CommonProxy;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerEventBus() {
        super.registerEventBus();
        MinecraftForge.EVENT_BUS.register(ClientHandler.class);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ClientHandler.preInit();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ClientHandler.init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        ClientHandler.postInit();
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
}
