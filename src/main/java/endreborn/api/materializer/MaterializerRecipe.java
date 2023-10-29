package endreborn.api.materializer;

import java.util.List;
import java.util.function.BiFunction;

import net.minecraft.item.ItemStack;

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public final class MaterializerRecipe {

    private final ItemStack input;
    private final BiFunction<ItemStack, Catalyst, ItemStack> output;
    private final Int2ObjectLinkedOpenHashMap<CriticalityEvent> criticalityEvents;
    private final List<String> craftDescription;
    private final int ticksToComplete;

    public MaterializerRecipe(ItemStack input, BiFunction<ItemStack, Catalyst, ItemStack> output1,
                              int ticksToComplete,
                              Int2ObjectLinkedOpenHashMap<CriticalityEvent> criticalityEvents,
                              List<String> craftDescription) {
        this.input = input;
        this.output = output1;
        this.ticksToComplete = ticksToComplete;
        this.criticalityEvents = criticalityEvents;
        this.craftDescription = craftDescription;
    }

    public MaterializerRecipe(ItemStack input, BiFunction<ItemStack, Catalyst, ItemStack> output,
                              int ticksToComplete) {
        this.input = input;
        this.output = output;
        this.ticksToComplete = ticksToComplete;
        this.criticalityEvents = new Int2ObjectLinkedOpenHashMap<>();
        this.craftDescription = new ObjectArrayList<>();
    }

    public boolean registerCriticality(int progress, CriticalityEvent event) {
        if (progress <= 0 || progress > 100) return false;
        criticalityEvents.put(progress, event);
        return true;
    }

    public void addDescription(String description) {
        craftDescription.add(description);
    }

    public ItemStack getInput() {
        return input;
    }

    public List<String> getCraftDescription() {
        return craftDescription;
    }

    public BiFunction<ItemStack, Catalyst, ItemStack> getOutput() {
        return output;
    }

    public Int2ObjectLinkedOpenHashMap<CriticalityEvent> getCriticalityEvents() {
        return criticalityEvents;
    }

    public int getTicksToComplete() {
        return ticksToComplete;
    }
}
