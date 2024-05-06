package io.github.srdjanv.endreforked.common.capabilities.timedflight;

import io.github.srdjanv.endreforked.Tags;
import io.github.srdjanv.endreforked.api.capabilities.timedflight.ITimedFlight;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

public class CapabilityTimedFlightHandler {

    @CapabilityInject(ITimedFlight.class)
    public static Capability<ITimedFlight> INSTANCE;

    public static final ResourceLocation TimedFlight = new ResourceLocation(Tags.MODID, "TimedFlight");

    @SubscribeEvent
    public static void attachTimedFlightCap(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof EntityPlayerMP)) return;
        event.addCapability(TimedFlight, new TimedFlightCapabilityProvider());
    }

    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (event.player instanceof EntityPlayerMP playerMP) {
            playerMP.getCapability(INSTANCE, null).tickPlayer(playerMP);
        }
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(ITimedFlight.class, new Capability.IStorage<>() {

            @Override
            public @NotNull NBTBase writeNBT(Capability<ITimedFlight> capability, ITimedFlight instance,
                                             EnumFacing side) {
                return new NBTTagInt(instance.getFlightDuration());
            }

            @Override
            public void readNBT(Capability<ITimedFlight> capability, ITimedFlight instance, EnumFacing side,
                                NBTBase nbt) {
                instance.setFlightDuration(((NBTTagInt) nbt).getInt());
            }
        }, TimedFlight::new);
    }
}
