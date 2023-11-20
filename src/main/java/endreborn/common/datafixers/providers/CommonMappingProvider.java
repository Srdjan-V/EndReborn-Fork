package endreborn.common.datafixers.providers;

import java.util.Collections;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IFixableData;

import org.jetbrains.annotations.NotNull;

import endreborn.Reference;

public interface CommonMappingProvider extends IFixableData {

    FixTypes getType();

    @Override
    default NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        return compound;
    }

    @NotNull
    default Map<ResourceLocation, ResourceLocation> getCommonMappings() {
        return Collections.emptyMap();
    }

    default ResourceLocation resLoc(String id) {
        return new ResourceLocation(Reference.MODID, id);
    }
}
