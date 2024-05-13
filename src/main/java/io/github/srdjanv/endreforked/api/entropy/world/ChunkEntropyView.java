package io.github.srdjanv.endreforked.api.entropy.world;

import io.github.srdjanv.endreforked.api.base.util.DimPos;
import io.github.srdjanv.endreforked.api.capabilities.entropy.EntropyChunk;
import io.github.srdjanv.endreforked.api.capabilities.entropy.WeakEntropyStorage;
import io.github.srdjanv.endreforked.api.entropy.IEntropyDataProvider;
import io.github.srdjanv.endreforked.api.entropy.EntropyRange;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ChunkEntropyView implements WeakEntropyStorage, IEntropyDataProvider {
    private final EntropyRange radius;
    private final List<EntropyChunk> sortedEntropies = new ObjectArrayList<>();
    private final List<EntropyChunk> unmodifiableSortedEntropies = Collections.unmodifiableList(sortedEntropies);
    private final Supplier<DimPos> centerPosSup;
    private final Function<DimPos, Optional<EntropyChunk>> resolver;
    private EntropyChunk centerEntropy = null;

    public ChunkEntropyView(
            Supplier<DimPos> centerPosSup,
            Function<DimPos, Optional<EntropyChunk>> resolver,
            EntropyRange radius) {
        this.centerPosSup = centerPosSup;
        this.resolver = resolver;
        this.radius = radius;
        buildView();
    }

    public EntropyRange getRadius() {
        return radius;
    }

    public void buildView() {
        var pos = centerPosSup.get();
        if (pos == null) return;
        if (this.centerEntropy != null)
            if (this.centerEntropy.getDimPos().equals(pos)
                    && sortedEntropies.size() == radius.getChunks()
            && !validateView()) return;

        var entropyChunkOptional = resolver.apply(pos);
        if (!entropyChunkOptional.isPresent()) {
            sortedEntropies.clear();
            return;
        }
        final var centerEntropy = entropyChunkOptional.get();

        this.centerEntropy = centerEntropy;
        var chunks = radius.getChunksInRadius(centerEntropy.getDimPos());
        sortedEntropies.clear();
        for (var chunk : chunks) resolver.apply(chunk).ifPresent(sortedEntropies::add);
        sortedEntropies.sort(Comparator
                .comparing(EntropyChunk::isOverLoaded)
                .thenComparingInt(EntropyChunk::getCurrentEntropy));
    }

    public boolean validateView() {
        return sortedEntropies.stream().allMatch(EntropyChunk::isLoaded);
    }

    @UnmodifiableView
    public List<EntropyChunk> getView() {
        buildView();
        return unmodifiableSortedEntropies;
    }

    @Nullable
    public EntropyChunk getCenterEntropy() {
        return centerEntropy;
    }

    @Override public double getLoadFactor() {
        buildView();
        return sortedEntropies.stream().mapToDouble(WeakEntropyStorage::getLoadFactor).average().orElse(0);
    }

    @Override public void setLoadFactor(double loadFactor) {
        buildView();
        sortedEntropies.forEach(e -> e.setLoadFactor(loadFactor));
    }

    @Override public boolean isOverLoaded() {
        buildView();
        return sortedEntropies.stream().anyMatch(WeakEntropyStorage::isOverLoaded);
    }

    @Override public int getDecay() {
        buildView();
        return (int) sortedEntropies.stream().mapToInt(WeakEntropyStorage::getDecay).average().orElse(0);
    }

    @Override public void setDecay(int decay) {
        buildView();
        for (EntropyChunk defaultChunkEntropy : sortedEntropies) {
            defaultChunkEntropy.setDecay(decay);
        }
    }

    @Override public int getMaxEntropy() {
        buildView();
        return sortedEntropies.stream().mapToInt(EntropyChunk::getMaxEntropy).sum();
    }

    @Override public int getCurrentEntropy() {
        buildView();
        return sortedEntropies.stream().mapToInt(EntropyChunk::getCurrentEntropy).sum();
    }

    @Override public int induceEntropy(int entropy, boolean simulate) {
        buildView();
        int accepted = entropy;
        for (EntropyChunk defaultChunkEntropy : sortedEntropies) {
            if (accepted <= 0) break;
            accepted -= defaultChunkEntropy.induceEntropy(accepted, simulate);
        }
        for (EntropyChunk defaultChunkEntropy : sortedEntropies) {
            if (accepted <= 0) break;
            var ref = defaultChunkEntropy.getEntropyStorageReference();
            if (!ref.isPresent()) continue;
            accepted -= ref.get().drainEntropy(accepted, simulate);
        }
        return entropy - accepted;
    }

    @Override public int drainEntropy(int entropy, boolean simulate) {
        buildView();
        int accepted = entropy;
        for (EntropyChunk defaultChunkEntropy : sortedEntropies) {
            if (accepted <= 0) break;
            accepted -= defaultChunkEntropy.drainEntropy(accepted, simulate);
        }
        for (EntropyChunk defaultChunkEntropy : sortedEntropies) {
            if (accepted <= 0) break;
            var ref = defaultChunkEntropy.getEntropyStorageReference();
            if (!ref.isPresent()) continue;
            accepted -= ref.get().drainEntropy(accepted, simulate);
        }
        return entropy - accepted;
    }

}
