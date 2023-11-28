package io.github.srdjanv.endreforked.common.entity;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import io.github.srdjanv.endreforked.common.LootHandler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class EntityWatcher extends EndEntity {

    public EntityWatcher(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAIAttackMelee(this, 1.0D, false));
        tasks.addTask(1, new MobEntityAIFollow<>(this));
        tasks.addTask(2, new EntityAIAvoidEntity<>(this, EntityMob.class, 8.0F, 0.80D, 0.80D));
        tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
        tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(5, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTargetReinforce(this));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityEndermite.class, 10, true, false,
                EntityEndermite::isSpawnedByPlayer));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.34D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.34D);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.0D);
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootHandler.WATCHER;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);

        if (flag && getHeldItemMainhand().isEmpty() && entityIn instanceof EntityLivingBase) {
            float f = world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();
            ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 140 * (int) f));
        }

        return flag;
    }

    @Override
    public float getEyeHeight() {
        return 2.55F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : SoundEvents.ENTITY_ENDERMEN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ENDERMEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMEN_DEATH;
    }

    protected static class EntityAIHurtByTargetReinforce extends EntityAIHurtByTarget {

        public EntityAIHurtByTargetReinforce(EntityCreature creatureIn) {
            super(creatureIn, true);
        }

        @Override
        protected void alertOthers() {
            if (Objects.isNull(taskOwner.getRevengeTarget())) return;// should not be possible
            double d0 = getTargetDistance();
            AxisAlignedBB searchArea = (new AxisAlignedBB(
                    taskOwner.posX, taskOwner.posY, taskOwner.posZ,
                    taskOwner.posX + 1.0D, taskOwner.posY + 1.0D, taskOwner.posZ + 1.0D))
                            .grow(d0, 10.0D, d0);
            List<EntityMob> entityMobs = new ObjectArrayList<>();
            entityMobs.addAll(taskOwner.world.getEntitiesWithinAABB(EntityEnderman.class, searchArea));
            entityMobs.addAll(taskOwner.world.getEntitiesWithinAABB(EntityWatcher.class, searchArea));

            for (EntityCreature entitycreature : entityMobs)
                if (taskOwner != entitycreature && entitycreature.getAttackTarget() == null)
                    setEntityAttackTarget(entitycreature, taskOwner.getRevengeTarget());
        }
    }
}
