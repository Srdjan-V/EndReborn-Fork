package endreborn.common;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import endreborn.common.entity.EntityWatcher;
import endreborn.utils.EndForge;

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
    public static void playerInteractEvent(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();

        EntityPlayer player = event.getEntityPlayer();
        if (event.getHand() == EnumHand.OFF_HAND) {
            ItemStack mainStack = player.getHeldItem(EnumHand.MAIN_HAND);
            if (EndForge.hasAction(event.getWorld(), event.getPos(), mainStack, event.getFace())) {
                event.setCancellationResult(EnumActionResult.SUCCESS);
                event.setCanceled(true);
                return;
            }
        }

        if (EndForge.hasAction(event.getWorld(), event.getPos(), stack, event.getFace())) {
            if (EndForge.performAction(event.getWorld(), event.getPos(), player, stack, event.getFace(),
                    event.getHand())) {
                event.setCancellationResult(EnumActionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onEventDrop(LivingDropsEvent event) {
        if (event.getEntity() instanceof EntityDragon) {
            ItemStack itemStackToDrop = new ItemStack(ModItems.DRAGON_SCALES.get(), 2);
            event.getDrops().add(new EntityItem(event.getEntity().getEntityWorld(), event.getEntity().posX,
                    event.getEntity().posY, event.getEntity().posZ, itemStackToDrop));

        }
        if (event.getEntity() instanceof EntityEnderman) {
            ItemStack itemStackToDrop = new ItemStack(ModItems.ENDER_FLESH.get(), 1);
            event.getDrops().add(new EntityItem(event.getEntity().getEntityWorld(), event.getEntity().posX,
                    event.getEntity().posY, event.getEntity().posZ, itemStackToDrop));
        }

        if (event.getEntity() instanceof EntityWatcher) {
            ItemStack itemStackToDrop = new ItemStack(ModItems.ENDER_FLESH.get(), 1);
            event.getDrops().add(new EntityItem(event.getEntity().getEntityWorld(), event.getEntity().posX,
                    event.getEntity().posY, event.getEntity().posZ, itemStackToDrop));
        }

        if (event.getEntity() instanceof EntityWatcher) {
            ItemStack itemStackToDrop = new ItemStack(Items.ENDER_EYE, 1);
            event.getDrops().add(new EntityItem(event.getEntity().getEntityWorld(), event.getEntity().posX,
                    event.getEntity().posY, event.getEntity().posZ, itemStackToDrop));
        }
    }

    @SubscribeEvent
    public static void onLivingSpawn(LivingSpawnEvent event) {
        /*
         * EntityLivingBase entity = event.getEntityLiving();
         * if (entity instanceof EntityEnderman && Configs.GENERAL.spawnWatcher) {
         * if (entity.world.provider.getDimension() == 1 && entity.world.getDifficulty() != EnumDifficulty.PEACEFUL &&
         * !entity.world.isRemote) {
         * if (entity.getRNG().nextInt(Configs.BALANCE.watcherRare) == 1) {
         * EntityWatcher watch = new EntityWatcher(entity.world);
         * watch.copyLocationAndAnglesFrom(entity);
         * entity.world.spawnEntity(watch);
         * entity.setDead();
         * }
         * }
         * }
         * if (entity instanceof EntityEnderman && Configs.GENERAL.spawnChronologist) {
         * if (entity.world.provider.getDimension() == 0 && entity.world.getDifficulty() != EnumDifficulty.PEACEFUL &&
         * !entity.world.isRemote) {
         * if (entity.getRNG().nextInt(Configs.BALANCE.chronRare) == 1) {
         * EntityChronologist chron = new EntityChronologist(entity.world);
         * chron.copyLocationAndAnglesFrom(entity);
         * entity.world.spawnEntity(chron);
         * entity.setDead();
         * }
         * }
         * }
         * if (entity instanceof EntityEnderman && Configs.GENERAL.spawnEndGuard) {
         * if (entity.world.provider.getDimension() == 1 && entity.world.getDifficulty() != EnumDifficulty.PEACEFUL &&
         * !entity.world.isRemote) {
         * if (entity.getRNG().nextInt(Configs.BALANCE.guardRare) == 1) {
         * EntityEGuard guard = new EntityEGuard(entity.world);
         * guard.copyLocationAndAnglesFrom(entity);
         * entity.world.spawnEntity(guard);
         * entity.setDead();
         * }
         * }
         * }
         */
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
