package io.github.srdjanv.endreforked.common.capabilities.entropy;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
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
    @CapabilityInject(ChunkEntropy.class)
    public static Capability<ChunkEntropy> CHUNK_ENTROPY;
    public static final ResourceLocation CHUNK_ENTROPY_LOC = new ResourceLocation("endreforked", "ChunkEntropy");

    @SubscribeEvent
    public static void attachChunkEntropyCap(AttachCapabilitiesEvent<Chunk> event) {
        if (event.getObject().getWorld().isRemote) {
            event.addCapability(CHUNK_ENTROPY_LOC, new ChunkEntropyProvider(event.getObject()));
        }
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(ChunkEntropy.class, new Capability.IStorage<>() {
            @Nullable @Override public NBTBase writeNBT(Capability<ChunkEntropy> capability, ChunkEntropy instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override public void readNBT(Capability<ChunkEntropy> capability, ChunkEntropy instance, EnumFacing side, NBTBase nbt) {
                instance.deserializeNBT((NBTTagCompound) nbt);
            }
        }, () -> null);
    }
}
