package com.riagenic;

import com.riagenic.Commands.CmdManagerHandler;
import com.riagenic.Options.Keybinds;
import com.riagenic.Options.OptionsManager;
import com.riagenic.Utils.ChatMessenger;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Scott on 10/10/2015.
 */
public enum MossyClient {

    Mossy;
    public static final String VERSION = "1.8.1.0";
    public Keybinds keybinds;
    public ChatMessenger chat;
    public CmdManagerHandler cmdManagerHandler;
    public OptionsManager options;
    public void startClient() {
        chat = new ChatMessenger();
        keybinds = new Keybinds();
        cmdManagerHandler = new CmdManagerHandler();
        options = new OptionsManager();

        // Wire-up Forge level Events.
        MinecraftForge.EVENT_BUS.register( cmdManagerHandler);
    }

    public OptionsManager.Mods getMods() {
        return Mossy.options.mods;
    }
    public OptionsManager.Target getTargets() {
        return Mossy.options.target;
    }

}
