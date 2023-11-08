package endreborn.utils;

import net.minecraft.item.ItemStack;

public final class ItemStackUtils {

    public static ItemStack copyStackWithAmount(ItemStack stack, int amount) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;
        ItemStack s2 = stack.copy();
        s2.setCount(amount);
        return s2;
    }

    private ItemStackUtils() {}
}
