package endreborn.handlers;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import endreborn.Reference;
import endreborn.mod.blocks.TileEntropyUser;

public class TileHandler {

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntropyUser.class, new ResourceLocation(Reference.MODID + ":entropy_user"));
    }
}
