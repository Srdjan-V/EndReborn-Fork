package endreborn.handlers;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import endreborn.mod.entity.*;
import endreborn.mod.entity.render.*;

public class RenderHandler {

    public static void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityEGuard.class, RenderEGuard::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWatcher.class, RenderWatcher::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLord.class, RenderLord::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityChronologist.class, RenderChronologist::new);
    }
}
