package endreborn.api.materializer;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import endreborn.common.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public final class MaterializerHandler {

    private static final Map<IdentityItemStack, Catalyst> catalysts = new Object2ObjectOpenHashMap<>();

    @Nullable
    public static Catalyst findCatalyst(ItemStack stack) {
        if (stack.isEmpty()) return null;
        return catalysts.get(new IdentityItemStack(stack));
    }

    @Nullable
    public static Catalyst findCatalyst(Item stack) {
        return catalysts.get(new IdentityItemStack(stack));
    }

    public static Collection<Catalyst> getCatalysts() {
        return catalysts.values();
    }

    public static Catalyst registerCatalyst(Catalyst catalyst) {
        catalysts.put(new IdentityItemStack(catalyst.getCatalyst()), catalyst);
        return catalyst;
    }

    @Nullable
    public static MaterializerRecipe findRecipe(@Nullable Catalyst catalyst, ItemStack stack) {
        if (Objects.isNull(catalyst) || stack.isEmpty()) return null;
        return catalyst.getRecipes().get(new IdentityItemStack(stack));
    }

    @Nullable
    public static MaterializerRecipe findRecipe(@Nullable Catalyst catalyst, Item stack) {
        if (Objects.isNull(catalyst)) return null;
        return catalyst.getRecipes().get(new IdentityItemStack(stack));
    }

    public static Collection<MaterializerRecipe> getRecipes() {
        return catalysts.values().stream().map(Catalyst::getRecipes)
                .flatMap(map -> map.values().stream()).collect(Collectors.toList());
    }

    public static MaterializerRecipe registerRecipe(@Nullable Catalyst catalyst, MaterializerRecipe recipe) {
        if (Objects.isNull(catalyst)) return null;
        catalyst.getRecipes().put(new IdentityItemStack(recipe.getInput()), recipe);
        return recipe;
    }

    static {
        var catalyst1 = registerCatalyst(new Catalyst(
                new ItemStack(ModItems.CATALYST.get())));

        var tmp = new Int2ObjectLinkedOpenHashMap<CriticalityEvent>();
        tmp.put(15, CriticalityEvent.create(15,
                CriticalityEvent.equalsToBlock(Blocks.STONE),
                CriticalityEvent.replaceWithDefaultBlockState(Blocks.IRON_BLOCK)));

        tmp.put(20, CriticalityEvent.create(15,
                CriticalityEvent.equalsToBlock(Blocks.IRON_BLOCK),
                CriticalityEvent.replaceWithDefaultBlockState(Blocks.GOLD_BLOCK)));

        tmp.put(30, CriticalityEvent.create(15,
                CriticalityEvent.equalsToBlock(Blocks.GOLD_BLOCK),
                CriticalityEvent.replaceWithDefaultBlockState(Blocks.STONE)));

        registerRecipe(catalyst1, new MaterializerRecipe(
                new ItemStack(Items.IRON_INGOT),
                (stack, catalyst) -> new ItemStack(Items.IRON_INGOT, 2),
                120, tmp));

        /*
         * var tmp2 = new Int2ObjectLinkedOpenHashMap<CriticalityEvent>();
         * tmp2.put(10, CriticalityEvent.createBlock2BlockEvent(100, Blocks.STONE, Blocks.LAVA));
         * tmp2.put(20, CriticalityEvent.createBlock2BlockEvent(100, Blocks.LAVA, Blocks.GLOWSTONE));
         * tmp2.put(30, CriticalityEvent.createBlock2BlockEvent(100, Blocks.GLOWSTONE, Blocks.OBSIDIAN));
         * tmp2.put(40, CriticalityEvent.createBlock2BlockEvent(100, Blocks.OBSIDIAN, Blocks.OAK_FENCE_GATE));
         * tmp2.put(50, CriticalityEvent.createBlock2BlockEvent(100, Blocks.OAK_FENCE_GATE, Blocks.PUMPKIN));
         * tmp2.put(60, CriticalityEvent.createBlock2BlockEvent(100, Blocks.PUMPKIN, Blocks.GLASS));
         * tmp2.put(70, CriticalityEvent.createBlock2BlockEvent(100, Blocks.GLASS, Blocks.OBSIDIAN));
         * tmp2.put(80, CriticalityEvent.createBlock2BlockEvent(100, Blocks.OBSIDIAN, Blocks.STONE));
         * 
         * 
         * registerRecipe(new MaterializerRecipe(
         * 30,
         * new ItemStack(Items.GOLDEN_APPLE),
         * (stack, catalyst) -> new ItemStack(Items.IRON_INGOT, 2),
         * tmp2,
         * 20));
         */

    }
}
