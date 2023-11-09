package endreborn.common.capabilities.timedflight;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.world.World;

public class TimedFlight {

    private boolean modifyFlight;
    private int flightDuration;

    public boolean isModifyFlight() {
        return modifyFlight;
    }

    public int getFlightDuration() {
        return flightDuration;
    }

    public void setModifyFlight(boolean modifyFlight) {
        this.modifyFlight = modifyFlight;
    }

    public void setFlightDuration(int flightDuration) {
        this.flightDuration = flightDuration;
    }

    public void tickPlayer(PlayerCapabilities capabilities) {
        if (flightDuration > 0) {
            flightDuration--;
        } else if (modifyFlight) {
            modifyFlight = false;
            capabilities.allowFlying = false;
            capabilities.isFlying = false;
        }
    }

    public void setFlightDuration(World world, PlayerCapabilities capabilities, int flightDuration) {
        if (!world.isRemote) return;
        if (flightDuration > 0) {
            this.flightDuration = flightDuration;
            capabilities.allowFlying = true;
            modifyFlight = true;
        }
    }
}
