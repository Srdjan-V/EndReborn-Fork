package endreborn.common.datafixers.fixers;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
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

public class Xorcite implements IFixableData {

    private static final Map<ResourceLocation, ResourceLocation> RESOURCE_LOCATION = new HashMap<>();

    static {
        RESOURCE_LOCATION.put(new ResourceLocation(Reference.MODID, "dragon_essence"),
                new ResourceLocation(Reference.MODID, "xorcite_block"));
        RESOURCE_LOCATION.put(new ResourceLocation(Reference.MODID, "death_essence"),
                new ResourceLocation(Reference.MODID, "xorcite_ingot"));
    }

    public Xorcite() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getFixVersion() {
        return 1;
    }

    @Override
    public @NotNull NBTTagCompound fixTagCompound(@NotNull NBTTagCompound compound) {
        return compound;
    }

    @SubscribeEvent
    public void missingItemMapping(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            ResourceLocation oldName = entry.key;
            ResourceLocation newName = RESOURCE_LOCATION.get(oldName);
            if (newName != null) {
                Item newItem = ForgeRegistries.ITEMS.getValue(newName);
                if (newItem != null) entry.remap(newItem);
            }
        }
    }

    @SubscribeEvent
    public void missingBlockMapping(RegistryEvent.MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> entry : event.getAllMappings()) {
            ResourceLocation oldName = entry.key;
            ResourceLocation newName = RESOURCE_LOCATION.get(oldName);
            if (newName != null) {
                var newBlock = ForgeRegistries.BLOCKS.getValue(newName);
                if (newBlock != null) entry.remap(newBlock);
            }
        }
    }
}
