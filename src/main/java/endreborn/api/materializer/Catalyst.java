package endreborn.api.materializer;

import net.minecraft.item.ItemStack;

public final class Catalyst {

    private final ItemStack catalyst;
    private final int criticality;
    private final int worldCoruptionRange;

    public Catalyst(ItemStack catalyst, int criticality, int worldCoruptionRange) {
        this.catalyst = catalyst;
        this.criticality = criticality;
        this.worldCoruptionRange = worldCoruptionRange;
    }

    public ItemStack getCatalyst() {
        return catalyst;
    }

    public int getCriticality() {
        return criticality;
    }

    public int getWorldCoruptionRange() {
        return worldCoruptionRange;
    }
}
