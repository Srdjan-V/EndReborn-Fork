package endreborn.api.materializer;

import endreborn.common.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public final class MaterializerHandler {

    private static final Map<HashableItemStack, Catalyst> catalysts = new Object2ObjectOpenHashMap<>();
    private static final Map<HashableItemStack, MaterializerRecipe> recipes = new Object2ObjectOpenHashMap<>();

    @Nullable
    public static Catalyst findCatalyst(ItemStack stack) {
        return catalysts.get(new HashableItemStack(stack));
    }

    @Nullable
    public static Catalyst findCatalyst(Item stack) {
        return catalysts.get(new HashableItemStack(stack));
    }

    public static Collection<Catalyst> getCatalysts() {
        return catalysts.values();
    }

    public static Catalyst registerCatalyst(Catalyst catalyst) {
        catalysts.put(new HashableItemStack(catalyst.getCatalyst()), catalyst);
        return catalyst;
    }

    @Nullable
    public static MaterializerRecipe findRecipe(ItemStack stack) {
        return recipes.get(new HashableItemStack(stack));
    }

    @Nullable
    public static MaterializerRecipe findRecipe(Item stack) {
        return recipes.get(new HashableItemStack(stack));
    }

    public static Collection<MaterializerRecipe> getRecipes() {
        return recipes.values();
    }

    public static MaterializerRecipe registerRecipe(MaterializerRecipe recipe) {
        recipes.put(new HashableItemStack(recipe.input), recipe);
        return recipe;
    }

    static {
        registerCatalyst(new Catalyst(
                new ItemStack(ModItems.CATALYST.get()),
                60,
                10));

        var tmp = new Int2ObjectLinkedOpenHashMap<CriticalityEvent>();
        tmp.put(15, CriticalityEvent.createBlock2BlockEvent(15, Blocks.STONE, Blocks.IRON_BLOCK));
        tmp.put(20, CriticalityEvent.createBlock2BlockEvent(20, Blocks.IRON_BLOCK, Blocks.GOLD_BLOCK));
        tmp.put(30, CriticalityEvent.createBlock2BlockEvent(30, Blocks.GOLD_BLOCK, Blocks.STONE));

        registerRecipe(new MaterializerRecipe(
                30,
                new ItemStack(Items.IRON_INGOT),
                (stack, catalyst) -> new ItemStack(Items.IRON_INGOT, 2),
                tmp,
                120));

    }
}
