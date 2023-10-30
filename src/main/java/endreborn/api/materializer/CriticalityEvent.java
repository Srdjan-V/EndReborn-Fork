package endreborn.api.materializer;

import static net.minecraft.util.EnumParticleTypes.CRIT;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import org.jetbrains.annotations.NotNull;

public final class CriticalityEvent implements Comparable<CriticalityEvent> {

    private final int chance;
    private final BlockChecker blockChecker;
    private final BlockAction blockAction;

    public static CriticalityEvent create(int chance, BlockChecker blockChecker, BlockAction blockAction) {
        return new CriticalityEvent(chance, blockChecker, blockAction);
    }

    public CriticalityEvent(int chance, BlockChecker blockChecker, BlockAction blockAction) {
        this.chance = chance;
        this.blockChecker = blockChecker;
        this.blockAction = blockAction;
    }

    public int getChance() {
        return chance;
    }

    public BlockChecker getBlockChecker() {
        return blockChecker;
    }

    public BlockAction getBlockAction() {
        return blockAction;
    }

    @Override
    public int compareTo(@NotNull CriticalityEvent o) {
        return chance - o.chance;
    }

    public static BlockChecker equalsToBlock(Block block) {
        return (worldServer, state, pos) -> state.getBlock() == block;
    }

    public static BlockChecker equalsToItemStack(ItemStack item) {
        return equalsToItem(item.getItem());
    }

    public static BlockChecker equalsToItem(Item item) {
        return (worldServer, state, pos) -> {
            var itemFromBlock = Item.getItemFromBlock(state.getBlock());
            if (itemFromBlock != Items.AIR) {
                return itemFromBlock == item;
            }
            return false;
        };
    }

    @FunctionalInterface
    public interface BlockChecker {

        boolean test(WorldServer worldServer, IBlockState state, BlockPos pos);
    }

    public static BlockAction replaceWithBlockState(IBlockState block) {
        return (worldServer, state, pos) -> worldServer.setBlockState(pos, block);
    }

    public static BlockAction replaceWithDefaultBlockState(Block block) {
        return (worldServer, state, pos) -> worldServer.setBlockState(pos, block.getDefaultState());
    }

    public static BlockAction replaceWithBlockFromItemStack(ItemStack item) {
        return replaceWithBlockFromItem(item.getItem());
    }

    public static BlockAction replaceWithBlockFromItem(Item item) {
        var blockFormItem = Block.getBlockFromItem(item);
        return replaceWithDefaultBlockState(blockFormItem);
    }

    public static BlockAction replaceFromItemStack(ItemStack item) {
        return (worldServer, state, pos) -> {
            worldServer.setBlockToAir(pos);
            Block.spawnAsEntity(worldServer, pos, item);
        };
    }

    public static BlockAction replaceFromItem(Item item) {
        return replaceFromItemStack(new ItemStack(item));
    }

    @FunctionalInterface
    public interface BlockAction {

        void run(WorldServer worldServer, IBlockState state, BlockPos pos);

        default void particles(WorldServer worldServer, IBlockState state, BlockPos pos) {
            transformBlockParticles(worldServer, CRIT, state, pos);
        }

        // TODO: 26/10/2023 test
        default void transformBlockParticles(WorldServer world, EnumParticleTypes type, IBlockState state,
                                             BlockPos pos) {
            AxisAlignedBB bounds = state.getBoundingBox(world, pos);

            for (int i = 0; i <= 6; i++) {
                double startX = pos.getX() + bounds.minX;
                double endX = pos.getX() + bounds.maxX;

                double startY = pos.getY() + bounds.minY;
                double endY = pos.getY() + bounds.maxY;

                double startZ = pos.getZ() + bounds.minZ;
                double endZ = pos.getZ() + bounds.maxZ;

                iterateBlockFace(world, type, startX, endX, startY, endY, startZ, endZ);
            }
        }

        default void iterateBlockFace(WorldServer world, EnumParticleTypes type, double fromX, double toX,
                                      double fromY, double toY, double fromZ, double toZ) {
            for (double x = fromX; x < toX; x += 0.3)
                for (double y = fromY; y < toY; y += 0.3)
                    for (double z = fromZ; z < toZ; z += 0.3)
                        world.spawnParticle(type, x, y, z,
                                1, 0, 0, 0, 0.2);
        }
    }
}
