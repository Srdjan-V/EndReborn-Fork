package io.github.srdjanv.endreforked.api.capabilities.entropy;

import io.github.srdjanv.endreforked.api.base.util.DimPos;
import io.github.srdjanv.endreforked.api.entropy.IEntropyDataProvider;
import io.github.srdjanv.endreforked.api.entropy.storage.EntropyStorageReference;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface EntropyChunk extends INBTSerializable<NBTTagCompound>, WeakEntropyStorage, EntropyStorageReference, IEntropyDataProvider {
    DimPos getDimPos();
}
