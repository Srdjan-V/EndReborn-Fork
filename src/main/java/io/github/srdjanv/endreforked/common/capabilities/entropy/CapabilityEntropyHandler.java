package io.github.srdjanv.endreforked.common.capabilities.entropy;

import io.github.srdjanv.endreforked.Tags;
import io.github.srdjanv.endreforked.api.capabilities.entropy.EntropyStorage;
import io.github.srdjanv.endreforked.api.capabilities.entropy.WeekEntropyStorage;
import io.github.srdjanv.endreforked.common.entropy.storage.DefaultEntropyStorage;
import io.github.srdjanv.endreforked.common.entropy.storage.DefaultWeekEntropyStorage;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

public class CapabilityEntropyHandler {
    @CapabilityInject(EntropyStorage.class)
    public static Capability<EntropyStorage> INSTANCE;
    public static final ResourceLocation ENTROPY_STORAGE_LOC = new ResourceLocation(Tags.MODID, "entropy_storage");

    @CapabilityInject(WeekEntropyStorage.class)
    public static Capability<WeekEntropyStorage> WEEK_INSTANCE;
    public static final ResourceLocation WEEK_ENTROPY_STORAGE_LOC = new ResourceLocation(Tags.MODID, "week_entropy_storage");


    @SubscribeEvent
    public static void attachChunkEntropyCap(AttachCapabilitiesEvent<Chunk> event) {
        if (!event.getObject().getWorld().isRemote) {
            event.addCapability(ENTROPY_STORAGE_LOC, new ChunkEntropyProvider(event.getObject()));
        }
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(EntropyStorage.class, new Capability.IStorage<>() {
            @Nullable @Override public NBTBase writeNBT(Capability<EntropyStorage> capability, EntropyStorage instance, EnumFacing side) {
                return new NBTTagInt(instance.getCurrentEntropy());
            }

            @Override public void readNBT(Capability<EntropyStorage> capability, EntropyStorage instance, EnumFacing side, NBTBase nbt) {
                if (!(instance instanceof DefaultEntropyStorage defInst))
                    throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
                defInst.setEntropy(((NBTTagInt)nbt).getInt());
            }
        }, () -> new DefaultEntropyStorage(1_000));

        CapabilityManager.INSTANCE.register(WeekEntropyStorage.class, new Capability.IStorage<>() {
            @Nullable @Override public NBTBase writeNBT(Capability<WeekEntropyStorage> capability, WeekEntropyStorage instance, EnumFacing side) {
                var tag = new NBTTagCompound();
                tag.setInteger("entropy", instance.getCurrentEntropy());
                tag.setInteger("decay", instance.getDecay());
                return tag;
            }

            @Override public void readNBT(Capability<WeekEntropyStorage> capability, WeekEntropyStorage instance, EnumFacing side, NBTBase nbt) {
                if (!(instance instanceof DefaultWeekEntropyStorage defInst))
                    throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
                var tag = (NBTTagCompound) nbt;
                defInst.setEntropy(tag.getInteger("entropy"));
                defInst.setDecay(tag.getInteger("decay"));
            }
        }, () -> new DefaultWeekEntropyStorage(1_000, 10));
    }
}
