package io.github.srdjanv.endreforked.api.entropy;

import io.github.srdjanv.endreforked.api.base.HandlerRegistry;
import io.github.srdjanv.endreforked.api.util.FluidStackHashStrategy;
import io.github.srdjanv.endreforked.api.util.ItemStackHashStrategy;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class EntropyChamberHandler {
    public static final Fluid FLUID = new Fluid();
    public static class Fluid extends HandlerRegistry<FluidStack, ChamberRecipe.Fluid> {
        private Fluid() {
        }
        @Override public Hash.Strategy<FluidStack> getHashStrategy() {
            return FluidStackHashStrategy.memorizedComparingAllButAmount();
        }
    }

    public static final Item ITEM = new Item();
    public static class Item extends HandlerRegistry<ItemStack, ChamberRecipe.Item> {
        private Item() {
        }
        @Override public Hash.Strategy<ItemStack> getHashStrategy() {
            return ItemStackHashStrategy.memorizedComparingAllButCount();
        }
    }
}
