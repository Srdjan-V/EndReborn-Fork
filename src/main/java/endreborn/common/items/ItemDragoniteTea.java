package endreborn.common.items;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.Nullable;

import endreborn.EndReborn;
import endreborn.common.capabilities.timedflight.CapabilityTimedFlightHandler;
import endreborn.utils.models.InventoryItemModel;

public class ItemDragoniteTea extends ItemFood implements InventoryItemModel {

    public ItemDragoniteTea(String name) {
        super(2, 1, false);
        setTranslationKey(name);
        setRegistryName(name);
        setAlwaysEdible();
        setMaxStackSize(16);
        setCreativeTab(EndReborn.endertab);
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);

        if (entityLiving instanceof EntityPlayer player) {
            player.getCooldownTracker().setCooldown(this, 480);
            player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 500, 0));
            player.getCapability(CapabilityTimedFlightHandler.TIMED_FLIGHT_CAPABILITY, null)
                    .setFlightDuration(worldIn, player.capabilities, 500);
        }
        return stack;
    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 10;
    }

    @SideOnly(Side.CLIENT)
    public ItemStack getDefaultInstance() {
        return PotionUtils.addPotionToItemStack(super.getDefaultInstance(), PotionTypes.HEALING);
    }

    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format("tile.tea.tooltip"));
    }
}
