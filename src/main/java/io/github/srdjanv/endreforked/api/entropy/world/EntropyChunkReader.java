package io.github.srdjanv.endreforked.api.entropy.world;

import io.github.srdjanv.endreforked.api.base.util.DimPos;
import io.github.srdjanv.endreforked.api.capabilities.entropy.EntropyChunk;
import io.github.srdjanv.endreforked.api.entropy.EntropyRange;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class EntropyChunkReader {
    private final ChunkEntropyView entropyView;

    public EntropyChunkReader(
            Supplier<DimPos> centerPosSup,
            Function<DimPos, Optional<EntropyChunk>> resolver) {
        this(centerPosSup, resolver, EntropyRange.ONE);
    }

    public EntropyChunkReader(
            Supplier<DimPos> centerPosSup,
            Function<DimPos, Optional<EntropyChunk>> resolver,
            EntropyRange radius) {
        entropyView = new ChunkEntropyView(centerPosSup, resolver, radius);
    }

    public EntropyRange getRadius() {
        return entropyView.getRadius();
    }

    @NotNull
    public ChunkEntropyView getEntropyView() {
        entropyView.buildView();
        return entropyView;
    }

    @Nullable
    public EntropyChunk getCenterEntropy() {
        entropyView.buildView();
        return entropyView.getCenterEntropy();
    }

    public static EntropyChunkReader ofEntity(Entity entity) {
        return ofEntity(entity, EntropyRange.ONE);
    }

    public static EntropyChunkReader ofEntity(Entity entity, EntropyRange radius) {
        return new EntropyChunkReader(() -> {
            var world = entity.world;
            var tilePos = entity.getPosition();
            if (world == null || tilePos == null) return null;
            return new DimPos(world.provider.getDimension(), tilePos);
        }, EntropyWorldHandler::getEntropyChunkWorld, radius);
    }

    public static EntropyChunkReader ofTileEntity(TileEntity tileEntity) {
        return ofTileEntity(tileEntity, EntropyRange.ONE);
    }

    public static EntropyChunkReader ofTileEntity(TileEntity tileEntity, EntropyRange radius) {
        return new EntropyChunkReader(() -> {
            var world = tileEntity.getWorld();
            var tilePos = tileEntity.getPos();
            if (world == null || tilePos == null) return null;
            return new DimPos(world.provider.getDimension(), tilePos);
        }, EntropyWorldHandler::getEntropyChunkWorld, radius);
    }
}
