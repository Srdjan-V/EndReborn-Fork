package io.github.srdjanv.endreforked.api.worldgen.features;

import java.util.*;

import net.minecraft.nbt.*;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import org.jetbrains.annotations.ApiStatus;

import io.github.srdjanv.endreforked.EndReforked;
import io.github.srdjanv.endreforked.Tags;
import io.github.srdjanv.endreforked.api.worldgen.DimConfig;
import io.github.srdjanv.endreforked.api.worldgen.base.Locator;
import io.github.srdjanv.endreforked.api.worldgen.base.PositionedFeature;

public class WorldGenStructure extends PositionedFeature {

    public static final PlacementSettings defaultSettings = new PlacementSettings().setIgnoreEntities(false)
            .setIgnoreStructureBlock(false).setMirror(Mirror.NONE).setRotation(Rotation.NONE);
    protected final ResourceLocation location;
    protected final PlacementSettings settings;

    @ApiStatus.Internal
    public WorldGenStructure(Locator locator, DimConfig dimConfig, String name) {
        this(locator, dimConfig, name, defaultSettings);
    }

    @ApiStatus.Internal
    public WorldGenStructure(Locator locator, DimConfig dimConfig, String name, PlacementSettings settings) {
        this(locator, dimConfig, new ResourceLocation(Tags.MODID, name), settings);
    }

    public WorldGenStructure(Locator locator, DimConfig dimConfig, ResourceLocation location,
                             PlacementSettings settings) {
        super(locator, dimConfig);
        this.location = location;
        this.settings = settings;
    }

    @Override
    public boolean doGenerate(WorldServer server, Random rand, BlockPos pos) {
        TemplateManager manager = server.getStructureTemplateManager();
        Template template = manager.get(null, location);

        if (Objects.isNull(template)) {
            EndReforked.LOGGER.error("Unable to find template for structure: {}", location);
            return false;
        }

        // probably not needed
        // IBlockState state = world.getBlockState(pos);
        // world.notifyBlockUpdate(pos, state, state, 3);
        template.addBlocksToWorld(server, pos.add(2, 0, 2), settings);
        return true;
    }
}
