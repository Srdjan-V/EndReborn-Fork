package endreborn.api.worldgen.features;

import java.util.Objects;
import java.util.Random;

import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import endreborn.EndReborn;
import endreborn.Reference;
import endreborn.api.worldgen.DimConfig;

public class WorldGenStructure extends WorldGenerator {

    public static final PlacementSettings defaultSettings = (new PlacementSettings()).setIgnoreEntities(false)
            .setIgnoreStructureBlock(false).setMirror(Mirror.NONE).setRotation(Rotation.NONE);
    protected final DimConfig dimConfig;
    protected final ResourceLocation location;
    protected final PlacementSettings settings;

    public WorldGenStructure(ResourceLocation location, DimConfig dimConfig) {
        this(dimConfig, location, defaultSettings);
    }

    public WorldGenStructure(String name, DimConfig dimConfig) {
        this(dimConfig, new ResourceLocation(Reference.MODID, name), defaultSettings);
    }

    public WorldGenStructure(DimConfig dimConfig, ResourceLocation location, PlacementSettings settings) {
        this.dimConfig = dimConfig;
        this.location = location;
        this.settings = settings;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
        if (!(world instanceof WorldServer server)) {
            EndReborn.LOGGER.warn("Unable to run world generator on ClientWorld");
            return false;
        }

        TemplateManager manager = server.getStructureTemplateManager();
        Template template = manager.get(null, location);

        if (Objects.isNull(template)) {
            EndReborn.LOGGER.error("Unable to find template for structure: {}", location);
            return false;
        }

        // probably not needed
        // IBlockState state = world.getBlockState(pos);
        // world.notifyBlockUpdate(pos, state, state, 3);
        template.addBlocksToWorldChunk(world, pos.add(2, 0, 2), settings);
        return true;
    }
}
