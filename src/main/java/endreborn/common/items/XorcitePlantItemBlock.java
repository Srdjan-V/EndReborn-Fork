package endreborn.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import endreborn.common.ModItems;

public class XorcitePlantItemBlock extends ItemBlock {

    protected Block block;

    public XorcitePlantItemBlock(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setNoRepair();
        this.block = block;

        ModItems.ITEMS.add(this);
    }

    public int getMetadata(int i) {
        return i;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack);
    }
}
