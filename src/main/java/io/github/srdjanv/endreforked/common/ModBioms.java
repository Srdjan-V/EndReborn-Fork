package io.github.srdjanv.endreforked.common;

import com.google.common.base.Suppliers;
import io.github.srdjanv.endreforked.common.bioms.OrganaBiome;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class ModBioms {
    public static final List<Supplier<? extends Biome>> END_BIOMES = new ObjectArrayList<>();

    public static final Supplier<OrganaBiome> ORGANA_BIOME = regEnd(OrganaBiome::new);

    private static <B extends Biome> Supplier<B> regEnd(com.google.common.base.Supplier<B> sup) {
        sup = Suppliers.memoize(sup);
        END_BIOMES.add(sup);
        return sup;
    }

    @SubscribeEvent
    public static void onBiomeRegister(RegistryEvent.Register<Biome> event) {
        var registry = event.getRegistry();
        END_BIOMES.stream()
                .map(Supplier::get)
                .filter(Objects::nonNull)
                .forEach(registry::register);

        BiomeDictionary.addTypes(
                ORGANA_BIOME.get(),
                BiomeDictionary.Type.DENSE,
                BiomeDictionary.Type.LUSH,
                BiomeDictionary.Type.END,
                BiomeDictionary.Type.MAGICAL,
                BiomeDictionary.Type.RARE,
                BiomeDictionary.Type.getType("ORGANA"));
    }

/*    @SubscribeEvent
    public static void registerNetherAPIEnd(@Nonnull NetherAPIRegistryEvent.End event) {
        END_BIOMES.stream()
                .map(Supplier::get)
                .filter(Objects::nonNull)
                .forEach(biome -> {
                    BiomesConfig.getInstance().get
                });

        event.registry.registerBiome(Biomes.HELL, NetherAPIConfig.hellWeight);
    }*/

}
