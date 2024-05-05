package io.github.srdjanv.endreforked.common.capabilities.entropy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;

import java.util.concurrent.ThreadLocalRandom;

public class EntropyInducer {
    private ChunkEntropy chunkEntropy;
    private final int frequency;
    private int tick;

    public EntropyInducer(int frequency) {
        this.frequency = frequency;
        tick = ThreadLocalRandom.current().nextInt(frequency);
    }

    public void induceEntropy(WorldServer server, TileEntity tile, int entropy) {
        if (++tick % frequency != 0) return;
        if (!initCap(server, tile)) return;

    }


    private boolean initCap(WorldServer server, TileEntity tile) {
        if (chunkEntropy == null) {
            chunkEntropy = server.getChunk(tile.getPos()).getCapability(CapabilityEntropyHandler.CHUNK_ENTROPY, null);
            return chunkEntropy != null;
        }
        var chunkPos = new ChunkPos(tile.getPos());
        if (chunkPos.equals(chunkEntropy.getChunkPos())) return true;
        chunkEntropy = server.getChunk(tile.getPos()).getCapability(CapabilityEntropyHandler.CHUNK_ENTROPY, null);
        return chunkEntropy != null;
    }
}
