package endreborn.common.world.gen;

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

public class WorldGenStructure extends WorldGenerator {

    public static final PlacementSettings settings = (new PlacementSettings()).setChunk(null).setIgnoreEntities(false)
            .setIgnoreStructureBlock(false).setMirror(Mirror.NONE).setRotation(Rotation.NONE);
    private final String structureName;

    public WorldGenStructure(String name) {
        this.structureName = name;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
        if (!(world instanceof WorldServer server)) {
            EndReborn.LOGGER.warn("Unable to run world generator on ClientWorld");
            return false;
        }

        TemplateManager manager = server.getStructureTemplateManager();
        Template template = manager.get(null, new ResourceLocation(Reference.MODID, structureName));

        if (template == null) {
            EndReborn.LOGGER.error("Unable to find template for structure: {}", structureName);
            return false;
        }

        // probably not needed
        // IBlockState state = world.getBlockState(pos);
        // world.notifyBlockUpdate(pos, state, state, 3);
        template.addBlocksToWorldChunk(world, pos, settings);
        return true;
    }
}
