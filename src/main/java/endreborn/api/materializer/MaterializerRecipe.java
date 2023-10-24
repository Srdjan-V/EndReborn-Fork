package endreborn.api.materializer;

import java.util.function.BiFunction;

import net.minecraft.item.ItemStack;

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;

public final class MaterializerRecipe {

    public final int minimumCriticality;
    public final ItemStack input;
    public final BiFunction<ItemStack, Catalyst, ItemStack> output;
    public final Int2ObjectLinkedOpenHashMap<CriticalityEvent> criticalityEvents;
    public final int ticksToCompleate;

    public MaterializerRecipe(int minimumCriticality,
                              ItemStack input, BiFunction<ItemStack, Catalyst, ItemStack> output1,
                              Int2ObjectLinkedOpenHashMap<CriticalityEvent> criticalityEvents,
                              int ticksToCompleate) {
        this.minimumCriticality = minimumCriticality;
        this.input = input;
        this.output = output1;

        this.criticalityEvents = criticalityEvents;
        this.ticksToCompleate = ticksToCompleate;
    }
}
