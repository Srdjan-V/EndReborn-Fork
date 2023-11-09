package endreborn.common.capabilities.timedflight;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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

import endreborn.Reference;

public class CapabilityTimedFlightHandler {

    @CapabilityInject(TimedFlight.class)
    public static Capability<TimedFlight> TIMED_FLIGHT_CAPABILITY;

    public static final ResourceLocation dragoniteTeaFlightCap = new ResourceLocation(
            Reference.MOD_PREFIX + "TimedFlight");

    @SubscribeEvent
    public static void attachTimedFlightCap(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof EntityPlayer)) return;
        event.addCapability(dragoniteTeaFlightCap, new TimedFlightCapabilityProvider());
    }

    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        event.player.getCapability(TIMED_FLIGHT_CAPABILITY, null).tickPlayer(event.player.capabilities);
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(TimedFlight.class, new Capability.IStorage<>() {

            @Override
            public @NotNull NBTBase writeNBT(Capability<TimedFlight> capability, TimedFlight instance,
                                             EnumFacing side) {
                return new NBTTagInt(instance.getFlightDuration());
            }

            @Override
            public void readNBT(Capability<TimedFlight> capability, TimedFlight instance, EnumFacing side,
                                NBTBase nbt) {
                instance.setFlightDuration(((NBTTagInt) nbt).getInt());
            }
        }, TimedFlight::new);
    }
}
