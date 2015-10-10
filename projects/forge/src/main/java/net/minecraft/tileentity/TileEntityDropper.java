package net.minecraft.tileentity;

public class TileEntityDropper extends TileEntityDispenser
{
    private static final String __OBFID = "CL_00000353";

    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.dropper";
    }

    public String getGuiID()
    {
        return "minecraft:dropper";
    }
}