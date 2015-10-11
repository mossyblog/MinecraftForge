package com.riagenic.Options;

/**
 * Created by Scott on 10/10/2015.
 */
public class OptionsManager {

    public Target target = new Target();
    public Mods mods = new Mods();

    public class Mods {
        public boolean IsFastBreakEnabled = true;
        public boolean IsKillAuraEnabled = true;
        public boolean IsRainEnabled = false;
        public int KillAuraRange = 9;
        public int NukeRange = 9;
        public int FastBreakSpeed = 10;
        public boolean IsXrayEnabled = false;
        public boolean IsBrightnessEnabled = false;
        public boolean IsHurtcamEnabled = true;
        public boolean IsNukeEnabled = false;
        public boolean IsHarvestEnabled = false;
        public boolean IsSurgicalNukeEnabled = false;




    }
    public class Target
    {

        public boolean players = true;
        public boolean animals = false;
        public boolean squids = true;
        public boolean monsters = true;
        public boolean villagers = true;
        public boolean villagerzombie = true;
        public boolean golems = true;
        public boolean sleeping_players = true;
        public boolean invisible_players = true;
        public boolean invisible_mobs = true;
        public boolean teams = false;
    }
}
