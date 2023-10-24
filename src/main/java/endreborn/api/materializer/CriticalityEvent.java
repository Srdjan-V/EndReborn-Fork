package endreborn.api.materializer;

import net.minecraft.item.Item;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public final class CriticalityEvent implements Comparable<CriticalityEvent> {

    private final int chance;
    private final Pair<? extends Item, ?> criticalityItems;

    public CriticalityEvent(int chance, Pair<? extends Item, ?> criticalityItems) {
        this.chance = chance;
        this.criticalityItems = criticalityItems;
    }

    public Pair<? extends Item, ?> getCriticalityItems() {
        return criticalityItems;
    }

    public int getChance() {
        return chance;
    }

    @Override
    public int compareTo(@NotNull CriticalityEvent o) {
        return chance - o.chance;
    }
}
