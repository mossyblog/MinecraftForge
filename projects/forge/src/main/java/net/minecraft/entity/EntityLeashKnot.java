package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockFence;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityLeashKnot extends EntityHanging
{
    private static final String __OBFID = "CL_00001548";

    public EntityLeashKnot(World worldIn)
    {
        super(worldIn);
    }

    public EntityLeashKnot(World worldIn, BlockPos p_i45851_2_)
    {
        super(worldIn, p_i45851_2_);
        this.setPosition((double)p_i45851_2_.getX() + 0.5D, (double)p_i45851_2_.getY() + 0.5D, (double)p_i45851_2_.getZ() + 0.5D);
        float f = 0.125F;
        float f1 = 0.1875F;
        float f2 = 0.25F;
        this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.1875D, this.posY - 0.25D + 0.125D, this.posZ - 0.1875D, this.posX + 0.1875D, this.posY + 0.25D + 0.125D, this.posZ + 0.1875D));
    }

    protected void entityInit()
    {
        super.entityInit();
    }

    public void func_174859_a(EnumFacing p_174859_1_) {}

    public int getWidthPixels()
    {
        return 9;
    }

    public int getHeightPixels()
    {
        return 9;
    }

    public float getEyeHeight()
    {
        return -0.0625F;
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance)
    {
        return distance < 1024.0D;
    }

    public void onBroken(Entity p_110128_1_) {}

    public boolean writeToNBTOptional(NBTTagCompound tagCompund)
    {
        return false;
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {}

    public void readEntityFromNBT(NBTTagCompound tagCompund) {}

    public boolean interactFirst(EntityPlayer playerIn)
    {
        ItemStack itemstack = playerIn.getHeldItem();
        boolean flag = false;
        double d0;
        List list;
        Iterator iterator;
        EntityLiving entityliving;

        if (itemstack != null && itemstack.getItem() == Items.lead && !this.worldObj.isRemote)
        {
            d0 = 7.0D;
            list = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + d0, this.posZ + d0));
            iterator = list.iterator();

            while (iterator.hasNext())
            {
                entityliving = (EntityLiving)iterator.next();

                if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == playerIn)
                {
                    entityliving.setLeashedToEntity(this, true);
                    flag = true;
                }
            }
        }

        if (!this.worldObj.isRemote && !flag)
        {
            this.setDead();

            if (playerIn.capabilities.isCreativeMode)
            {
                d0 = 7.0D;
                list = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + d0, this.posZ + d0));
                iterator = list.iterator();

                while (iterator.hasNext())
                {
                    entityliving = (EntityLiving)iterator.next();

                    if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == this)
                    {
                        entityliving.clearLeashed(true, false);
                    }
                }
            }
        }

        return true;
    }

    public boolean onValidSurface()
    {
        return this.worldObj.getBlockState(this.hangingPosition).getBlock() instanceof BlockFence;
    }

    public static EntityLeashKnot createKnot(World worldIn, BlockPos fence)
    {
        EntityLeashKnot entityleashknot = new EntityLeashKnot(worldIn, fence);
        entityleashknot.forceSpawn = true;
        worldIn.spawnEntityInWorld(entityleashknot);
        return entityleashknot;
    }

    public static EntityLeashKnot getKnotForPosition(World worldIn, BlockPos pos)
    {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        List list = worldIn.getEntitiesWithinAABB(EntityLeashKnot.class, new AxisAlignedBB((double)i - 1.0D, (double)j - 1.0D, (double)k - 1.0D, (double)i + 1.0D, (double)j + 1.0D, (double)k + 1.0D));
        Iterator iterator = list.iterator();
        EntityLeashKnot entityleashknot;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            entityleashknot = (EntityLeashKnot)iterator.next();
        }
        while (!entityleashknot.func_174857_n().equals(pos));

        return entityleashknot;
    }
}