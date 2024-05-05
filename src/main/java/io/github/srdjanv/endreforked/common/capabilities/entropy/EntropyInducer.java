package io.github.srdjanv.endreforked.common.capabilities.entropy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
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

    public void induceEntropy(World server, TileEntity tile, int entropy) {
        if (!server.isRemote) induceEntropy((WorldServer) server, tile, entropy);
    }

    public void induceEntropy(WorldServer server, TileEntity tile, int entropy) {
        induceEntropy(server, tile.getPos(), entropy);
    }

    public void induceEntropy(World server, BlockPos pos, int entropy) {
        if (!server.isRemote) induceEntropy((WorldServer) server, pos, entropy);
    }

    public void induceEntropy(WorldServer server, BlockPos pos, int entropy) {
        if (++tick % frequency != 0) return;
        if (!initCap(server, pos)) return;
        chunkEntropy.induceEntropy(entropy, false);
    }

    private boolean initCap(WorldServer server, BlockPos pos) {
        if (chunkEntropy == null) {
            chunkEntropy = server.getChunk(pos).getCapability(CapabilityEntropyHandler.CHUNK_ENTROPY, null);
            return chunkEntropy != null;
        }
        var chunkPos = new ChunkPos(pos);
        if (chunkPos.equals(chunkEntropy.getChunkPos())) return true;
        chunkEntropy = server.getChunk(pos).getCapability(CapabilityEntropyHandler.CHUNK_ENTROPY, null);
        return chunkEntropy != null;
    }
}
