package endreborn.common.datafixers;

import java.util.Map;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IFixableData;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import com.google.common.collect.Maps;

import endreborn.Reference;
import endreborn.common.datafixers.fixers.Materializer;
import endreborn.common.datafixers.fixers.Tungsten;
import endreborn.common.datafixers.fixers.Xorcite;
import endreborn.common.datafixers.providers.BlockMappingProvider;
import endreborn.common.datafixers.providers.CommonMappingProvider;
import endreborn.common.datafixers.providers.ItemMappingProvider;
import endreborn.utils.Initializer;

public final class Fixer implements Initializer {

    private static Fixer instance;

    public static Fixer getInstance() {
        return Objects.requireNonNull(instance);
    }

    public final ModFixs modFixer;

    public Fixer() {
        instance = this;
        modFixer = FMLCommonHandler.instance().getDataFixer().init(Reference.MODID,
                Reference.DATAFIXER_VERSION);
        modFixer.registerFix(Tungsten.TYPE, new Tungsten());
        modFixer.registerFix(Xorcite.TYPE, new Xorcite());
        modFixer.registerFix(Materializer.TYPE, new Materializer());
    }

    @Override
    public void registerEventBus() {
        registerThisToEventBus();
    }

    private Map<ResourceLocation, ResourceLocation> getBlockMappings() {
        Map<ResourceLocation, ResourceLocation> mappingProviders = Maps.newHashMap();
        for (IFixableData fix : modFixer.getFixes(FixTypes.ITEM_INSTANCE)) {
            if (fix instanceof CommonMappingProvider provider)
                mappingProviders.putAll(provider.getCommonMappings());
            if (fix instanceof BlockMappingProvider provider)
                mappingProviders.putAll(provider.getBlockMappings());
        }
        return mappingProviders;
    }

    @SubscribeEvent
    public void missingBlockMapping(RegistryEvent.MissingMappings<Block> event) {
        var mappings = getBlockMappings();
        for (RegistryEvent.MissingMappings.Mapping<Block> entry : event.getAllMappings()) {
            ResourceLocation oldName = entry.key;
            ResourceLocation newName = mappings.get(oldName);
            if (newName != null) {
                var newBlock = ForgeRegistries.BLOCKS.getValue(newName);
                if (newBlock != null) entry.remap(newBlock);
            }
        }
    }

    private Map<ResourceLocation, ResourceLocation> getItemMappings() {
        Map<ResourceLocation, ResourceLocation> mappingProviders = Maps.newHashMap();
        for (IFixableData fix : modFixer.getFixes(FixTypes.ITEM_INSTANCE)) {
            if (fix instanceof CommonMappingProvider provider)
                mappingProviders.putAll(provider.getCommonMappings());
            if (fix instanceof ItemMappingProvider provider)
                mappingProviders.putAll(provider.getItemMappings());
        }
        return mappingProviders;
    }

    @SubscribeEvent
    public void missingItemMapping(RegistryEvent.MissingMappings<Item> event) {
        var mappings = getItemMappings();
        for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            ResourceLocation oldName = entry.key;
            ResourceLocation newName = mappings.get(oldName);
            if (newName != null) {
                Item newItem = ForgeRegistries.ITEMS.getValue(newName);
                if (newItem != null) entry.remap(newItem);
            }
        }
    }
}
