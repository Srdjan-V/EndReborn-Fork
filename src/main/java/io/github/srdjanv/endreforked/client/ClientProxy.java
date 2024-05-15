package io.github.srdjanv.endreforked.client;

import io.github.srdjanv.endreforked.Tags;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import io.github.srdjanv.endreforked.common.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        components.add(new Registration());
    }

    @Override public void registerEventBus() {
        super.registerEventBus();
        MinecraftForge.EVENT_BUS.register(ParticleHandler.class);
        MinecraftForge.EVENT_BUS.register(TextureHandler.class);
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

    @Override public void registerItemRenderer(Item item, int meta, String postfix, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName() + postfix, id));
    }

    @Override public void registerToTextureAtlas(ResourceLocation location) {
        TextureHandler.registerFluidTexture(location);
    }

    @Override public void registerStateMapper(
            @Nullable Block block,
            @Nullable Item item,
            String file, String variantName) {
        StateMapper mapper = new StateMapper(Tags.MODID, file, variantName);
        if (item != null) {
            ModelBakery.registerItemVariants(item);
            ModelLoader.setCustomMeshDefinition(item, mapper);
        }
        if (block != null) {
            ModelLoader.setCustomStateMapper(block, mapper);
        }
    }
}
