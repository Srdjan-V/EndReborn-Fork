package endreborn.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class TungstenOre extends BlockBase {

    public TungstenOre(String name, Material material) {
        super(name, material);

        setSoundType(SoundType.STONE);
        setHardness(4.0F);
        setResistance(25.0F);
        setHarvestLevel("pickaxe", 2);
    }
}
