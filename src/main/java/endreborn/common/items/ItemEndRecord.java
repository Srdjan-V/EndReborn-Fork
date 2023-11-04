package endreborn.common.items;

import net.minecraft.item.ItemRecord;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import endreborn.EndReborn;
import endreborn.common.sounds.EndSound;
import endreborn.utils.IHasModel;

public class ItemEndRecord extends ItemRecord implements IHasModel {

    public ItemEndRecord(String name, EndSound soundIn) {
        super(soundIn.name, soundIn);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getRecordNameLocal() {
        return I18n.translateToLocal("item.endreborn.record.end.desc");
    }

    @Override
    public void registerModels() {
        EndReborn.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
