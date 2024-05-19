package io.github.srdjanv.endreforked.client;

import io.github.srdjanv.endreforked.common.ModBlocks;
import net.minecraft.client.Minecraft;

public class ColorsHandler {
    public static void init() {
        var blockColors = Minecraft.getMinecraft().getBlockColors();
        blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> 0xffe580ff,
                ModBlocks.ORGANA_FLOWER_STEM_BLOCK.get(), ModBlocks.ORGANA_FLOWER_STEM_DEAD_BLOCK.get());

    }
}
