package endreborn.common.blocks;

import net.minecraft.block.material.Material;

import endreborn.EndReborn;
import endreborn.common.ModItems;
import endreborn.common.blocks.base.BlockBase;
import endreborn.utils.IHasModel;

public class BlockPurpurLamp extends BlockBase implements IHasModel {

    public BlockPurpurLamp(String name) {
        super(name, Material.ROCK);
        setLightLevel(1.0F);
    }

    @Override
    public void registerModels() {
        EndReborn.proxy.registerItemRenderer(ModItems.PURPUR_LAMP.get(), 0, "inventory");
    }
}
