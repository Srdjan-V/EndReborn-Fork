package endreborn.api.materializer;

import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public final class Catalyst {

    private final ItemStack catalyst;
    private final Map<IdentityItemStack, MaterializerRecipe> recipes = new Object2ObjectOpenHashMap<>();

    public Catalyst(ItemStack catalyst) {
        this.catalyst = catalyst;
    }

    public void registerRecipe(ItemStack itemStack, MaterializerRecipe recipe) {
        recipes.put(new IdentityItemStack(itemStack), recipe);
    }

    public void registerRecipe(Item item, MaterializerRecipe recipe) {
        recipes.put(new IdentityItemStack(item), recipe);
    }

    public void registerRecipe(IdentityItemStack itemStack, MaterializerRecipe recipe) {
        recipes.put(itemStack, recipe);
    }

    public ItemStack getCatalyst() {
        return catalyst;
    }

    public Map<IdentityItemStack, MaterializerRecipe> getRecipes() {
        return recipes;
    }
}
