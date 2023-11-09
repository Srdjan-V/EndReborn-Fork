package endreborn.common.blocks;

import net.minecraft.block.BlockFire;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import endreborn.EndReborn;
import endreborn.utils.models.InventoryBlockModel;

public class BlockColdFire extends BlockFire implements InventoryBlockModel {

    public BlockColdFire(String name) {
        super();
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityWalk(worldIn, pos, entityIn);
    }
}
