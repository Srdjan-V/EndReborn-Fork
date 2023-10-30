package endreborn.common;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import endreborn.common.enchants.EnchantECore;
import endreborn.common.enchants.EnchantEnder;
import endreborn.common.enchants.EnchantSCore;

@SuppressWarnings("unused")
public final class ModEnchants {

    public static final Enchantment ender_core = new EnchantECore();
    public static final Enchantment ender_killer = new EnchantEnder();
    public static final Enchantment shulker_core = new EnchantSCore();

    public static final Enchantment[] helmetEnchants = new Enchantment[] { ender_core };
    public static final Enchantment[] chestplateEnchants = new Enchantment[] { ender_core };
    public static final Enchantment[] leggingsEnchants = new Enchantment[] { ender_core };
    public static final Enchantment[] bootsEnchants = new Enchantment[] { ender_core };
    public static final Enchantment[] swordEnchants = new Enchantment[] { shulker_core, ender_killer };

    @SubscribeEvent
    public static void onEvent(final RegistryEvent.Register<Enchantment> event) {
        final IForgeRegistry<Enchantment> registry = event.getRegistry();

        registry.register(ender_core);
        registry.register(ender_killer);
        registry.register(shulker_core);
    }
}
