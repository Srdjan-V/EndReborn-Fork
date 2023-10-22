package endreborn.common.blocks;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import endreborn.EndReborn;
import endreborn.common.ModItems;
import endreborn.utils.IHasModel;

public class DragonBush extends BlockCrops implements IHasModel {

    public DragonBush(String name) {
        this.setTranslationKey(name);
        this.setRegistryName(name);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            if (this.isMaxAge(state)) {
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(ModItems.DRAGONITE_BERRIES.get(), 2)));
                worldIn.setBlockState(pos, this.withAge(3));
                return true;
            }
        }

        return false;
    }

    @Override
    protected Item getSeed() {
        return ModItems.DRAGONITE_SEEDS.get();
    }

    @Override
    protected Item getCrop() {
        return ModItems.DRAGONITE_BERRIES.get();
    }

    @Override
    public void registerModels() {
        EndReborn.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
