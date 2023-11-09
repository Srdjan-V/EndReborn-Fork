package endreborn.common.items;

import endreborn.common.items.base.BaseSeeds;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import endreborn.EndReborn;
import endreborn.common.ModBlocks;
import endreborn.utils.IHasModel;
import org.jetbrains.annotations.NotNull;

public class ItemDragoniteSeeds extends BaseSeeds implements IHasModel, IPlantable {
    public static final EnumPlantType DRAGONITE = EnumPlantType.getPlantType("dragonite");

    public ItemDragoniteSeeds(String name) {
        super(name, ModBlocks.CROP_DRAGONITE.get(), ModBlocks.ENTROPY_END_STONE.get());
    }

    @Override
    public @NotNull EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return DRAGONITE;
    }

    @Override
    public void registerModels() {
        EndReborn.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
