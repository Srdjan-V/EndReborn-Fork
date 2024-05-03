package io.github.srdjanv.endreforked.common.capabilities.timedflight;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.common.network.PlayerParticlePacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;

//todo add effect on player side
public class TimedFlight {
    private boolean sendStartUpdate;
    private boolean sendStopUpdate;
    private int flightDuration;

    public int getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(int flightDuration) {
        this.flightDuration = flightDuration;
        sendStopUpdate = true;
        sendStartUpdate = true;
    }

    public void tickPlayer(EntityPlayerMP playerMP) {
        if (flightDuration > 0) {
            if (sendStartUpdate) {
                sendStartUpdate = false;
                sendUpdate(playerMP, true);
            }
            flightDuration--;
        } else if (sendStopUpdate) {
            sendStopUpdate = false;
            sendUpdate(playerMP, false);
        }
    }

    public void sendUpdate(EntityPlayerMP playerMP, boolean flight) {
        if (playerMP.capabilities.isCreativeMode) return;
        handleParticle(playerMP, flight);
        if (flight) {
            playerMP.capabilities.allowFlying = true;
        } else {
            playerMP.capabilities.allowFlying = false;
            playerMP.capabilities.isFlying = false;
        }
        playerMP.sendPlayerAbilities();
    }

    public void handleParticle(EntityPlayerMP playerMP, boolean flight) {
        final String id = "TimedFlight";

        if (flight) {
            EndReforked.NET.sendTo(PlayerParticlePacket.newParticle(id, flightDuration, EnumParticleTypes.SPELL_MOB),
                    playerMP);
        } else {
            EndReforked.NET.sendTo(PlayerParticlePacket.remove(id), playerMP);
        }
    }
}
