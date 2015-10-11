package com.riagenic.Options;

/**
 * Created by Scott on 10/10/2015.
 */
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.TreeMap;

public class Keybinds extends TreeMap<String, String>
{
    public Keybinds()
    {
        put("B", "/moss day 2000");
        put("CTRL+R", "/moss ReloadMods");
//        put("C", ".t fullbright");
//        put("F", ".t fastplace");
//        put("G", ".t flight");
//        put("GRAVE", ".t speednuker");
//        put("H", ".t /home");
//        put("J", ".t phase");
//        put("K", ".t multiaura");
//        put("L", ".t nuker");
//        put("LCONTROL", ".t clickgui");
//        put("R", ".t killaura");
//        put("U", ".t freecam");
//        put("X", ".t x-ray");
//        put("Z", ".t sneak");
    }

    private static final int shift1 = 42;
    private static final int shift2 = 54;
    private static final int ctrl1 = Minecraft.isRunningOnMac ? 219 : 29;
    private static final int ctrl2 = Minecraft.isRunningOnMac ? 220 : 157;
    private static final int alt1 = 56;
    private static final int alt2 = 184;

    public static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(Keybinds.shift1) || Keyboard.isKeyDown(Keybinds.shift2);
    }

    public static boolean isCtrlKeyDown() {
        return Keyboard.isKeyDown(Keybinds.ctrl1) || Keyboard.isKeyDown(Keybinds.ctrl2);
    }

    public static boolean isAltKeyDown() {
        return Keyboard.isKeyDown(Keybinds.alt1) || Keyboard.isKeyDown(Keybinds.alt2);
    }

    public static boolean isShiftKey(int keyCode) {
        return keyCode == Keybinds.shift1 || keyCode == Keybinds.shift2;
    }

    public static boolean isCtrlKey(int keyCode) {
        return keyCode == Keybinds.ctrl1 || keyCode == Keybinds.ctrl2;
    }

    public static boolean isAltKey(int keyCode) {
        return keyCode == Keybinds.alt1 || keyCode == Keybinds.alt2;
    }

    public static boolean isSpecialKey(int keyCode) {
        return Keybinds.isShiftKey(keyCode) || Keybinds.isCtrlKey(keyCode) || Keybinds.isAltKey(keyCode);
    }

    public static boolean isKeyPressed_KeyBoard(KeyBinding keyBinding) {
        return keyBinding.getKeyCode() >= 0 ? Keyboard.isKeyDown(keyBinding.getKeyCode()) : Mouse.isButtonDown(keyBinding.getKeyCode() + 100);
    }

    public static boolean isSpecialKeyBindingPressed(KeyBinding keyBinding, boolean[] alts) {
        return Keybinds.isKeyPressed_KeyBoard(keyBinding) &&
                (alts[0] ? Keybinds.isShiftKeyDown() : !Keybinds.isShiftKeyDown()) &&
                (alts[1] ? Keybinds.isCtrlKeyDown() : !Keybinds.isCtrlKeyDown()) &&
                (alts[2] ? Keybinds.isAltKeyDown() : !Keybinds.isAltKeyDown());
    }
}