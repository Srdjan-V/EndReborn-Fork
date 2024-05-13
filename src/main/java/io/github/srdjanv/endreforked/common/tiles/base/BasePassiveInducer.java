package io.github.srdjanv.endreforked.common.tiles.base;

import io.github.srdjanv.endreforked.api.base.util.Ticker;
import io.github.srdjanv.endreforked.api.entropy.EntropyRange;
import io.github.srdjanv.endreforked.api.entropy.IEntropyDataProvider;
import io.github.srdjanv.endreforked.api.entropy.world.EntropyChunkReader;
import io.github.srdjanv.endreforked.common.entropy.chunks.PassiveEntropyChunkInducer;
import net.minecraft.util.ITickable;

import java.util.Optional;

public abstract class BasePassiveInducer extends BaseTileEntity implements ITickable, IEntropyDataProvider {
    private final EntropyRange range;
    private final EntropyChunkReader tileWrapper;
    private final PassiveEntropyChunkInducer inducer;

    public BasePassiveInducer(EntropyRange range, int frequency, int entropy) {
        this.range = range;
        this.tileWrapper = EntropyChunkReader.ofTileEntity(this);
        this.inducer = new PassiveEntropyChunkInducer(tileWrapper, new Ticker(frequency), entropy);
    }

    @Override public Optional<EntropyRange> getEntropyRange() {
        return Optional.of(range);
    }

    @Override public Optional<PassiveInducer> getPassiveInducer() {
        return Optional.of(inducer.getPassiveInducer());
    }

    @Override public void update() {
        inducer.induce();
    }

}
