package endreborn.common.blocks;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import endreborn.EndReborn;
import endreborn.utils.models.InventoryBlockModel;

public class BlockPillar extends BlockRotatedPillar implements InventoryBlockModel {

    public BlockPillar(String name, Material material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(EndReborn.endertab);
        setSoundType(SoundType.STONE);
        setHardness(3.0F);
        setHarvestLevel("pickaxe", 2);
    }
}
