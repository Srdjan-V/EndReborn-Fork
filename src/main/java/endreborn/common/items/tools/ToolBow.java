package endreborn.common.items.tools;

import net.minecraft.item.ItemBow;

import endreborn.EndReborn;

public class ToolBow extends ItemBow {

    public ToolBow(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
        setMaxStackSize(1);
    }
}
