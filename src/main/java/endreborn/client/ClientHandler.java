package endreborn.client;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import endreborn.client.entity.render.*;
import endreborn.common.Configs;
import endreborn.common.EndVillagerHandler;
import endreborn.common.ModBlocks;
import endreborn.common.ModItems;
import endreborn.common.entity.*;
import endreborn.utils.GuiMainMenuEnd;
import endreborn.utils.IHasModel;

public final class ClientHandler {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (Item item : ModItems.ITEMS) {
            if (item instanceof IHasModel) {
                ((IHasModel) item).registerModels();
            }
        }

        for (Block block : ModBlocks.BLOCKS) {
            if (block instanceof IHasModel) {
                ((IHasModel) block).registerModels();
            }
        }

        RenderingRegistry.registerEntityRenderingHandler(EntityEGuard.class, RenderEGuard.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityWatcher.class, RenderWatcher.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityLord.class, RenderLord.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityChronologist.class, RenderChronologist.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityColdFireball.class, RenderColdFireball::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityEGuard.class, RenderEGuard::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWatcher.class, RenderWatcher::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLord.class, RenderLord::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityChronologist.class, RenderChronologist::new);
    }

    public static void preInit() {}

    public static void init() {
        if (Configs.GENERAL.spawnNewVillagers) {
            EndVillagerHandler.initIEVillagerTrades();
            EndVillagerHandler.initIEVillagerHouse();
        }
        if (Configs.GENERAL.panorama) {
            GuiMainMenuEnd.endMainMenu();
        }
    }

    public static void postInit() {}
}
