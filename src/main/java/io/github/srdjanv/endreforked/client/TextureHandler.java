package io.github.srdjanv.endreforked.client;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Set;

public final class TextureHandler {
    private static final Set<ResourceLocation> fluidTextures = new ObjectArraySet<>();

    public static void registerFluidTexture(ResourceLocation fluidTexture) {
        fluidTextures.add(fluidTexture);
    }

    @SubscribeEvent
    static void onRegisterTextures(TextureStitchEvent.Pre event) {
        for (ResourceLocation fluidTexture : fluidTextures) {
            event.getMap().registerSprite(fluidTexture);
        }
    }
}
