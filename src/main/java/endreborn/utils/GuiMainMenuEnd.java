package endreborn.utils;

import net.minecraft.util.ResourceLocation;

import endreborn.Reference;

public class GuiMainMenuEnd {

    public static void endMainMenu() {
        ResourceLocation[] endTitlePanoramaPaths = new ResourceLocation[6];
        for (int i = 0; i < endTitlePanoramaPaths.length; i++) {
            endTitlePanoramaPaths[i] = new ResourceLocation(
                    Reference.TEXTURE_PATH_GUIBACKGROUNDS + "end_panorama_" + i + ".png");
        }
        EndHelper.setTitlePanoramaPaths(endTitlePanoramaPaths);
    }
}
