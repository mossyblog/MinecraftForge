package net.minecraft.client.renderer;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DestroyBlockProgress
{
    private final int miningPlayerEntId;
    private final BlockPos position;
    private int partialBlockProgress;
    private int createdAtCloudUpdateTick;
    private static final String __OBFID = "CL_00001427";

    public DestroyBlockProgress(int p_i45925_1_, BlockPos p_i45925_2_)
    {
        this.miningPlayerEntId = p_i45925_1_;
        this.position = p_i45925_2_;
    }

    public BlockPos getPosition()
    {
        return this.position;
    }

    public void setPartialBlockDamage(int damage)
    {
        if (damage > 10)
        {
            damage = 10;
        }

        this.partialBlockProgress = damage;
    }

    public int getPartialBlockDamage()
    {
        return this.partialBlockProgress;
    }

    public void setCloudUpdateTick(int p_82744_1_)
    {
        this.createdAtCloudUpdateTick = p_82744_1_;
    }

    public int getCreationCloudUpdateTick()
    {
        return this.createdAtCloudUpdateTick;
    }
}