package com.riagenic.Commands;

import com.riagenic.Events.ChatOutputEvent;
import com.riagenic.MossyClient;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * Created by Scott on 10/10/2015.
 */
public class CmdManagerHandler {
    private final TreeMap<String, Cmd> cmds = new TreeMap<String, Cmd>(
            new Comparator<String>()
            {
                @Override
                public int compare(String o1, String o2)
                {
                    return o1.compareToIgnoreCase(o2);
                }
            });

    public CmdManagerHandler() {
        addCommand(new CmdDay());
        addCommand(new CmdReloadMods());
    }

    @SubscribeEvent
    public void onSentMessage(ChatOutputEvent event)
    {
        String message = event.getMessage();
        if(message.startsWith("/moss"))
        {
            event.setCanceled(true);
            String input = message.substring(6);
            String commandName = input.split(" ")[0];
            String[] args;
            if(input.contains(" "))
                args = input.substring(input.indexOf(" ") + 1).split(" ");
            else
                args = new String[0];

            Cmd cmd = getCommandByName(commandName);
            if(cmd != null)
                try
                {
                    cmd.execute(args);

                }catch(Cmd.SyntaxError e)
                {
                    if(e.getMessage() != null)
                        MossyClient.Mossy.chat.error("Syntax Error! " + e.getMessage());
                    else
                        MossyClient.Mossy.chat.error("Syntax Error!");

                    cmd.printSyntax();
                }catch(Cmd.Error e)
                {
                    MossyClient.Mossy.chat.error(e.getMessage());
                }catch(Exception e)
                {
                    MossyClient.Mossy.chat.failure(e.getMessage());
                    // MossyClient.Mossy.eventManager.handleException(e, cmd, "executing", "Exact input: `" + event.getMessage() + "`");
                }
            else
                MossyClient.Mossy.chat.failure(EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + "/" + commandName + EnumChatFormatting.RESET + " is not a valid command.");
        }
    }
    public Cmd getCommandByClass(Class<?> commandClass)
    {
        return cmds.get(commandClass.getAnnotation(Cmd.Info.class).name());
    }

    public Cmd getCommandByName(String name)
    {
        return cmds.get(name);
    }

    public Collection<Cmd> getAllCommands()
    {
        return cmds.values();
    }

    public int countCommands()
    {
        return cmds.size();
    }

    private void addCommand(Cmd commmand)
    {
        cmds.put(commmand.getName(), commmand);
    }
}
