package io.github.srdjanv.endreforked.common.items.food;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.common.ModPotions;
import io.github.srdjanv.endreforked.utils.models.InventoryItemModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class FoodEnderFlesh extends ItemFood implements InventoryItemModel {

    public FoodEnderFlesh(String name) {
        super(4, 0.4F, false);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReforked.ENDERTAB);
        setPotionEffect(new PotionEffect(ModPotions.ENDER_EYES.get(), 90 * 20, 0), 1F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format("tile.flesh.tooltip"));
    }
}
