package io.github.srdjanv.endreforked.api.capabilities.entropy;

import io.github.srdjanv.endreforked.api.util.DimPos;
import io.github.srdjanv.endreforked.api.entropy.EntropyDataProvider;
import io.github.srdjanv.endreforked.api.entropy.storage.EntropyStorageReference;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface EntropyChunk extends INBTSerializable<NBTTagCompound>, WeakEntropyStorage, EntropyStorageReference, EntropyDataProvider {
    boolean isLoaded();
    DimPos getDimPos();
}
