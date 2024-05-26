package io.github.srdjanv.endreforked.api.fluids.base;

import io.github.srdjanv.endreforked.api.base.crafting.recipe.base.BaseRecipe;
import io.github.srdjanv.endreforked.api.fluids.IWorldRecipe;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class EntityFluidRecipe<T extends Entity>
        extends BaseRecipe<Class<T>, BiFunction<WorldServer, T, ? extends Entity>>
        implements IWorldRecipe, Comparable<EntityFluidRecipe<?>> {

    protected final int chance;
    protected final boolean consumeSource;
    protected final int consumeChance;
    protected final boolean removeEntity;
    protected final EventPriority priority;
    protected final EntityMather<T> entityMather;
    protected final BiConsumer<WorldServer, BlockPos> interactionCallback;
    protected final BiConsumer<WorldServer, BlockPos> fluidInteractionCallback;

    EntityFluidRecipe(Class<T> input,
                      BiFunction<WorldServer, T, ? extends Entity> entityFunction,
                      int chance,
                      boolean consumeSource,
                      int consumeChance,
                      boolean removeEntity,
                      EventPriority priority,
                      EntityMather<T> entityMather,
                      BiConsumer<WorldServer, BlockPos> interactionCallback,
                      BiConsumer<WorldServer, BlockPos> fluidInteractionCallback) {
        super(input, entityFunction);
        this.chance = chance;
        this.consumeSource = consumeSource;
        this.consumeChance = consumeChance;
        this.removeEntity = removeEntity;
        this.priority = priority;
        this.entityMather = entityMather;
        this.interactionCallback = interactionCallback;
        this.fluidInteractionCallback = fluidInteractionCallback;
    }

    public EventPriority getPriority() {
        return priority;
    }

    public EntityMather<T> getEntityMather() {
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

    //todo fix sorting
    @Override public int compareTo(@NotNull EntityFluidRecipe<?> o) {
        return priorityToInt(priority) - priorityToInt(o.priority);
    }

    private int priorityToInt(EventPriority priority) {
        return switch (priority) {
            case LOWEST -> -2;
            case LOW -> -1;
            case NORMAL -> 0;
            case HIGH -> 1;
            case HIGHEST -> 2;
        };
    }

    public static <T extends Entity> Builder<T> builder() {
        return new Builder<>();
    }

    public static final class Builder<E extends Entity> {
        private int chance;
        private boolean consumeSource;
        private int consumeChance;
        private boolean removeEntity;
        private EventPriority priority = EventPriority.NORMAL;
        private EntityMather<E> entityMather;
        private BiConsumer<WorldServer, BlockPos> interactionCallback = CollisionRecipe.EMPTY_INTERACTION_CALLBACK;
        private BiConsumer<WorldServer, BlockPos> fluidInteractionCallback = CollisionRecipe.EMPTY_FLUID_INTERACTION_CALLBACK;
        private Class<E> clazz;
        private BiFunction<WorldServer, E, ? extends Entity> entityFunction;

        private Builder() {}

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

        public Builder<E> withEntityFunction(BiFunction<WorldServer, E, ? extends Entity> entityFunction) {
            this.entityFunction = entityFunction;
            return this;
        }

        public EntityFluidRecipe<E> build() {
            return new EntityFluidRecipe<>(
                    Objects.requireNonNull(clazz),
                    Objects.requireNonNull(entityFunction),
                    chance,
                    consumeSource,
                    consumeChance,
                    removeEntity,
                    priority,
                    Objects.requireNonNull(entityMather),
                    Objects.requireNonNull(interactionCallback),
                    Objects.requireNonNull(fluidInteractionCallback));
        }
    }

}
