package io.github.srdjanv.endreforked.common.entropy.chunks;

import io.github.srdjanv.endreforked.api.entropy.storage.WeekEntropyStorage;
import io.github.srdjanv.endreforked.common.capabilities.entropy.ChunkEntropy;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChunkEntropyView implements WeekEntropyStorage {
    private final int radius;
    private ChunkEntropy centerEntropy = null;
    private final List<ChunkEntropy> sortedEntropies = new ObjectArrayList<>();
    private final List<ChunkEntropy> unmodifiableSortedEntropies = Collections.unmodifiableList(sortedEntropies);

    public ChunkEntropyView(int radius) {
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
            accepted -= chunkEntropy.induceEntropy(accepted, simulate);
        }
        return entropy - accepted;
    }

    @Override public int drainEntropy(int entropy, boolean simulate) {
        int accepted = entropy;
        for (ChunkEntropy chunkEntropy : sortedEntropies) {
            accepted -= chunkEntropy.drainEntropy(entropy, simulate);
        }
        return entropy - accepted;
    }

    public void buildView(WorldServer server, EntropyChunkDataWrapper<?> reader, @Nullable ChunkEntropy centerEntropy) {
        if (centerEntropy == null) return;
        if (this.centerEntropy != null && this.centerEntropy.getChunkPos().equals(centerEntropy.getChunkPos()))
            return;
        this.centerEntropy = centerEntropy;
        var chunks = getChunksInRadius(centerEntropy.getChunkPos(), radius);
        sortedEntropies.clear();
        for (var chunk : chunks) {
            var chunkEntropy = reader.resolveChunkEntropy(server, chunk.getBlock(8, 0, 8));
            if (chunkEntropy == null) continue;
            sortedEntropies.add(chunkEntropy);
        }
        sortedEntropies.sort(Comparator.comparingInt(ChunkEntropy::getCurrentEntropy));
    }

    private static List<ChunkPos> getChunksInRadius(ChunkPos center, int radius) {
        List<ChunkPos> chunks = new ObjectArrayList<>();

        for (int x = center.x - radius; x <= center.x + radius; x++) {
            for (int z = center.z - radius; z <= center.z + radius; z++) {
                chunks.add(new ChunkPos(x, z));
            }
        }
        return chunks;
    }

}
