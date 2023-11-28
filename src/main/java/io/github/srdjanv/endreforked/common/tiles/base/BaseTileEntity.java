package io.github.srdjanv.endreforked.common.tiles.base;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import org.jetbrains.annotations.Nullable;

public class BaseTileEntity extends TileEntity {

    public BaseTileEntity() {}

    protected class InternalItemStackHandler extends ItemStackHandler {

        public InternalItemStackHandler() {}

        public InternalItemStackHandler(int size) {
            super(size);
        }

        public InternalItemStackHandler(NonNullList<ItemStack> stacks) {
            super(stacks);
        }

        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    }

    protected class InternalFluidTank extends FluidTank {

        public InternalFluidTank(int capacity) {
            super(capacity);
        }

        public InternalFluidTank(@Nullable FluidStack fluidStack, int capacity) {
            super(fluidStack, capacity);
        }

        public InternalFluidTank(Fluid fluid, int amount, int capacity) {
            super(fluid, amount, capacity);
        }

        @Override
        protected void onContentsChanged() {
            markDirty();
        }
    }
}
