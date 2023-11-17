package endreborn.common;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import com.cleanroommc.modularui.utils.Color;

public final class ModPotions {

    public static final Potion ENDER_EYES = new EnderEyes();

    @SubscribeEvent
    public static void onEvent(final RegistryEvent.Register<Potion> event) {
        final IForgeRegistry<Potion> registry = event.getRegistry();

        registry.register(ENDER_EYES);
    }

    public static class EnderEyes extends Potion {

        protected EnderEyes() {
            super(false, Color.argb(225, 225, 225, 225));
            setRegistryName("ender_eyes");
            setPotionName("effect.ender_eyes");
        }
    }
}
