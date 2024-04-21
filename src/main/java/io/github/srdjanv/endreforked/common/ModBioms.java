package io.github.srdjanv.endreforked.common;

import com.google.common.base.Suppliers;
import git.jbredwards.nether_api.api.event.NetherAPIRegistryEvent;
import git.jbredwards.nether_api.mod.NetherAPI;
import git.jbredwards.nether_api.mod.common.compat.betternether.BetterNetherHandler;
import git.jbredwards.nether_api.mod.common.compat.biomesoplenty.BiomesOPlentyHandler;
import git.jbredwards.nether_api.mod.common.compat.journey_into_the_light.JITLHandler;
import git.jbredwards.nether_api.mod.common.compat.nethercraft.NethercraftHandler;
import git.jbredwards.nether_api.mod.common.compat.netherex.NetherExHandler;
import git.jbredwards.nether_api.mod.common.config.NetherAPIConfig;
import io.github.srdjanv.endreforked.common.bioms.OrganaBiome;
import io.github.srdjanv.endreforked.common.configs.bioms.BiomesConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.init.Biomes;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ModBioms {
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
