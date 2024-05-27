package io.github.srdjanv.endreforked.api.fluids.base;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.entity.Entity;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FluidEntityCollisionHandler {
    private final Object2ObjectOpenHashMap<Class<? extends Entity>, ObjectList<EntityFluidRecipe<Entity>>> registry;
    private boolean sortNeeded = true;

    protected FluidEntityCollisionHandler() {
        registry = new Object2ObjectOpenHashMap<>();
    }

    public Object2ObjectOpenHashMap<Class<? extends Entity>, ObjectList<EntityFluidRecipe<Entity>>> getRegistry() {
        return registry;
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> void unRegister(Predicate<EntityFluidRecipe<T>> remove) {
        registry.values().forEach(list -> {
            if (list.removeIf((Predicate<? super EntityFluidRecipe<Entity>>) remove)) {
                sortNeeded = true;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> void unRegister(EntityFluidRecipe<T> recipe) {
        var list = registry.computeIfAbsent(recipe.getInput(), k -> new ObjectArrayList<>());
        if (list.remove((EntityFluidRecipe<Entity>) recipe)) sortNeeded = true;
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> void register(EntityFluidRecipe<T> recipe) {
        var list = registry.computeIfAbsent(recipe.getInput(), k -> new ObjectArrayList<>());
        if (list.add((EntityFluidRecipe<Entity>) recipe)) sortNeeded = true;
    }

    public <T extends Entity> Iterator<EntityFluidRecipe<Entity>> getFluidRecipe(final T entity) {
        if (sortNeeded) {
            for (ObjectList<EntityFluidRecipe<Entity>> value : registry.values()) Collections.sort(value);
            sortNeeded = false;
        }
        var list = registry.get(entity.getClass());
        if (list == null) return Collections.emptyIterator();
        return list.stream()
                .filter(rec -> rec.getEntityMather().test(entity))
                .iterator();
    }

}
