package io.github.srdjanv.endreforked.api.base.crafting.processors;

import io.github.srdjanv.endreforked.api.base.crafting.EntropyRecipe;
import io.github.srdjanv.endreforked.api.base.crafting.Recipe;
import io.github.srdjanv.endreforked.api.entropy.world.EntropyChunkReader;
import io.github.srdjanv.endreforked.common.tiles.base.TileStatus;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ProcessorExecutor<IN, OUT, R extends Recipe<IN, OUT>> implements INBTSerializable<NBTTagInt> {
    protected final RecipeProcessor<IN, OUT, R> processor;
    protected final Supplier<IN> recipeFunctionSupplier;
    protected final Predicate<OUT> isOutputFull;
    protected final Predicate<IN> canExtractInput;
    protected final BiConsumer<IN, OUT> performTransfer;
    protected final Supplier<EntropyChunkReader> readerSupplier;
    protected final Consumer<TileStatus> statusUpdater;

    protected int ticksRun;

    public ProcessorExecutor(
            RecipeProcessor<IN, OUT, R> processor,
            Consumer<TileStatus> statusUpdater,
            Supplier<EntropyChunkReader> readerSupplier,
            Supplier<IN> recipeFunctionSupplier,
            Predicate<OUT> isOutputFull,
            Predicate<IN> canExtractInput,
            BiConsumer<IN, OUT> performTransfer) {
        this.processor = processor;
        this.recipeFunctionSupplier = recipeFunctionSupplier;
        this.isOutputFull = isOutputFull;
        this.canExtractInput = canExtractInput;
        this.performTransfer = performTransfer;
        this.readerSupplier = readerSupplier;
        this.statusUpdater = statusUpdater;
    }

    public void execute() {
        // finished
        if (ticksRun >= processor.getRecipe().getTicksToComplete()) {
            if (!drainEntropy(true)) {
                statusUpdater.accept(TileStatus.NotEnoughEntropy);
                return;
            }

            var input = processor.getRecipe().getInput();
            if (!canExtractInput.test(input)) {
                statusUpdater.accept(TileStatus.Invalid);
                return;
            }

            var outputStack = processor.getRecipe().getRecipeFunction().apply(recipeFunctionSupplier.get());
            if (!isOutputFull.test(outputStack)) {
                statusUpdater.accept(TileStatus.OutFull);
                return;
            }

            drainEntropy(false);
            performTransfer.accept(input, outputStack);
            ticksRun = 0;
        } else ticksRun++;
    }

    public double getPercentage() {
        if (processor.hasRecipe()) {
            return  (double) getTicksRun() / processor.getRecipe().getTicksToComplete();
        }
        return 0;
    }

    public int getTicksRun() {
        return ticksRun;
    }

    protected boolean drainEntropy(boolean simulate) {
        var rec = processor.getRecipe();
        if (rec instanceof EntropyRecipe entropyRecipe) {
            var data = Objects.requireNonNull(readerSupplier.get()).getEntropyView();
            return data.drainEntropy(entropyRecipe.getEntropyCost(), simulate) == entropyRecipe.getEntropyCost();
        }
        return true;
    }

    @Override public NBTTagInt serializeNBT() {
        return new NBTTagInt(ticksRun);
    }

    @Override public void deserializeNBT(NBTTagInt nbt) {
        ticksRun = nbt.getInt();
    }
}
