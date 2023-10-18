package endreborn.common.datafixers.items;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import org.jetbrains.annotations.NotNull;

import endreborn.Reference;

public class Tungsten implements IFixableData {

    private static final Map<ResourceLocation, ResourceLocation> BLOCK_NAME_MAPPINGS = new HashMap<>();

    static {
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(Reference.MODID, "item_ingot_wolframium"),
                new ResourceLocation(Reference.MODID, "tungsten_ingot"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(Reference.MODID, "wolframium_nugget"),
                new ResourceLocation(Reference.MODID, "tungsten_nugget"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(Reference.MODID, "block_wolframium"),
                new ResourceLocation(Reference.MODID, "tungsten_block"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(Reference.MODID, "block_wolframium_ore"),
                new ResourceLocation(Reference.MODID, "tungsten_ore"));
    }

    public Tungsten() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getFixVersion() {
        return 1;
    }

    @Override
    public @NotNull NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        return compound;
    }

    @SubscribeEvent
    public void missingItemMapping(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            ResourceLocation oldName = entry.key;
            ResourceLocation newName = BLOCK_NAME_MAPPINGS.get(oldName);
            if (newName != null) {
                Item newItem = ForgeRegistries.ITEMS.getValue(newName);
                if (newItem != null) entry.remap(newItem);
            }
        }
    }
}
