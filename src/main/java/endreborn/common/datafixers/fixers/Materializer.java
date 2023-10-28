package endreborn.common.datafixers.fixers;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IFixableData;

import org.jetbrains.annotations.NotNull;

import endreborn.common.datafixers.providers.BlockMappingProvider;
import endreborn.common.datafixers.providers.ItemMappingProvider;

public class Materializer implements IFixableData, BlockMappingProvider, ItemMappingProvider {

    public static final FixTypes TYPE = FixTypes.ITEM_INSTANCE;

    @Override
    public int getFixVersion() {
        return 1;
    }

    @Override
    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        return compound;
    }

    @Override
    public @NotNull Map<ResourceLocation, ResourceLocation> getCommonMappings() {
        Map<ResourceLocation, ResourceLocation> mappings = new HashMap<>();

        mappings.put(
                resLoc("entropy_user"),
                resLoc("materializer"));

        return mappings;
    }
}
