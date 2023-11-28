package io.github.srdjanv.endreforked.common.items.tools;

import net.minecraft.item.ItemBow;

import io.github.srdjanv.endreforked.EndReforked;

public class ToolBow extends ItemBow {

    public ToolBow(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReforked.endertab);
        setMaxStackSize(1);
    }
}
