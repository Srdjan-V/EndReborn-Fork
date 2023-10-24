package endreborn.client.gui;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import endreborn.api.materializer.MaterializerHandler;

public class MaterializerCatalystSlot extends SlotItemHandler {

    public MaterializerCatalystSlot(ItemStackHandler inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return MaterializerHandler.findCatalyst(stack) != null;
    }
}
