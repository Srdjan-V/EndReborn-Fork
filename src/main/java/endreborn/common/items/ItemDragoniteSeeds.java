package endreborn.common.items;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import org.jetbrains.annotations.NotNull;

import endreborn.common.ModBlocks;
import endreborn.common.items.base.BaseSeeds;
import endreborn.utils.models.InventoryItemModel;

public class ItemDragoniteSeeds extends BaseSeeds implements InventoryItemModel, IPlantable {

    public static final EnumPlantType DRAGONITE = EnumPlantType.getPlantType("dragonite");

    public ItemDragoniteSeeds(String name) {
        super(name, ModBlocks.CROP_DRAGONITE.get(), ModBlocks.ENTROPY_END_STONE.get());
    }

    @Override
    public @NotNull EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return DRAGONITE;
    }
}
