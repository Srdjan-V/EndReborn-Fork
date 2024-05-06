package io.github.srdjanv.endreforked.common.entropy.chunks;

import io.github.srdjanv.endreforked.common.capabilities.entropy.CapabilityEntropyHandler;
import io.github.srdjanv.endreforked.common.capabilities.entropy.ChunkEntropy;
import io.github.srdjanv.endreforked.common.entropy.EntropyRange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class EntropyChunkDataWrapper<D> {
    private final Function<D, BlockPos> posFunction;
    private final ChunkEntropyView entropyView;
    private ChunkEntropy centerEntropy = null;

    public EntropyChunkDataWrapper(Function<D, BlockPos> posFunction) {
        this(posFunction, EntropyRange.ONE);
    }

    public EntropyChunkDataWrapper(Function<D, BlockPos> posFunction, EntropyRange radius) {
        this.posFunction = posFunction;
        entropyView = new ChunkEntropyView(radius);
    }

    @NotNull
    public ChunkEntropyView getEntropyView(D data) {
        var resData = resolveData(data);
        if (resData != null) entropyView.buildView(resData, this, getCenterEntropy(data));
        return entropyView;
    }

    @Nullable
    public ChunkEntropy getCenterEntropy(D data) {
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

    public static final class TileEntity extends EntropyChunkDataWrapper<net.minecraft.tileentity.TileEntity> {
        public TileEntity() {
            super(net.minecraft.tileentity.TileEntity::getPos);
        }

        public TileEntity(EntropyRange radius) {
            super(net.minecraft.tileentity.TileEntity::getPos, radius);
        }

        @Override public WorldServer resolveData(net.minecraft.tileentity.TileEntity data) {
            if (!data.getWorld().isRemote) return (WorldServer) data.getWorld();
            return null;
        }
    }

    public static final class EntityPlayer extends EntropyChunkDataWrapper<net.minecraft.entity.player.EntityPlayer> {
        public EntityPlayer() {
            super(net.minecraft.entity.player.EntityPlayer::getPosition);
        }

        public EntityPlayer(EntropyRange radius) {
            super(net.minecraft.entity.player.EntityPlayer::getPosition, radius);
        }

        @Override public @Nullable WorldServer resolveData(net.minecraft.entity.player.EntityPlayer data) {
            if (!data.world.isRemote) return (WorldServer) data.world;
            return null;
        }
    }

}
