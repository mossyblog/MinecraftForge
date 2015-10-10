package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;

public interface ICommand extends Comparable
{
    String getName();

    String getCommandUsage(ICommandSender sender);

    List getAliases();

    void execute(ICommandSender sender, String[] args) throws CommandException;

    boolean canCommandSenderUse(ICommandSender sender);

    List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos);

    boolean isUsernameIndex(String[] args, int index);
}