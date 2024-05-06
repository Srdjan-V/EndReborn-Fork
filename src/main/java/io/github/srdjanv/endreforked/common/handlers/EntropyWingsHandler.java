package io.github.srdjanv.endreforked.common.handlers;

import io.github.srdjanv.endreforked.api.entropy.IEntropyWings;
import io.github.srdjanv.endreforked.common.capabilities.timedflight.CapabilityTimedFlightHandler;
import io.github.srdjanv.endreforked.common.entropy.chunks.ChunkEntropyView;
import io.github.srdjanv.endreforked.common.entropy.chunks.EntropyChunkDataWrapper;
import io.github.srdjanv.endreforked.utils.Initializer;
import io.github.srdjanv.endreforked.utils.PlayerUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;

public class EntropyWingsHandler implements Initializer {
    private static EntropyWingsHandler instance;

    public static EntropyWingsHandler getInstance() {
        if (instance == null) instance = new EntropyWingsHandler();
        return instance;
    }

    private final Map<EntityPlayerMP, EntropyChunkDataWrapper.EntityPlayer> cachedEntropyChunks = new Object2ObjectOpenHashMap<>();

    @Override public void registerEventBus() {
        registerThisToEventBus();
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;

        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            if (!player.hasCapability(CapabilityTimedFlightHandler.INSTANCE, null)) continue;
            var cap = player.getCapability(CapabilityTimedFlightHandler.INSTANCE, null);
            if (cap.getFlightDuration() > 0) {
                cap.tickPlayer(player);
                continue;
            }

            for (ItemStack stack : PlayerUtils.getAllPlayerItems(player)) {
                if (stack.isEmpty()) continue;
                if (!(stack.getItem() instanceof IEntropyWings wings)) continue;
                var wrapper = cachedEntropyChunks.get(player);
                if (wrapper == null || !wrapper.getRadius().equals(wings.getEntropyRange())) {
                    wrapper = new EntropyChunkDataWrapper.EntityPlayer(wings.getEntropyRange());
                    cachedEntropyChunks.put(player, wrapper);
                }
                ChunkEntropyView data = wrapper.getEntropyView(player);
                if (data.drainEntropy(wings.getEntropyCost(), true) == wings.getEntropyCost()) {
                    data.drainEntropy(wings.getEntropyCost(), false);
                    cap.setFlightDuration(wings.getFlightDuration());
                }
            }
        }
    }

    @Override
    public void onServerStopped(FMLServerStoppedEvent event) {
        cachedEntropyChunks.clear();
    }
}
