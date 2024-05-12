package io.github.srdjanv.endreforked.api.worldgen.features;

import java.util.*;
import java.util.function.Consumer;

import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import org.jetbrains.annotations.ApiStatus;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.Tags;
import io.github.srdjanv.endreforked.api.worldgen.GenConfig;
import io.github.srdjanv.endreforked.api.worldgen.base.Locator;
import io.github.srdjanv.endreforked.api.worldgen.base.Locators;
import io.github.srdjanv.endreforked.api.worldgen.base.PositionedFeature;
import org.jetbrains.annotations.Nullable;

public class TemplateGenerator extends PositionedFeature {

    public static final PlacementSettings DEFAULT_SETTINGS = new PlacementSettings()
            .setIgnoreEntities(false)
            .setMirror(Mirror.NONE)
            .setRotation(Rotation.NONE);

    protected final ResourceLocation location;
    protected final PlacementSettings settings;
    private Template cachedTemplate;

    private ChunksSize size = ChunksSize.ONE;
    private Consumer<PlacementSettings> conf;

    @ApiStatus.Internal
    public TemplateGenerator(GenConfig genConfig, String name) {
        this(Locators.GEN_CONFIG_MIN_MAX, genConfig, new ResourceLocation(Tags.MODID, name), DEFAULT_SETTINGS);
    }

    public TemplateGenerator(
            Locator locator, GenConfig genConfig,
            ResourceLocation location, PlacementSettings settings) {
        super(genConfig, locator);
        this.location = location;
        this.settings = settings;
    }

    public TemplateGenerator setSize(ChunksSize size) {
        this.size = size;
        return this;
    }

    public enum ChunksSize {
        ONE,
        FOUR
    }

    public TemplateGenerator applySettings(Consumer<PlacementSettings> conf) {
        this.conf = conf;
        return this;
    }

    @Nullable
    public Template resolveTemplate(WorldServer server) {
        if (cachedTemplate != null) return cachedTemplate;
        TemplateManager manager = server.getStructureTemplateManager();
        return cachedTemplate = manager.get(server.getMinecraftServer(), location);
    }

    @Nullable
    public StructureBoundingBox resolveBoundingBox(WorldServer server, BlockPos startPos) {
        var template = resolveTemplate(server);
        if (template == null) return null;
        int minX = Math.min(template.getSize().getX(), startPos.getX());
        int minY = Math.min(template.getSize().getY(), startPos.getY());
        int minZ = Math.min(template.getSize().getZ(), startPos.getZ());
        int maxX = Math.max(template.getSize().getX(), startPos.getX());
        int maxY = Math.max(template.getSize().getY(), startPos.getY());
        int maxZ = Math.max(template.getSize().getZ(), startPos.getZ());

        return new StructureBoundingBox(
                minX, minY, minZ,
                maxX, maxY, maxZ);
    }


    @Override
    public boolean doGenerate(WorldServer server, Random rand, BlockPos startPos) {
        Template template = resolveTemplate(server);
        if (Objects.isNull(template)) {
            EndReforked.LOGGER.error("Unable to find template for structure: {}", location);
            return false;
        }

        final StructureBoundingBox box;
        final ChunkPos chunkStart;
        final ChunkPos chunkEast;
        final ChunkPos chunkSouth;
        final ChunkPos chunkEnd;

        switch (size) {
            case ONE -> {
                chunkStart = new ChunkPos(startPos);
                chunkEast = chunkStart;
                chunkSouth = chunkStart;
                chunkEnd = chunkStart;

                box = new StructureBoundingBox(
                        chunkStart.getXStart(), 0, chunkStart.getZStart(),
                        chunkStart.getXEnd(), 255, chunkStart.getZEnd());
            }
            case FOUR -> {
                chunkStart = new ChunkPos(startPos);
                chunkEast = new ChunkPos(chunkStart.x + 1, chunkStart.z);
                chunkSouth = new ChunkPos(chunkStart.x, chunkStart.z + 1);
                chunkEnd = new ChunkPos(chunkStart.x + 1, chunkStart.z + 1);
                box = new StructureBoundingBox(
                        chunkStart.getXStart(), 0, chunkStart.getZStart(),
                        chunkEnd.getXEnd(), 255, chunkEnd.getZEnd());
            }
            default -> throw new IllegalStateException("Unexpected value: " + size);
        }
        settings.setBoundingBox(box);
        if (Objects.nonNull(conf)) conf.accept(settings);
        var rot = settings.getRotation();
        final var posY = startPos.getY();
        startPos = switch (settings.getMirror()) {
            case NONE -> switch (rot) {
                case NONE -> chunkStart.getBlock(0, posY, 0);
                case CLOCKWISE_90 -> chunkEast.getBlock(16, posY, 0);
                case CLOCKWISE_180 -> chunkSouth.getBlock(16, posY, 16);
                case COUNTERCLOCKWISE_90 -> chunkSouth.getBlock(0, posY, 16);
            };
            case LEFT_RIGHT -> switch (rot) {
                case CLOCKWISE_90 -> chunkStart.getBlock(0, posY, 0);
                case CLOCKWISE_180 -> chunkEast.getBlock(16, posY, 0);
                case COUNTERCLOCKWISE_90 -> chunkSouth.getBlock(16, posY, 16);
                case NONE -> chunkSouth.getBlock(0, posY, 16);
            };
            case FRONT_BACK -> switch (rot) {
                case COUNTERCLOCKWISE_90 -> chunkStart.getBlock(0, posY, 0);
                case NONE -> chunkEast.getBlock(16, posY, 0);
                case CLOCKWISE_90 -> chunkSouth.getBlock(16, posY, 16);
                case CLOCKWISE_180 -> chunkSouth.getBlock(0, posY, 16);
            };
        };

        template.addBlocksToWorld(server, startPos, settings);
        return true;
    }
}
