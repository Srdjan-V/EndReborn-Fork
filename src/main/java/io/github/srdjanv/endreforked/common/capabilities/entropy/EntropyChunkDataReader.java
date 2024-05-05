package io.github.srdjanv.endreforked.common.capabilities.entropy;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class EntropyChunkDataReader<D> {
    private final Function<D, BlockPos> posFunction;
    private final ChunkEntropyView entropyView;
    private ChunkEntropy centerEntropy = null;

    public EntropyChunkDataReader(Function<D, BlockPos> posFunction) {
        this(posFunction, 1);
    }

    public EntropyChunkDataReader(Function<D, BlockPos> posFunction, int radius) {
        this.posFunction = posFunction;
        entropyView = new ChunkEntropyView(radius);
    }

    @NotNull
    public ChunkEntropyView getEntropyView(D data) {
        var resData = resolveData(data);
        if (resData != null) entropyView.buildView(resData, this, resolveCenterChunkEntropy(data));
        return entropyView;
    }

    @Nullable
    public ChunkEntropy resolveCenterChunkEntropy(D data) {
        final var pos = posFunction.apply(data);

        if (centerEntropy != null && (!centerEntropy.getChunkPos().equals(new ChunkPos(pos))))
            centerEntropy = null;

        if (centerEntropy == null)
            centerEntropy = resolveChunkEntropy(data, pos);

        return centerEntropy;
    }

    @Nullable
    public abstract WorldServer resolveData(D data);

    @Nullable
    public ChunkEntropy resolveChunkEntropy(D data, BlockPos pos) {
        var resData = resolveData(data);
        if (resData == null) return null;
        return resolveChunkEntropy(resData, pos);
    }

    @Nullable
    public ChunkEntropy resolveChunkEntropy(WorldServer server, BlockPos pos) {
        var chunk = server.getChunk(pos);
        if (chunk.hasCapability(CapabilityEntropyHandler.CHUNK_ENTROPY, null)) {
            return chunk.getCapability(CapabilityEntropyHandler.CHUNK_ENTROPY, null);
        }
        return null;
    }

    public static final class TileEntity extends EntropyChunkDataReader<net.minecraft.tileentity.TileEntity> {
        public TileEntity() {
            super(net.minecraft.tileentity.TileEntity::getPos, 1);
        }

        public TileEntity(int radius) {
            super(net.minecraft.tileentity.TileEntity::getPos, radius);
        }

        @Override public WorldServer resolveData(net.minecraft.tileentity.TileEntity data) {
            if (!data.getWorld().isRemote) return (WorldServer) data.getWorld();
            return null;
        }
    }

    public static final class EntityPlayer extends EntropyChunkDataReader<net.minecraft.entity.player.EntityPlayer> {
        public EntityPlayer() {
            super(net.minecraft.entity.player.EntityPlayer::getPosition, 1);
        }

        public EntityPlayer(int radius) {
            super(net.minecraft.entity.player.EntityPlayer::getPosition, radius);
        }

        @Override public @Nullable WorldServer resolveData(net.minecraft.entity.player.EntityPlayer data) {
            if (!data.world.isRemote) return (WorldServer) data.world;
            return null;
        }
    }

}
