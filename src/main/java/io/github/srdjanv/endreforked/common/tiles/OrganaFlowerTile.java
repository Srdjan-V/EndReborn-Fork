package io.github.srdjanv.endreforked.common.tiles;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.common.capabilities.timedflight.CapabilityTimedFlightHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class OrganaFlowerTile extends TileEntity implements ITickable {
    private int tick;

    public OrganaFlowerTile() {
        this.tick = ThreadLocalRandom.current().nextInt((60));
    }

    @Override
    public void update() {
        if (!world.isRemote && ++tick % 80 == 0)
            addEffectToPlayers();
    }

    private void addEffectToPlayers() {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1)).grow(10);
        List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
        for (EntityPlayer player : list) {
            if (!player.hasCapability(CapabilityTimedFlightHandler.INSTANCE, null)) {
                EndReforked.LOGGER.warn("Player {} has no timed flight capability", player.getName());
                continue;
            }
            var cap = player.getCapability(CapabilityTimedFlightHandler.INSTANCE, null);
            cap.setFlightDuration(30 * 20);
        }

    }
}
