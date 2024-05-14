package io.github.srdjanv.endreforked.common.tiles.base;

import io.github.srdjanv.endreforked.api.base.util.Ticker;
import io.github.srdjanv.endreforked.api.entropy.EntropyRadius;
import io.github.srdjanv.endreforked.api.entropy.EntropyDataProvider;
import io.github.srdjanv.endreforked.api.entropy.world.EntropyChunkReader;
import io.github.srdjanv.endreforked.common.entropy.chunks.PassiveEntropyChunkInducer;
import net.minecraft.util.ITickable;

import java.util.Optional;

public abstract class BasePassiveInducer extends BaseTileEntity implements ITickable, EntropyDataProvider {
    private final EntropyRadius range;
    private final EntropyChunkReader tileWrapper;
    private final PassiveEntropyChunkInducer inducer;

    public BasePassiveInducer(EntropyRadius range, int frequency, int entropy) {
        this.range = range;
        this.tileWrapper = EntropyChunkReader.ofTileEntity(this, range);
        this.inducer = new PassiveEntropyChunkInducer(tileWrapper, new Ticker(frequency), entropy);
    }

    @Override public Optional<EntropyRadius> getEntropyRadius() {
        return Optional.of(range);
    }

    @Override public Optional<PassiveInducer> getPassiveInducer() {
        return Optional.of(inducer.getPassiveInducer());
    }

    @Override public void update() {
        inducer.induce();
    }

}
