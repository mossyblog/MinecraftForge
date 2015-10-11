package com.riagenic.Utils;

/**
 * Created by Scott on 10/10/2015.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import scala.tools.nsc.doc.base.comment.Bold;

import java.awt.*;

import static net.minecraft.util.EnumChatFormatting.*;

public class ChatMessenger
{
    private boolean enabled = true;

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void message(String message)
    {

        StringBuilder prefix = new StringBuilder();
        prefix.append(BOLD);
        prefix.append(DARK_GRAY);
        prefix.append("MS: ");
        prefix.append(GRAY);

        if(enabled)
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(prefix.toString() + message));
    }

    public void info(String message)
    {
        message = new StringBuilder().append(BLUE)
                .append(BOLD)
                .append("[INF] ")
                .append(GRAY)
                .append(message)
                .toString();
        message(message);
    }

    public void debug(String message)
    {
        message = new StringBuilder().append(GRAY)
                .append(BOLD)
                .append("[DBG] ")
                .append(DARK_GRAY)
                .append(message)
                .toString();
        message(message);
    }

    public void warning(String message)
    {
        message = new StringBuilder().append(YELLOW)
                .append(BOLD)
                .append("[WARN] ")
                .append(WHITE)
                .append(message)
                .toString();
        message(message);
    }

    public void error(String message)
    {
        message = new StringBuilder().append(RED)
                .append(BOLD)
                .append("[ERROR] ")
                .append(WHITE)
                .append(message)
                .toString();
        message(message);
    }

    public void success(String message)
    {
        message = new StringBuilder().append(GREEN)
                .append(BOLD)
                .append("[SUCCESS] ")
                .append(WHITE)
                .append(message)
                .toString();
        message(message);
    }

    public void failure(String message)
    {
        message = new StringBuilder().append(RED)
                .append(BOLD)
                .append("[FAIL] ")
                .append(RED)
                .append(message)
                .toString();
        message(message);
    }

    public void cmd(String message)
    {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§c[§6M§c]§f §0§l[§aCMD§0§l]§f " + message));
    }
}