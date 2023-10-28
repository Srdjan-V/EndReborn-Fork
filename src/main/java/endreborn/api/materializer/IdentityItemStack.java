package endreborn.api.materializer;

import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.Nullable;

public final class IdentityItemStack {

    private final Item item;
    private final NBTTagCompound nbtTagCompound;

    public IdentityItemStack(ItemStack itemStack) {
        this.item = itemStack.getItem();
        this.nbtTagCompound = itemStack.getTagCompound();
    }

    public IdentityItemStack(Item item) {
        this.item = item;
        this.nbtTagCompound = null;
    }

    public Item getItem() {
        return item;
    }

    @Nullable
    public NBTTagCompound getNbtTagCompound() {
        return nbtTagCompound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentityItemStack that = (IdentityItemStack) o;
        return (item == that.item) && Objects.equals(nbtTagCompound, that.nbtTagCompound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, nbtTagCompound);
    }
}
