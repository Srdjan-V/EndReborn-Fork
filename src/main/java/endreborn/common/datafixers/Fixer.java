package endreborn.common.datafixers;

import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;

import endreborn.Reference;
import endreborn.common.datafixers.items.Tungsten;

public final class Fixer {

    public static void init() {
        ModFixs modFixer = FMLCommonHandler.instance().getDataFixer().init(Reference.MODID,
                Reference.DATAFIXER_VERSION);
        modFixer.registerFix(FixTypes.ITEM_INSTANCE, new Tungsten());
    }
}
