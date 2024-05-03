package io.github.srdjanv.endreforked.client;

import io.github.srdjanv.endreforked.common.ModFluids;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

import io.github.srdjanv.endreforked.common.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        components.add(new Registration());
    }

    @Override public void registerEventBus() {
        super.registerEventBus();
        MinecraftForge.EVENT_BUS.register(ParticleHandler.class);
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
}
