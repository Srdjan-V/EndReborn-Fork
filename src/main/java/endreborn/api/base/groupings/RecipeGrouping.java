package endreborn.api.base.groupings;

import java.util.Map;

import org.jetbrains.annotations.Nullable;

import endreborn.api.base.HashStrategyTranslator;
import endreborn.api.base.Recipe;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;

public abstract class RecipeGrouping<IN1, IN2, R extends Recipe<IN1, IN2, ?>>
                                    implements HashStrategyTranslator<Hash.Strategy<IN2>> {

    private final IN1 grouping;
    private final Map<IN2, R> recipes;
    private final Hash.Strategy<IN2> hashStrategy;

    public RecipeGrouping(IN1 grouping, Hash.Strategy<IN2> hashStrategy) {
        this.grouping = grouping;
        this.hashStrategy = hashStrategy;
        this.recipes = new Object2ObjectOpenCustomHashMap<>(hashStrategy);
    }

    public void registerRecipe(R recipe) {
        recipes.put(recipe.getInput(), recipe);
    }

    @Nullable
    public R findRecipe(IN2 input) {
        return recipes.get(input);
    }

    public IN1 getGrouping() {
        return grouping;
    }

    public Map<IN2, R> getRecipes() {
        return recipes;
    }

    @Override
    public Hash.Strategy<IN2> getHashStrategy() {
        return hashStrategy;
    }
}
