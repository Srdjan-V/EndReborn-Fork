package endreborn.api.base;

import java.util.Collection;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import endreborn.api.base.groupings.RecipeGrouping;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;

public abstract class HandlerRegistry<IN1, IN2, OUT,
        RG extends RecipeGrouping<IN1, IN2, R>,
        R extends Recipe<IN1, IN2, OUT>>
                                     implements HashStrategyTranslator<Hash.Strategy<IN1>> {

    public abstract Hash.Strategy<IN1> getHashStrategy();

    public final Map<IN1, RG> register = new Object2ObjectOpenCustomHashMap<>(getHashStrategy());

    public Map<IN1, RG> getRegister() {
        return register;
    }

    public Collection<RG> getRecipeGroupings() {
        return register.values();
    }

    public boolean registerRecipeGrouping(IN1 target, RG grouping) {
        register.put(target, grouping);
        return true;
    }

    public boolean registerRecipeGrouping(RG grouping) {
        register.put(grouping.getGrouping(), grouping);
        return true;
    }

    @Nullable
    public RG findRecipeGrouping(IN1 input) {
        return register.get(input);
    }

    public boolean registerRecipeToGrouping(RG grouping, R recipe) {
        grouping.registerRecipe(recipe);
        return true;
    }

    @Nullable
    public R findRecipe(RG grouping, IN2 input) {
        return grouping.findRecipe(input);
    }
}
