package io.github.srdjanv.endreforked.api.base.crafting;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class HandlerRegistry<IN, R extends Recipe<IN, ?>> implements HashStrategyTranslator<Hash.Strategy<IN>> {
    public abstract Hash.Strategy<IN> getHashStrategy();

    public final Map<IN, R> registry = new Object2ObjectOpenCustomHashMap<>(getHashStrategy());

    public Map<IN, R> getRegistry() {
        return registry;
    }

    public boolean registerRecipe(R recipe) {
        registry.put(recipe.getInput(), recipe);
        return true;
    }

    @Nullable
    public R findRecipe(IN input) {
        return registry.get(input);
    }

}
