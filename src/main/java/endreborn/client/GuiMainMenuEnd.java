package endreborn.client;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import endreborn.EndReborn;
import endreborn.Reference;

public final class GuiMainMenuEnd {

    public static final String[] GUIMAINMENU_TITLEPANORAMAPATHS = new String[] { "TITLE_PANORAMA_PATHS",
            "field_73978_o", "o" };

    public static void setTitlePanoramaPaths(ResourceLocation[] titlePanoramaPaths) {
        Field field = ReflectionHelper.findField(GuiMainMenu.class, GUIMAINMENU_TITLEPANORAMAPATHS);

        Field modifiersField;
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.setAccessible(true);
            field.set(null, titlePanoramaPaths);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
            EndReborn.LOGGER.error(e);
        }
    }

    public static void endMainMenu() {
        ResourceLocation[] endTitlePanoramaPaths = new ResourceLocation[6];
        for (int i = 0; i < endTitlePanoramaPaths.length; i++) {
            endTitlePanoramaPaths[i] = new ResourceLocation(
                    Reference.TEXTURE_PATH_GUIBACKGROUNDS + "end_panorama_" + i + ".png");
        }
        setTitlePanoramaPaths(endTitlePanoramaPaths);
    }
}
