package io.github.srdjanv.endreforked.api.fluids.base;

import io.github.srdjanv.endreforked.api.util.EntityMatchStrategy;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class EntityMather<E extends Entity> implements Predicate<E> {
    //todo cache this?
    protected final Function<World, E> entityBuilder;
    protected final EntityMatchStrategy<E> matchStrategy;
    protected final String translation;

    public EntityMather(Function<World, E> entityBuilder, EntityMatchStrategy<E> matchStrategy, String translation) {
        this.entityBuilder = entityBuilder;
        this.matchStrategy = matchStrategy;
        this.translation = translation;
    }

    @Override public boolean test(E e) {
        return matchStrategy.match(entityBuilder.apply(e.getEntityWorld()), e);
    }

    public static <E extends Entity> EntityMatherBuilder<E> builder() {
        return new EntityMatherBuilder<>();
    }

    public static final class EntityMatherBuilder<E extends Entity> {
        private Function<World, E> entityBuilder;
        private EntityMatchStrategy<E> matchStrategy;
        private String translation;

        private EntityMatherBuilder() {}

        public EntityMatherBuilder<E> withEntityBuilder(Function<World, E> entityBuilder) {
            this.entityBuilder = entityBuilder;
            return this;
        }

        public EntityMatherBuilder<E> withMatchStrategy(EntityMatchStrategy<E> matchStrategy) {
            this.matchStrategy = matchStrategy;
            return this;
        }

        public EntityMatherBuilder<E> withTranslation(String translation) {
            this.translation = translation;
            return this;
        }

        public EntityMather<E> build() {return new EntityMather<>(
                Objects.requireNonNull(entityBuilder),
                Objects.requireNonNull(matchStrategy),
                translation);
        }
    }
}
