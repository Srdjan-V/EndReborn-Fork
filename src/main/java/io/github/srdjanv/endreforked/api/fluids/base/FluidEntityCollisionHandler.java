package io.github.srdjanv.endreforked.api.fluids.base;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FluidEntityCollisionHandler {
    private final Object2ObjectOpenHashMap<Class<? extends Entity>, ObjectSortedSet<EntityFluidRecipe<Entity>>> registry;

    protected FluidEntityCollisionHandler() {
        registry = new Object2ObjectOpenHashMap<>();
    }

    public Object2ObjectOpenHashMap<Class<? extends Entity>, ObjectSortedSet<EntityFluidRecipe<Entity>>> getRegistry() {
        return registry;
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> void register(EntityFluidRecipe<T> recipe) {
        var set = registry.computeIfAbsent(recipe.getInput(), k -> new ObjectAVLTreeSet<>());
        set.add((EntityFluidRecipe<Entity>) recipe);
    }

    public <T extends Entity> List<EntityFluidRecipe<Entity>> getFluidRecipe(final T entity) {
        var set = registry.get(entity.getClass());
        if (set == null) return Collections.emptyList();
        return set.stream()
                .filter(rec -> rec.getEntityMather().test(entity))
                .collect(Collectors.toCollection(ObjectArrayList::new));
    }

}
