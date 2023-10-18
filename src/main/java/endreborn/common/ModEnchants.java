package endreborn.common;

import net.minecraft.enchantment.Enchantment;

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
}
