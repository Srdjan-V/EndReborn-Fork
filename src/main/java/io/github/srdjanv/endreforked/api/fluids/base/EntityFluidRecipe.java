package io.github.srdjanv.endreforked.api.fluids.base;

import io.github.srdjanv.endreforked.api.base.crafting.recipe.base.BaseRecipe;
import io.github.srdjanv.endreforked.api.fluids.IWorldRecipe;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class EntityFluidRecipe<E extends Entity>
        extends BaseRecipe<Class<E>, BiFunction<WorldServer, E, EntityFluidRecipeResult<E>>>
        implements IWorldRecipe {
    protected final ResourceLocation registryName;
    protected final int chance;
    protected final boolean consumeSource;
    protected final int consumeChance;
    protected final boolean removeEntity;
    protected final EventPriority priority;
    protected final EntityMather<E> entityMather;
    protected final BiConsumer<WorldServer, BlockPos> interactionCallback;
    protected final BiConsumer<WorldServer, BlockPos> fluidInteractionCallback;

    EntityFluidRecipe(Class<E> input,
                      BiFunction<WorldServer, E, EntityFluidRecipeResult<E>> entityFunction,
                      ResourceLocation registryName,
                      int chance,
                      boolean consumeSource,
                      int consumeChance,
                      boolean removeEntity,
                      EventPriority priority,
                      EntityMather<E> entityMather,
                      BiConsumer<WorldServer, BlockPos> interactionCallback,
                      BiConsumer<WorldServer, BlockPos> fluidInteractionCallback) {
        super(input, entityFunction);
        this.registryName = registryName;
        this.chance = chance;
        this.consumeSource = consumeSource;
        this.consumeChance = consumeChance;
        this.removeEntity = removeEntity;
        this.priority = priority;
        this.entityMather = entityMather;
        this.interactionCallback = interactionCallback;
        this.fluidInteractionCallback = fluidInteractionCallback;
    }

    @Override
    public EventPriority priority() {
        return priority;
    }

    @Override public ResourceLocation registryName() {
        return registryName;
    }

    public EntityMather<E> getEntityMather() {
        return entityMather;
    }

    @Override
    public int getChance() {
        return chance;
    }

    @Override public int getChanceConsumeSource() {
        return consumeChance;
    }

    @Override
    public boolean isConsumeSource() {
        return consumeSource;
    }

    public boolean isRemoveEntity() {
        return removeEntity;
    }

    @Override
    public BiConsumer<WorldServer, BlockPos> getFluidInteractionCallback() {
        return fluidInteractionCallback;
    }

    @Override
    public BiConsumer<WorldServer, BlockPos> getInteractionCallback() {
        return interactionCallback;
    }

    public static <T extends Entity> Builder<T> builder() {
        return new Builder<>();
    }

    public static final class Builder<E extends Entity> {
        private ResourceLocation registryName;
        private int chance;
        private boolean consumeSource;
        private int consumeChance;
        private boolean removeEntity;
        private EventPriority priority = EventPriority.NORMAL;
        private EntityMather<E> entityMather;
        private BiConsumer<WorldServer, BlockPos> interactionCallback = CollisionRecipe.EMPTY_INTERACTION_CALLBACK;
        private BiConsumer<WorldServer, BlockPos> fluidInteractionCallback = CollisionRecipe.EMPTY_FLUID_INTERACTION_CALLBACK;
        private Class<E> clazz;
        private BiFunction<WorldServer, E, EntityFluidRecipeResult<E>> entityFunction;

        private Builder() {}

        public Builder<E> withRegistryName(ResourceLocation location) {
            this.registryName = location;
            return this;
        }

        public Builder<E> withRegistryName(String location) {
            this.registryName = new ResourceLocation(location);
            return this;
        }

        public Builder<E> withChance(int chance) {
            this.chance = chance;
            return this;
        }

        public Builder<E> withConsumeSource(boolean consumeSource) {
            this.consumeSource = consumeSource;
            return this;
        }

        public Builder<E> withConsumeChance(int consumeChance) {
            this.consumeChance = consumeChance;
            return this;
        }

        public Builder<E> withRemoveEntity(boolean removeEntity) {
            this.removeEntity = removeEntity;
            return this;
        }

        public Builder<E> withPriority(EventPriority priority) {
            this.priority = priority;
            return this;
        }

        public Builder<E> withEntityMather(EntityMather<E> entityMather) {
            this.entityMather = entityMather;
            return this;
        }

        public Builder<E> withInteractionCallback(BiConsumer<WorldServer, BlockPos> interactionCallback) {
            this.interactionCallback = interactionCallback;
            return this;
        }

        public Builder<E> withFluidInteractionCallback(BiConsumer<WorldServer, BlockPos> fluidInteractionCallback) {
            this.fluidInteractionCallback = fluidInteractionCallback;
            return this;
        }

        public Builder<E> withClazz(Class<E> clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder<E> withEntityFunction(BiFunction<WorldServer, E, EntityFluidRecipeResult<E>> entityFunction) {
            this.entityFunction = entityFunction;
            return this;
        }

        public EntityFluidRecipe<E> build() {
            return new EntityFluidRecipe<>(
                    Objects.requireNonNull(clazz),
                    Objects.requireNonNull(entityFunction),
                    Objects.requireNonNull(registryName),
                    chance,
                    consumeSource,
                    consumeChance,
                    removeEntity,
                    priority,
                    Objects.requireNonNull(entityMather),
                    Objects.requireNonNull(interactionCallback), Objects.requireNonNull(fluidInteractionCallback));
        }
    }

}
