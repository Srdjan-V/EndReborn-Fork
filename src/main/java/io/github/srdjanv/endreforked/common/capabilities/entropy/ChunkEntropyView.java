package io.github.srdjanv.endreforked.common.capabilities.entropy;

import io.github.srdjanv.endreforked.api.entropy.storage.WeekEntropyStorage;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ChunkEntropyView implements WeekEntropyStorage {
    private final int radius;
    private ChunkEntropy centerEntropy = null;
    private final Map<ChunkPos, ChunkEntropy> chunkEntropies = new Object2ObjectArrayMap<>();
    private final List<ChunkEntropy> sortedEntropies = new ObjectArrayList<>();
    private final Map<ChunkPos, ChunkEntropy> unmodifiedChunkEntropies = Collections.unmodifiableMap(chunkEntropies);

    public ChunkEntropyView(int radius) {
        this.radius = radius;
    }

    @UnmodifiableView
    public Map<ChunkPos, ChunkEntropy> getView() {
        return unmodifiedChunkEntropies;
    }

    @Override public int genDecayFrequency() {
        return (int) sortedEntropies.stream().mapToInt(WeekEntropyStorage::genDecayFrequency).average().orElse(0);
    }

    @Override public int getDecay() {
        return (int) sortedEntropies.stream().mapToInt(WeekEntropyStorage::getDecay).average().orElse(0);
    }

    @Override public int getMaxEntropy() {
        return sortedEntropies.stream().mapToInt(ChunkEntropy::getMaxEntropy).sum();
    }

    @Override public int getMinEntropy() {
        return sortedEntropies.stream().mapToInt(ChunkEntropy::getMinEntropy).sum();
    }

    @Override public int getCurrentEntropy() {
        return sortedEntropies.stream().mapToInt(WeekEntropyStorage::getCurrentEntropy).sum();
    }

    @Override public int induceEntropy(int entropy, boolean simulate) {
        int remain = 0;
        for (ChunkEntropy chunkEntropy : sortedEntropies) {
            remain += chunkEntropy.induceEntropy(entropy, simulate);
        }
        return remain;
    }

    @Override public int drainEntropy(int entropy, boolean simulate) {
        int remain = 0;
        for (ChunkEntropy chunkEntropy : sortedEntropies) {
            remain += chunkEntropy.drainEntropy(entropy, simulate);
        }
        return remain;
    }

    void buildView(WorldServer server, EntropyChunkDataReader<?> reader, @Nullable ChunkEntropy centerEntropy) {
        if (centerEntropy == null) return;
        if (this.centerEntropy != null && this.centerEntropy.getChunkPos().equals(centerEntropy.getChunkPos()))
            return;
        this.centerEntropy = centerEntropy;
        var chunks = getChunksInRadius(centerEntropy.getChunkPos(), radius);
        chunkEntropies.clear();
        sortedEntropies.clear();
        for (var chunk : chunks) {
            var chunkEntropy = reader.resolveChunkEntropy(server, chunk.getBlock(8, 0, 8));
            if (chunkEntropy == null) continue;
            chunkEntropies.put(chunk, chunkEntropy);
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
