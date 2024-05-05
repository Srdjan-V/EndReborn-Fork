package io.github.srdjanv.endreforked.common.capabilities.entropy;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class EntropyReader<D> {
    private final Function<D, BlockPos> posFunction;
    private ChunkEntropy lastEntropy = null;

    public EntropyReader(Function<D, BlockPos> posFunction) {
        this.posFunction = posFunction;
    }

    @Nullable
    public ChunkEntropy getChunkEntropy(D data) {
        final var pos = posFunction.apply(data);

        if (lastEntropy != null && (!lastEntropy.getChunkPos().equals(new ChunkPos(pos))))
            lastEntropy = null;

        if (lastEntropy == null)
            lastEntropy = resolveChunkEntropy(data, pos);

        return lastEntropy;
    }

    @Nullable
    public abstract ChunkEntropy resolveChunkEntropy(D data, BlockPos pos);

    @Nullable
    public ChunkEntropy resolveChunkEntropy(WorldServer server, BlockPos pos) {
        var chunk = server.getChunk(pos);
        if (chunk.hasCapability(CapabilityEntropyHandler.CHUNK_ENTROPY, null)) {
            return chunk.getCapability(CapabilityEntropyHandler.CHUNK_ENTROPY, null);
        }
        return null;
    }
}
