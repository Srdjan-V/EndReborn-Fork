package io.github.srdjanv.endreforked.api.entropywand;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.block.Block;

import org.jetbrains.annotations.Nullable;

import io.github.srdjanv.endreforked.EndReforked;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public final class EntropyWandHandler {

    private static final Map<Block, List<Conversion>> converionMap = new Object2ObjectOpenHashMap<>();

    public static Map<Block, List<Conversion>> getConverionMap() {
        return converionMap;
    }

    @Nullable
    public static List<Conversion> getConversions(Block block) {
        return converionMap.get(block);
    }

    public static void registerConversions(List<Block> blocks, Conversion... conversions) {
        for (var block : blocks)
            registerConversions(block, conversions);
    }

    public static void registerDefaultStateConversion(Block block, Block conversion) {
        registerConversion(block, Conversion.builder()
                .matcher(block.getDefaultState())
                .newState(conversion.getDefaultState())
                .playFlintSound().build());
    }

    public static void registerConversion(Block block, Conversion conversion) {
        registerConversions(block, conversion);
    }

    public static void registerConversions(Block block, Conversion... conversions) {
        List<Conversion> oldConversions = converionMap.get(block);
        if (oldConversions != null && !oldConversions.isEmpty()) {
            EndReforked.LOGGER.warn("{}: overwriting conversions for block {}",
                    EntropyWandHandler.class.getSimpleName(),
                    block);
            oldConversions.clear();
        } else {
            oldConversions = new ObjectArrayList<>();
            converionMap.put(block, oldConversions);
        }
        oldConversions.addAll(Arrays.asList(conversions));
    }

    public static void addConversions(List<Block> blocks, Conversion... conversions) {
        for (var block : blocks)
            addConversions(block, conversions);
    }

    public static void addConversion(Block block, Conversion conversion) {
        addConversions(block, conversion);
    }

    public static void addConversions(Block block, Conversion... conversions) {
        var currentConversions = converionMap.computeIfAbsent(block, block1 -> new ObjectArrayList<>());
        currentConversions.addAll(Arrays.asList(conversions));
    }

    public static void removeBlockConversion(Block block) {
        converionMap.remove(block);
    }

    public static void removeConversion(Block block, Conversion conversion) {
        var conversionList = converionMap.get(block);
        if (Objects.isNull(conversionList)) return;
        conversionList.remove(conversion);
    }

    public static void removeConversions(Block block, Conversion... conversions) {
        var conversionList = converionMap.get(block);
        if (Objects.isNull(conversionList)) return;
        conversionList.removeAll(Arrays.asList(conversions));
    }

    private EntropyWandHandler() {}
}
