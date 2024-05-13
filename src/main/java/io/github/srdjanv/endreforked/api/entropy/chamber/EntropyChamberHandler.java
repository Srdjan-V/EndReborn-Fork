package io.github.srdjanv.endreforked.api.entropy.chamber;

import io.github.srdjanv.endreforked.api.base.crafting.HandlerRegistry;
import io.github.srdjanv.endreforked.api.base.crafting.Recipe;

public abstract class EntropyChamberHandler<IN, R extends Recipe<IN, ?>> extends HandlerRegistry<IN, R> {
}
