package io.github.srdjanv.endreforked.common.entropy.chunks;

import io.github.srdjanv.endreforked.api.capabilities.entropy.EntropyChunk;
import io.github.srdjanv.endreforked.api.entropy.world.EntropyWorldHandler;
import io.github.srdjanv.endreforked.common.capabilities.entropy.DefaultEntropyChunk;
import io.github.srdjanv.endreforked.api.entropy.EntropyRange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class EntropyChunkDataWrapper<D> {
    private final Function<D, BlockPos> posFunction;
    private final ChunkEntropyView entropyView;
    private EntropyChunk centerEntropy = null;

    public EntropyChunkDataWrapper(Function<D, BlockPos> posFunction) {
        this(posFunction, EntropyRange.ONE);
    }

    public EntropyChunkDataWrapper(Function<D, BlockPos> posFunction, EntropyRange radius) {
        this.posFunction = posFunction;
        entropyView = new ChunkEntropyView(radius);
    }

    public EntropyRange getRadius() {
        return entropyView.getRadius();
    }

    @NotNull
    public ChunkEntropyView getEntropyView(D data) {
        final var dim = resolveDim(data);
        entropyView.buildView(blockPos -> resolveChunkEntropy(dim, blockPos), getCenterEntropy(data));
        return entropyView;
    }

    @Nullable
    public EntropyChunk getCenterEntropy(D data) {
        final var pos = posFunction.apply(data);

        if (centerEntropy != null && (!centerEntropy.getChunkPos().equals(new ChunkPos(pos))))
            centerEntropy = null;

        if (centerEntropy == null)
            centerEntropy = resolveChunkEntropy(data, pos);

        return centerEntropy;
    }

    public abstract int resolveDim(D data);

    @Nullable
    public EntropyChunk resolveChunkEntropy(D data, BlockPos pos) {
        var resData = resolveDim(data);
        return resolveChunkEntropy(resData, pos);
    }

    @Nullable
    public EntropyChunk resolveChunkEntropy(int dim, BlockPos pos) {
        return EntropyWorldHandler.getEntropyChunkWorld(dim, pos).orElse(null);
    }

    public static final class TileEntity extends EntropyChunkDataWrapper<net.minecraft.tileentity.TileEntity> {
        public TileEntity() {
            super(net.minecraft.tileentity.TileEntity::getPos);
        }

        public TileEntity(EntropyRange radius) {
            super(net.minecraft.tileentity.TileEntity::getPos, radius);
        }

        @Override public int resolveDim(net.minecraft.tileentity.TileEntity data) {
            return data.getWorld().provider.getDimension();
        }
    }

    public static final class EntityPlayer extends EntropyChunkDataWrapper<net.minecraft.entity.player.EntityPlayer> {
        public EntityPlayer() {
            super(net.minecraft.entity.player.EntityPlayer::getPosition);
        }

        public EntityPlayer(EntropyRange radius) {
            super(net.minecraft.entity.player.EntityPlayer::getPosition, radius);
        }

        @Override public int resolveDim(net.minecraft.entity.player.EntityPlayer data) {
            return data.world.provider.getDimension();
        }
    }

}
