package endreborn.api.materializer;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public final class CriticalityEvent implements Comparable<CriticalityEvent> {

    private final int chance;
    private final Pair<Block, ?> criticalityItems;

    public static CriticalityEvent createBlock2BlockEvent(int chance, Block in, Block out) {
        return new CriticalityEvent(chance, new ImmutablePair<>(in, out));
    }

    public static CriticalityEvent createBlock2ItemEvent(int chance, Block in, Item out) {
        return new CriticalityEvent(chance, new ImmutablePair<>(in, out));
    }

    public static CriticalityEvent createBlock2ItemStackEvent(int chance, Block in, ItemStack out) {
        return new CriticalityEvent(chance, new ImmutablePair<>(in, out));
    }

    // TODO: 24/10/2023 add more event types?

    private CriticalityEvent(int chance, Pair<Block, ?> criticalityItems) {
        this.chance = chance;
        this.criticalityItems = criticalityItems;
    }

    public Pair<Block, ?> getCriticalityItems() {
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
