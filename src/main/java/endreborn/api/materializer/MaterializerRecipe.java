package endreborn.api.materializer;

import java.util.List;
import java.util.function.BiFunction;

import net.minecraft.item.ItemStack;

import endreborn.api.base.Recipe;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public final class MaterializerRecipe extends Recipe<ItemStack, ItemStack, ItemStack> {

    private final Int2ObjectLinkedOpenHashMap<CriticalityEvent> criticalityEvents;
    private final List<String> craftDescription;

    public MaterializerRecipe(ItemStack input, int ticksToComplete,
                              BiFunction<ItemStack, ItemStack, ItemStack> function) {
        super(input, ticksToComplete, function);
        this.criticalityEvents = new Int2ObjectLinkedOpenHashMap<>();
        this.craftDescription = new ObjectArrayList<>();
    }

    public boolean registerCriticality(int progress, CriticalityEvent event) {
        if (progress <= 0 || progress > 100) return false;
        criticalityEvents.put(progress, event);
        craftDescription.add(String.format("has a %d chance to convert %s to %s", event.getChance(), "NOT IMPLEMENTED",
                "NOT IMPLEMENTED"));
        return true;
    }

    public List<String> getCraftDescription() {
        return craftDescription;
    }

    public Int2ObjectLinkedOpenHashMap<CriticalityEvent> getCriticalityEvents() {
        return criticalityEvents;
    }
}
