package endreborn.client;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import endreborn.client.entity.render.*;
import endreborn.common.Configs;
import endreborn.common.entity.*;
import endreborn.utils.Initializer;

final class Registration implements Initializer {

    @Override
    public void registerEventBus() {
        registerThisToEventBus();
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityEndGuard.class, RenderEGuard.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityWatcher.class, RenderWatcher.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityLord.class, RenderLord.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityChronologist.class, RenderChronologist.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityColdFireball.class, RenderColdFireball::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityEndGuard.class, RenderEGuard::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWatcher.class, RenderWatcher::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLord.class, RenderLord::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityChronologist.class, RenderChronologist::new);
    }

    @Override
    public void init() {
        if (Configs.GENERAL.panorama) {
            GuiMainMenuEnd.endMainMenu();
        }
    }
}
