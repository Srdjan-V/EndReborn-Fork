package endreborn.common.capabilities.timedflight;

import static endreborn.common.capabilities.timedflight.CapabilityTimedFlightHandler.TIMED_FLIGHT_CAPABILITY;

import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TimedFlightCapabilityProvider implements ICapabilitySerializable<NBTTagInt> {

    private final TimedFlight timedFlight = TIMED_FLIGHT_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == TIMED_FLIGHT_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == TIMED_FLIGHT_CAPABILITY ? (T) timedFlight : null;
    }

    @Override
    public NBTTagInt serializeNBT() {
        return (NBTTagInt) TIMED_FLIGHT_CAPABILITY.writeNBT(timedFlight, null);
    }

    @Override
    public void deserializeNBT(NBTTagInt nbt) {
        TIMED_FLIGHT_CAPABILITY.readNBT(timedFlight, null, nbt);
    }
}
