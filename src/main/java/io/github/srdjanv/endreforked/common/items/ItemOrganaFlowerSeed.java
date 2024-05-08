package io.github.srdjanv.endreforked.common.items;

import io.github.srdjanv.endreforked.common.ModItems;
import io.github.srdjanv.endreforked.common.items.base.BaseMetaItemBlock;
import io.github.srdjanv.endreforked.common.items.base.ItemBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemOrganaFlowerSeed extends ItemBase {
    protected final BaseMetaItemBlock flower;
    public ItemOrganaFlowerSeed() {
        super("organa_flower_seed");
        flower = ModItems.ORGANA_FLOWER.get();
    }

    @Override public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return flower.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
