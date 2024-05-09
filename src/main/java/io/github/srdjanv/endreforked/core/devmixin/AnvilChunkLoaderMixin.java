package io.github.srdjanv.endreforked.core.devmixin;

import io.github.srdjanv.endreforked.Tags;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(AnvilChunkLoader.class)
public abstract class AnvilChunkLoaderMixin {
    @Unique
    private static final Logger DEV_MIXIN_REGION_DATA_IGNORER_WARINING_LOGGER = LogManager.getLogger("AnvilChunkLoader");
    @Unique
    private boolean dev_mixin_logged_region_waring;
    @Shadow
    private File chunkSaveLocation;

    @Inject(method = "loadChunk__Async", at = @At("HEAD"), cancellable = true, remap = false)
    public void loadChunk(World worldIn, int x, int z, CallbackInfoReturnable<Object[]> info) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_SPACE))) {
            if (!dev_mixin_logged_region_waring) {
                DEV_MIXIN_REGION_DATA_IGNORER_WARINING_LOGGER.warn(
                        "LCONTROL and (Shift or Space) was held while loading chunk data, ignoring chunk. Data dir {}. This is a dev mixin by {}",
                        chunkSaveLocation, Tags.MODNAME);
                dev_mixin_logged_region_waring = true;
            }
            info.setReturnValue(null);
        }
    }
}
