package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemAnvilBlock extends ItemMultiTexture
{
    private static final String __OBFID = "CL_00001764";

    public ItemAnvilBlock(Block block)
    {
        super(block, block, new String[] {"intact", "slightlyDamaged", "veryDamaged"});
    }

    public int getMetadata(int damage)
    {
        return damage << 2;
    }
}