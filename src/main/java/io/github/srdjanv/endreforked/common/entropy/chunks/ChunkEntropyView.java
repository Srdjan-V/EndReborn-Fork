package io.github.srdjanv.endreforked.common.entropy.chunks;

import io.github.srdjanv.endreforked.api.capabilities.entropy.WeekEntropyStorage;
import io.github.srdjanv.endreforked.common.capabilities.entropy.ChunkEntropy;
import io.github.srdjanv.endreforked.common.entropy.EntropyRange;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChunkEntropyView implements WeekEntropyStorage {
    private final EntropyRange radius;
    private ChunkEntropy centerEntropy = null;
    private final List<ChunkEntropy> sortedEntropies = new ObjectArrayList<>();
    private final List<ChunkEntropy> unmodifiableSortedEntropies = Collections.unmodifiableList(sortedEntropies);

    public ChunkEntropyView(EntropyRange radius) {
        this.radius = radius;
    }

    @UnmodifiableView
    public List<ChunkEntropy> getView() {
        return unmodifiableSortedEntropies;
    }

    @Override public int getDecay() {
        return (int) sortedEntropies.stream().mapToInt(WeekEntropyStorage::getDecay).average().orElse(0);
    }

    @Override public void setDecay(int decay) {
        for (ChunkEntropy chunkEntropy : sortedEntropies) {
            chunkEntropy.setDecay(decay);
        }
    }

    @Override public int getMaxEntropy() {
        return sortedEntropies.stream().mapToInt(ChunkEntropy::getMaxEntropy).sum();
    }

    @Override public int getCurrentEntropy() {
        return sortedEntropies.stream().mapToInt(WeekEntropyStorage::getCurrentEntropy).sum();
    }

    @Override public int induceEntropy(int entropy, boolean simulate) {
        int accepted = entropy;
        for (ChunkEntropy chunkEntropy : sortedEntropies) {
            if (accepted <= 0) break;
            accepted -= chunkEntropy.induceEntropy(accepted, simulate);
        }
        for (ChunkEntropy chunkEntropy : sortedEntropies) {
            if (accepted <= 0) break;
            if (!chunkEntropy.hasEntropyStorageReference()) continue;
            accepted -= chunkEntropy.getEntropyStorageReference().drainEntropy(entropy, simulate);
        }
        return entropy - accepted;
    }

    @Override public int drainEntropy(int entropy, boolean simulate) {
        int accepted = entropy;
        for (ChunkEntropy chunkEntropy : sortedEntropies) {
            if (accepted <= 0) break;
            accepted -= chunkEntropy.drainEntropy(entropy, simulate);
        }
        for (ChunkEntropy chunkEntropy : sortedEntropies) {
            if (accepted <= 0) break;
            if (!chunkEntropy.hasEntropyStorageReference()) continue;
            accepted -= chunkEntropy.getEntropyStorageReference().drainEntropy(entropy, simulate);
        }
        return entropy - accepted;
    }

    public void buildView(WorldServer server, EntropyChunkDataWrapper<?> reader, @Nullable ChunkEntropy centerEntropy) {
        if (centerEntropy == null) return;
        if (this.centerEntropy != null && this.centerEntropy.getChunkPos().equals(centerEntropy.getChunkPos()))
            return;
        this.centerEntropy = centerEntropy;
        var chunks = radius.getChunksInRadius(centerEntropy.getChunkPos());
        sortedEntropies.clear();
        for (var chunk : chunks) {
            var chunkEntropy = reader.resolveChunkEntropy(server, chunk.getBlock(8, 0, 8));
            if (chunkEntropy == null) continue;
            sortedEntropies.add(chunkEntropy);
        }
        sortedEntropies.sort(Comparator.comparingInt(ChunkEntropy::getCurrentEntropy));
    }

}
