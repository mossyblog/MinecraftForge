package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandKill extends CommandBase
{
    private static final String __OBFID = "CL_00000570";

    public String getName()
    {
        return "kill";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.kill.usage";
    }

    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length == 0)
        {
            EntityPlayerMP entityplayermp = getCommandSenderAsPlayer(sender);
            entityplayermp.onKillCommand();
            notifyOperators(sender, this, "commands.kill.successful", new Object[] {entityplayermp.getDisplayName()});
        }
        else
        {
            Entity entity = func_175768_b(sender, args[0]);
            entity.onKillCommand();
            notifyOperators(sender, this, "commands.kill.successful", new Object[] {entity.getDisplayName()});
        }
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}