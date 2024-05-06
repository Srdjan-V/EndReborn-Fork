package io.github.srdjanv.endreforked.compat.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import io.github.srdjanv.endreforked.common.items.EntropyWings;
import net.minecraft.item.ItemStack;

public class EntropyWingsRing extends EntropyWings implements IBauble {
    public EntropyWingsRing() {
        super("entropy_wings_ring");
    }

    @Override public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.RING;
    }
}
