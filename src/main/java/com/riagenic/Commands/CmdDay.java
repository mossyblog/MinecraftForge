package com.riagenic.Commands;

import com.riagenic.MossyClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

import static com.riagenic.MossyClient.*;

/**
 * Created by Scott on 10/10/2015.
 */
@Cmd.Info(help = "Sets the time as Day (Single or Op Only)",
        name = "day",
        syntax = {"[chars|day]", ""})
public class CmdDay extends Cmd {
    @Override
    public void execute(String[] args) throws Error {

        Mossy.chat.cmd(" ** Setting the Day to 10000...");
        String message = "/time set day 100000";
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
    }
}
