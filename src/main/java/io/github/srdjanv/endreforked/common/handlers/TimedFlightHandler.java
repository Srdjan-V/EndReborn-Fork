package io.github.srdjanv.endreforked.common.handlers;

import io.github.srdjanv.endreforked.api.capabilities.timedflight.ITimedFlight;
import io.github.srdjanv.endreforked.api.entropy.IEntropyWings;
import io.github.srdjanv.endreforked.common.capabilities.timedflight.CapabilityTimedFlightHandler;
import io.github.srdjanv.endreforked.api.entropy.world.ChunkEntropyView;
import io.github.srdjanv.endreforked.api.entropy.world.EntropyChunkReader;
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
import java.util.UUID;

public class TimedFlightHandler implements Initializer {
    private static TimedFlightHandler instance;

    public static TimedFlightHandler getInstance() {
        if (instance == null) instance = new TimedFlightHandler();
        return instance;
    }

    private final Map<UUID, EntropyChunkReader> cachedEntropyChunks = new Object2ObjectOpenHashMap<>();

    @Override public void registerEventBus() {
        registerThisToEventBus();
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;

        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            if (!player.hasCapability(CapabilityTimedFlightHandler.INSTANCE, null)) continue;
            var cap = player.getCapability(CapabilityTimedFlightHandler.INSTANCE, null);
            cap.tickPlayer(player);
            if (cap.getFlightDuration() > 0) continue;
            handelEntropyWings(player, cap);
        }
    }

    private void handelEntropyWings(EntityPlayerMP player, ITimedFlight cap) {
        for (ItemStack stack : PlayerUtils.getAllPlayerItems(player)) {
            if (stack.isEmpty()) continue;
            if (!(stack.getItem() instanceof IEntropyWings wings)) continue;
            var wrapper = cachedEntropyChunks.get(player);
            if (wrapper == null || !wrapper.getRadius().equals(wings.getEntropyRange())) {
                wrapper = EntropyChunkReader.ofEntity(player, wings.getEntropyRange());
                cachedEntropyChunks.put(player.getUniqueID(), wrapper);
            }
            ChunkEntropyView data = wrapper.getEntropyView();
            if (data.drainEntropy(wings.getEntropyCost(), true) == wings.getEntropyCost()) {
                data.drainEntropy(wings.getEntropyCost(), false);
                cap.setFlightDuration(wings.getFlightDuration());
            }
        }
    }

    @Override
    public void onServerStopped(FMLServerStoppedEvent event) {
        cachedEntropyChunks.clear();
    }
}
