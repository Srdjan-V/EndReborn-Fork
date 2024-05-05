package io.github.srdjanv.endreforked.common.items;

import io.github.srdjanv.endreforked.common.capabilities.entropy.ChunkEntropy;
import io.github.srdjanv.endreforked.common.capabilities.entropy.EntropyReader;
import io.github.srdjanv.endreforked.common.capabilities.entropy.IEntropyDataProvider;
import io.github.srdjanv.endreforked.common.items.base.ItemBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ItemEntropyReader extends ItemBase {
    private final EntropyReader<EntityPlayerMP> reader;

    public ItemEntropyReader() {
        super("entropy_reader");
        reader = new EntropyReader<>(EntityPlayerMP::getPosition) {
            @Override public @Nullable ChunkEntropy resolveChunkEntropy(EntityPlayerMP data, BlockPos pos) {
                return resolveChunkEntropy(data.getServerWorld(), pos);
            }
        };
    }

    @Override public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            var block = worldIn.getBlockState(pos).getBlock();
            if (block instanceof IEntropyDataProvider provider) {
                var data = provider.getEntropy();
                if (data != null) player.sendMessage(new TextComponentString(data.toString()));
            }
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            var data = reader.getChunkEntropy((EntityPlayerMP) playerIn);
            if (data != null) {
                playerIn.sendMessage(new TextComponentString(data.toString()));
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
