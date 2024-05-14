package io.github.srdjanv.endreforked.common.tiles.passiveinducers;

import io.github.srdjanv.endreforked.api.entropy.EntropyRadius;
import io.github.srdjanv.endreforked.common.tiles.base.BasePassiveInducer;
import net.minecraft.util.EnumParticleTypes;

import java.util.concurrent.ThreadLocalRandom;

public class OrganaFlowerTile extends BasePassiveInducer {
    private int tick;

    public OrganaFlowerTile() {
        super(EntropyRadius.TWO, 5 * 20, 10);
        this.tick = ThreadLocalRandom.current().nextInt(60);
    }

    @Override
    public void update() {
        super.update();

        if (!world.isRemote) return;
        if (++tick % 60 != 0) return;

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

/*    private void addEffectToPlayers() {
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
    }*/
}
