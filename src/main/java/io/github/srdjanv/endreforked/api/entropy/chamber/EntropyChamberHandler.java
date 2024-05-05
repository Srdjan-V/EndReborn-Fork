package io.github.srdjanv.endreforked.api.entropy.chamber;

import io.github.srdjanv.endreforked.api.base.HandlerRegistry;
import io.github.srdjanv.endreforked.api.base.Recipe;

public abstract class EntropyChamberHandler<IN, R extends Recipe<IN, ?>> extends HandlerRegistry<IN, R> {
}
