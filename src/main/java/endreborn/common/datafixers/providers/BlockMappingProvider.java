package endreborn.common.datafixers.providers;

import java.util.Collections;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

public interface BlockMappingProvider extends CommonMappingProvider {

    @NotNull
    default Map<ResourceLocation, ResourceLocation> getBlockMappings() {
        return Collections.emptyMap();
    }
}
