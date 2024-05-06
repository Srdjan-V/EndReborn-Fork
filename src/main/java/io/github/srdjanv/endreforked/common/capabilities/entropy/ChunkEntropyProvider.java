package io.github.srdjanv.endreforked.common.capabilities.entropy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChunkEntropyProvider implements ICapabilitySerializable<NBTTagCompound> {
    private final ChunkEntropy instance;

    public ChunkEntropyProvider(Chunk chunk) {
        instance = new ChunkEntropy(chunk);
    }

    @Override public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEntropyHandler.INSTANCE || capability == CapabilityEntropyHandler.WEEK_INSTANCE;
    }

    @Nullable @Override public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEntropyHandler.INSTANCE) {
            return CapabilityEntropyHandler.INSTANCE.cast(instance);
        } else if (capability == CapabilityEntropyHandler.WEEK_INSTANCE) {
            return CapabilityEntropyHandler.WEEK_INSTANCE.cast(instance);
        } else return null;
    }

    @Override public NBTTagCompound serializeNBT() {
        return instance.serializeNBT();
    }

    @Override public void deserializeNBT(NBTTagCompound nbt) {
        instance.deserializeNBT(nbt);
    }
}
