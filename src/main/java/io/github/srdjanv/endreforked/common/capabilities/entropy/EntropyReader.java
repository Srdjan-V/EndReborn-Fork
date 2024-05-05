package io.github.srdjanv.endreforked.common.capabilities.entropy;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class EntropyReader {
    private final Function<WorldServer, BlockPos> posFunction;
    private ChunkEntropy lastEntropy = null;

    public EntropyReader(Function<WorldServer, BlockPos> posFunction) {
        this.posFunction = posFunction;
    }

    @Nullable
    public ChunkEntropy getChunkEntropy(WorldServer server) {
        final var pos = posFunction.apply(server);

        if (lastEntropy != null && (!lastEntropy.getChunkPos().equals(new ChunkPos(pos))))
            lastEntropy = null;

        if (lastEntropy == null)
            lastEntropy = resolverChunkEntropy(server, pos);

        return lastEntropy;
    }

    @Nullable
    public ChunkEntropy resolverChunkEntropy(WorldServer server, BlockPos pos) {
        var chunk = server.getChunk(pos);
        if (chunk.hasCapability(CapabilityEntropyHandler.CHUNK_ENTROPY, null)) {
            return chunk.getCapability(CapabilityEntropyHandler.CHUNK_ENTROPY, null);
        }
        return null;
    }
}
