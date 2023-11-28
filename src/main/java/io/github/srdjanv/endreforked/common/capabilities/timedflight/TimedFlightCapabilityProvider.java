package io.github.srdjanv.endreforked.common.capabilities.timedflight;

import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TimedFlightCapabilityProvider implements ICapabilitySerializable<NBTTagInt> {

    private final TimedFlight timedFlight = CapabilityTimedFlightHandler.TIMED_FLIGHT_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityTimedFlightHandler.TIMED_FLIGHT_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityTimedFlightHandler.TIMED_FLIGHT_CAPABILITY ? (T) timedFlight : null;
    }

    @Override
    public NBTTagInt serializeNBT() {
        return (NBTTagInt) CapabilityTimedFlightHandler.TIMED_FLIGHT_CAPABILITY.writeNBT(timedFlight, null);
    }

    @Override
    public void deserializeNBT(NBTTagInt nbt) {
        CapabilityTimedFlightHandler.TIMED_FLIGHT_CAPABILITY.readNBT(timedFlight, null, nbt);
    }
}
