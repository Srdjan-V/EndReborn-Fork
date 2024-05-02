package io.github.srdjanv.endreforked.common.capabilities.timedflight;

import net.minecraft.entity.player.EntityPlayerMP;

//todo add effect on player side
public class TimedFlight {
    private boolean startFlight;
    private boolean stopFlight;
    private int flightDuration;

    public int getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(int flightDuration) {
        this.flightDuration = flightDuration;
        stopFlight = true;
        startFlight = true;
    }

    public void tickPlayer(EntityPlayerMP playerMP) {
        if (flightDuration > 0) {
            if (startFlight) {
                startFlight = false;
                sendUpdate(playerMP, true);
            }

            flightDuration--;
        } else if (stopFlight) {
            stopFlight = false;
            sendUpdate(playerMP, false);
        }
    }

    public void sendUpdate(EntityPlayerMP playerMP, boolean flight) {
        playerMP.capabilities.allowFlying = flight;
        playerMP.capabilities.isFlying = flight;
        playerMP.sendPlayerAbilities();
    }

}
