package io.github.srdjanv.endreforked.api.entropy.world;


import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.api.capabilities.entropy.EntropyChunk;
import io.github.srdjanv.endreforked.common.capabilities.entropy.CapabilityEntropyHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class EntropyWorld {
    static final Map<Long, Pair<WeakReference<Chunk>, EntropyChunk>> chunks = new ConcurrentHashMap<>();

    public Optional<EntropyChunk> getEntropyChunk(ChunkPos pos) {
        return getEntropyChunk(ChunkPos.asLong(pos.x, pos.z));
    }

    public Optional<EntropyChunk> getEntropyChunk(BlockPos pos) {
        return getEntropyChunk(pos.getX() >> 4, pos.getZ() >> 4);
    }

    public Optional<EntropyChunk> getEntropyChunk(int chunkX, int chunkZ) {
        return getEntropyChunk(ChunkPos.asLong(chunkX, chunkZ));
    }

    public Optional<EntropyChunk> getEntropyChunk(long pos) {
        var data = chunks.get(pos);
        if (data == null || data.getLeft().get() == null) return Optional.empty();
        return Optional.ofNullable(data.getRight());
    }

    void loadData(Chunk chunk) {
        var data = chunk.getCapability(CapabilityEntropyHandler.ENTROPY_CHUNK, null);
        if (data == null) {
            EndReforked.LOGGER.error("Chunk entropy capability not found {}", chunk.getPos());
            return;
        }
        var put = chunks.put(
                ChunkPos.asLong(chunk.x, chunk.z),
                Pair.of(new WeakReference<>(chunk), data));
        if (put != null) {
            EndReforked.LOGGER.error("Chunk entropy capability already loaded {}", chunk.getPos());
        }
    }

    void unloadData(Chunk chunk) {
        chunks.remove(ChunkPos.asLong(chunk.x, chunk.z));
    }
}
