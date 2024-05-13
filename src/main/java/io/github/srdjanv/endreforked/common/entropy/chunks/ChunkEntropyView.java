package io.github.srdjanv.endreforked.common.entropy.chunks;

import io.github.srdjanv.endreforked.api.capabilities.entropy.EntropyChunk;
import io.github.srdjanv.endreforked.api.capabilities.entropy.WeakEntropyStorage;
import io.github.srdjanv.endreforked.api.entropy.IEntropyDataProvider;
import io.github.srdjanv.endreforked.api.entropy.EntropyRange;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class ChunkEntropyView implements WeakEntropyStorage, IEntropyDataProvider {
    private final EntropyRange radius;
    private EntropyChunk centerEntropy = null;
    private final List<EntropyChunk> sortedEntropies = new ObjectArrayList<>();
    private final List<EntropyChunk> unmodifiableSortedEntropies = Collections.unmodifiableList(sortedEntropies);

    public ChunkEntropyView(EntropyRange radius) {
        this.radius = radius;
    }

    public EntropyRange getRadius() {
        return radius;
    }

    @UnmodifiableView
    public List<EntropyChunk> getView() {
        return unmodifiableSortedEntropies;
    }

    @Override public double getLoadFactor() {
        return sortedEntropies.stream().mapToDouble(WeakEntropyStorage::getLoadFactor).average().orElse(0);
    }

    @Override public void setLoadFactor(double loadFactor) {
        sortedEntropies.forEach(e -> e.setLoadFactor(loadFactor));
    }

    @Override public boolean isOverLoaded() {
        return sortedEntropies.stream().anyMatch(WeakEntropyStorage::isOverLoaded);
    }

    @Override public int getDecay() {
        return (int) sortedEntropies.stream().mapToInt(WeakEntropyStorage::getDecay).average().orElse(0);
    }

    @Override public void setDecay(int decay) {
        for (EntropyChunk defaultChunkEntropy : sortedEntropies) {
            defaultChunkEntropy.setDecay(decay);
        }
    }

    @Override public int getMaxEntropy() {
        return sortedEntropies.stream().mapToInt(EntropyChunk::getMaxEntropy).sum();
    }

    @Override public int getCurrentEntropy() {
        return sortedEntropies.stream().mapToInt(EntropyChunk::getCurrentEntropy).sum();
    }

    @Override public int induceEntropy(int entropy, boolean simulate) {
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

    public void buildView(Function<BlockPos, EntropyChunk> resolver, @Nullable EntropyChunk centerEntropy) {
        if (centerEntropy == null) return;
        if (this.centerEntropy != null && this.centerEntropy.getChunkPos().equals(centerEntropy.getChunkPos()))
            return;
        this.centerEntropy = centerEntropy;
        var chunks = radius.getChunksInRadius(centerEntropy.getChunkPos());
        sortedEntropies.clear();
        for (var chunk : chunks) {
            var chunkEntropy = resolver.apply(chunk.getBlock(8, 0, 8));
            if (chunkEntropy == null) continue;
            sortedEntropies.add(chunkEntropy);
        }
        sortedEntropies.sort(Comparator
                .comparing(EntropyChunk::isOverLoaded)
                .thenComparingInt(EntropyChunk::getCurrentEntropy));
    }

}
