package net.minecraft.item;

import net.minecraft.util.EnumChatFormatting;

public enum EnumRarity
{
    COMMON(EnumChatFormatting.WHITE, "Common"),
    UNCOMMON(EnumChatFormatting.YELLOW, "Uncommon"),
    RARE(EnumChatFormatting.AQUA, "Rare"),
    EPIC(EnumChatFormatting.LIGHT_PURPLE, "Epic");
    public final EnumChatFormatting rarityColor;
    public final String rarityName;

    private static final String __OBFID = "CL_00000056";

    private EnumRarity(EnumChatFormatting color, String name)
    {
        this.rarityColor = color;
        this.rarityName = name;
    }
}