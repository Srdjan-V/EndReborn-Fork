package endreborn.common.datafixers.providers;

import java.util.Collections;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import endreborn.Reference;

public interface CommonMappingProvider {

    @NotNull
    default Map<ResourceLocation, ResourceLocation> getCommonMappings() {
        return Collections.emptyMap();
    }

    default ResourceLocation resLoc(String id) {
        return new ResourceLocation(Reference.MODID + id);
    }
}
