package endreborn.api.base;

import org.jetbrains.annotations.Nullable;

import endreborn.api.util.ComparingLang;
import endreborn.api.util.FluidStackHashStrategy;
import endreborn.api.util.ItemStackHashStrategy;
import it.unimi.dsi.fastutil.Hash;

public interface HashStrategyTranslator<S extends Hash.Strategy<?>> {

    S getHashStrategy();

    @Nullable
    default String translateHashStrategy() {
        if (getHashStrategy() instanceof FluidStackHashStrategy fluid) {
            return ComparingLang.FluidLang.translate(fluid, fluid.customCheckLangKey());
        } else if (getHashStrategy() instanceof ItemStackHashStrategy item) {
            return ComparingLang.ItemLang.translate(item, item.customCheckLangKey());
        }
        return null;
    }
}
