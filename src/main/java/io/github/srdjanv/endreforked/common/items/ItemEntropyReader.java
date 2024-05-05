package io.github.srdjanv.endreforked.common.items;

import io.github.srdjanv.endreforked.common.capabilities.entropy.EntropyChunkDataReader;
import io.github.srdjanv.endreforked.common.capabilities.entropy.IEntropyDataProvider;
import io.github.srdjanv.endreforked.common.items.base.ItemBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemEntropyReader extends ItemBase {
    private final EntropyChunkDataReader<EntityPlayer> reader;

    public ItemEntropyReader() {
        super("entropy_reader");
        reader = new EntropyChunkDataReader.EntityPlayer();
    }

    @Override public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            var block = worldIn.getBlockState(pos).getBlock();
            if (block instanceof IEntropyDataProvider provider) {
                player.sendMessage(new TextComponentString(provider.getFormattedEntropyData()));
            }
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            var data = reader.getChunkEntropy(playerIn);
            if (data != null) {
                playerIn.sendMessage(new TextComponentString(data.toString()));
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
