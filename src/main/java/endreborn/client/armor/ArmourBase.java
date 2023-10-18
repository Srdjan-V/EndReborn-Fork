package endreborn.client.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

import endreborn.EndReborn;
import endreborn.common.ModItems;
import endreborn.utils.IHasModel;

public class ArmourBase extends ItemArmor implements IHasModel {

    public ArmourBase(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
        super(materialIn, renderIndexIn, equipmentSlotIn);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);

        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        EndReborn.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
