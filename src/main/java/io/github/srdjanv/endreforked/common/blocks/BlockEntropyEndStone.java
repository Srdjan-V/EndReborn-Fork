package io.github.srdjanv.endreforked.common.blocks;

import io.github.srdjanv.endreforked.api.entropy.EntropyRadius;
import io.github.srdjanv.endreforked.api.entropy.IEntropyDataProvider;
import io.github.srdjanv.endreforked.api.entropy.world.EntropyWorldHandler;
import io.github.srdjanv.endreforked.common.ModItems;
import io.github.srdjanv.endreforked.common.blocks.base.BlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;

public class BlockEntropyEndStone extends BlockBase implements IEntropyDataProvider {
    private static final IEntropyDataProvider.PassiveInducer PASSIVE_INDUCER = new IEntropyDataProvider.PassiveInducer() {
        @Override public OptionalInt getFrequency() {
            return OptionalInt.empty();
        }

        @Override public int getInduced() {
            return 1;
        }
    };

    public BlockEntropyEndStone() {
        super("entropy_end_stone", Material.ROCK);
        setSoundType(SoundType.STONE);
        setHardness(3.0F);
        setHarvestLevel("pickaxe", 2);
        setTickRandomly(true);
    }

    @Override public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ModItems.END_MOSS_BLOCK.get();
    }

    @Override public Optional<EntropyRadius> getEntropyRadius() {
        return Optional.of(EntropyRadius.ONE);
    }

    @Override public Optional<IEntropyDataProvider.PassiveInducer> getPassiveInducer() {
        return Optional.of(PASSIVE_INDUCER);
    }

    @Override public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        EntropyWorldHandler.getEntropyChunkWorld(worldIn.provider.getDimension(), pos)
                .ifPresent(entropyChunk -> entropyChunk.induceEntropy(PASSIVE_INDUCER.getInduced(), false));
    }
}
