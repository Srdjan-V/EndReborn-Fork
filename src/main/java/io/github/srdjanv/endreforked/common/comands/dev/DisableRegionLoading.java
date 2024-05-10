package io.github.srdjanv.endreforked.common.comands.dev;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class DisableRegionLoading extends CommandBase {

    @Override public String getName() {
        return "disable_region_loading";
    }

    @Override public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return Arrays.asList(Boolean.toString(true), Boolean.toString(false));
    }

    @Override public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        DevConfigs.DISABLE_REGION_LOADING.set(Boolean.parseBoolean(args[0]));
        DevConfigs.sync();
    }
}
