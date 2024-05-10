package io.github.srdjanv.endreforked.core.devmixin;

import io.github.srdjanv.endreforked.common.comands.dev.DevConfigs;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilChunkLoader.class)
public abstract class AnvilChunkLoaderMixin {

    @Inject(method = "loadChunk__Async", at = @At("HEAD"), cancellable = true, remap = false)
    public void loadChunk(World worldIn, int x, int z, CallbackInfoReturnable<Object[]> info) {
        if (DevConfigs.DISABLE_REGION_LOADING.getBoolean()) info.setReturnValue(null);
    }
}
