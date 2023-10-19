package endreborn.utils;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import endreborn.common.ModItems;

public class RecipesUser {

    private static final RecipesUser INSTANCE = new RecipesUser();
    private final Table<ItemStack, ItemStack, ItemStack> smeltingList = HashBasedTable
            .<ItemStack, ItemStack, ItemStack>create();

    public static RecipesUser getInstance() {
        return INSTANCE;
    }

    private RecipesUser() {
        addAltarRecipe(new ItemStack(ModItems.CATALYST), new ItemStack(Items.IRON_INGOT),
                new ItemStack(Items.IRON_INGOT));
        addAltarRecipe(new ItemStack(ModItems.CATALYST), new ItemStack(Items.GOLD_INGOT),
                new ItemStack(Items.GOLD_INGOT));
        addAltarRecipe(new ItemStack(ModItems.CATALYST), new ItemStack(Items.REDSTONE),
                new ItemStack(Items.REDSTONE));
        addAltarRecipe(new ItemStack(ModItems.CATALYST), new ItemStack(Items.GLOWSTONE_DUST),
                new ItemStack(Items.GLOWSTONE_DUST));
        addAltarRecipe(new ItemStack(ModItems.CATALYST), new ItemStack(ModItems.TUNGSTEN_INGOT),
                new ItemStack(ModItems.TUNGSTEN_INGOT));
        addAltarRecipe(new ItemStack(ModItems.CATALYST), new ItemStack(ModItems.INGOT_ENDORIUM),
                new ItemStack(ModItems.INGOT_ENDORIUM));
    }

    public void addAltarRecipe(ItemStack input1, ItemStack input2, ItemStack result) {
        if (!getAltarResult(input1, input2).isEmpty()) return;
        this.smeltingList.put(input1, input2, result);
    }

    public ItemStack getAltarResult(ItemStack input1, ItemStack input2) {
        for (Entry<ItemStack, Map<ItemStack, ItemStack>> entry : this.smeltingList.columnMap().entrySet()) {
            if (this.compareItemStacks(input1, (ItemStack) entry.getKey())) {
                for (Entry<ItemStack, ItemStack> ent : entry.getValue().entrySet()) {
                    if (this.compareItemStacks(input2, (ItemStack) ent.getKey())) {
                        return (ItemStack) ent.getValue();
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
        return stack2.getItem() == stack1.getItem() &&
                (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public Table<ItemStack, ItemStack, ItemStack> getDualSmeltingList() {
        return this.smeltingList;
    }
}
