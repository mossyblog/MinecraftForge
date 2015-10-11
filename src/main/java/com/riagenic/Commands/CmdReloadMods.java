package com.riagenic.Commands;

import com.riagenic.MossyClient;

import static com.riagenic.MossyClient.*;

/**
 * Created by Scott on 10/11/2015.
 */
@Cmd.Info(help = "Reloads the Mods)",
        name = "ReloadMods",
        syntax = {"[chars|ReloadMods]", ""})
public class CmdReloadMods extends Cmd {

    @Override
    public void execute(String[] args) throws Error {

        Mossy.chat.cmd(" ** Reloading Mods... hope this works bro...");
        Mossy.reloadMods();

    }
}
