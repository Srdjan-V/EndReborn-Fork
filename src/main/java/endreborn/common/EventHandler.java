package endreborn.common;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class EventHandler {

    @SubscribeEvent
    public static void onDragonTick(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        if (living.world.isRemote || !(living instanceof EntityDragon dragon)) return;
        if (dragon.deathTicks < 150 || dragon.deathTicks % 10 != 0) return;

        for (int i = 0; i < 6; i++) {
            int x = dragon.world.rand.nextInt(256) - 128;
            int z = dragon.world.rand.nextInt(256) - 128;
            BlockPos pos = new BlockPos(x, dragon.world.getHeight(x, z) - 1, z);
            if (!dragon.world.isBlockLoaded(pos))
                continue;
            if (dragon.world.getBlockState(pos.down()).getBlock() != Blocks.END_STONE)
                continue;
            dragon.world.setBlockState(pos, ModBlocks.XORCITE_BLOCK.get().getDefaultState());
        }
    }

    @SubscribeEvent
    public static void onPlayerPosition(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayerMP player && event.getEntityLiving().dimension == 1 &&
                Configs.GENERAL.teleporterEnd && event.getEntityLiving().getPosition().getY() <= -6) {
            PlayerList playerList = player.getEntityWorld().getMinecraftServer().getPlayerList();

            event.setCanceled(true);
            playerList.changePlayerDimension(player, 0);
        }
    }

    private EventHandler() {}
}
