package io.github.srdjanv.endreforked.api.entropy;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.ChunkPos;

import java.util.List;

public enum EntropyRange {
    ONE(0),
    TWO(1),
    THREE(3),
    FOR(4);

    private final int radius;
    EntropyRange(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public List<ChunkPos> getChunksInRadius(ChunkPos center) {
        List<ChunkPos> chunks = new ObjectArrayList<>();

        for (int x = center.x - radius; x <= center.x + radius; x++) {
            for (int z = center.z - radius; z <= center.z + radius; z++) {
                chunks.add(new ChunkPos(x, z));
            }
        }
        return chunks;
    }

}
