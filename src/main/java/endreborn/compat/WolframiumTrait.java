package endreborn.compat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;

import slimeknights.tconstruct.library.traits.AbstractTrait;

public class WolframiumTrait extends AbstractTrait {

    public WolframiumTrait() {
        super("wolframium", TextFormatting.GRAY);
    }

    @Override
    public void onHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage,
                      boolean isCritical) {
        if (player.world.getCurrentMoonPhaseFactor() == 1.0F)
            player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 256, 1));
    }
}
