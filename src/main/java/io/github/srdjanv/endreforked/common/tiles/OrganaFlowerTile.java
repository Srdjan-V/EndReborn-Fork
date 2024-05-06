package io.github.srdjanv.endreforked.common.tiles;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.common.capabilities.timedflight.CapabilityTimedFlightHandler;
import io.github.srdjanv.endreforked.common.entropy.chunks.EntropyChunkDataWrapper;
import io.github.srdjanv.endreforked.common.entropy.chunks.EntropyChunkInducer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class OrganaFlowerTile extends TileEntity implements ITickable {
    private final EntropyChunkDataWrapper<TileEntity> tileWrapper;
    private final EntropyChunkInducer<TileEntity> inducer;
    private int tick;

    public OrganaFlowerTile() {
        this.tick = ThreadLocalRandom.current().nextInt(60);
        this.tileWrapper = new EntropyChunkDataWrapper.TileEntity(1);
        inducer = new EntropyChunkInducer<>(tileWrapper, 5 * 20, 10);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            inducer.induce(this);
            if (++tick % 80 == 0) addEffectToPlayers();
        } else if (++tick % 60 == 0) {
            int color = 0x5900b3;
            double xSpeed = (double) (color >> 16 & 255) / 255.0D;
            double ySpeed = (double) (color >> 8 & 255) / 255.0D;
            double zSpeed = (double) (color >> 0 & 255) / 255.0D;

            world.spawnParticle(EnumParticleTypes.SPELL_MOB,
                    pos.getX() + (world.rand.nextDouble() - 0.5D),
                    pos.getY() + (world.rand.nextDouble() - 0.5D),
                    pos.getZ() + (world.rand.nextDouble() - 0.5D),
                    xSpeed, ySpeed, zSpeed);
        }
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
