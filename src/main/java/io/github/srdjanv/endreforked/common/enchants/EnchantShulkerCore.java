package io.github.srdjanv.endreforked.common.enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;

import io.github.srdjanv.endreforked.Tags;

public class EnchantShulkerCore extends Enchantment {

    public EnchantShulkerCore() {
        super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
        setRegistryName(Tags.MODID, "shulker_core");
        setName("shulker_core");
    }

    public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
        if (target instanceof EntityLivingBase) {
            ((EntityLivingBase) target).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 50, level + 2));
        }
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
