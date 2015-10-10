package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemPiston extends ItemBlock
{
    private static final String __OBFID = "CL_00000054";

    public ItemPiston(Block block)
    {
        super(block);
    }

    public int getMetadata(int damage)
    {
        return 7;
    }
}