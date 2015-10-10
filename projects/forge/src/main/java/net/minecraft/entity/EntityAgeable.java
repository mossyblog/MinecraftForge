package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAgeable extends EntityCreature
{
    protected int field_175504_a;
    protected int field_175502_b;
    protected int field_175503_c;
    private float field_98056_d = -1.0F;
    private float field_98057_e;
    private static final String __OBFID = "CL_00001530";

    public EntityAgeable(World worldIn)
    {
        super(worldIn);
    }

    public abstract EntityAgeable createChild(EntityAgeable ageable);

    public boolean interact(EntityPlayer player)
    {
        ItemStack itemstack = player.inventory.getCurrentItem();

        if (itemstack != null && itemstack.getItem() == Items.spawn_egg)
        {
            if (!this.worldObj.isRemote)
            {
                Class oclass = EntityList.getClassFromID(itemstack.getMetadata());

                if (oclass != null && this.getClass() == oclass)
                {
                    EntityAgeable entityageable = this.createChild(this);

                    if (entityageable != null)
                    {
                        entityageable.setGrowingAge(-24000);
                        entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
                        this.worldObj.spawnEntityInWorld(entityageable);

                        if (itemstack.hasDisplayName())
                        {
                            entityageable.setCustomNameTag(itemstack.getDisplayName());
                        }

                        if (!player.capabilities.isCreativeMode)
                        {
                            --itemstack.stackSize;

                            if (itemstack.stackSize <= 0)
                            {
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                            }
                        }
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(12, Byte.valueOf((byte)0));
    }

    public int getGrowingAge()
    {
        return this.worldObj.isRemote ? this.dataWatcher.getWatchableObjectByte(12) : this.field_175504_a;
    }

    public void func_175501_a(int p_175501_1_, boolean p_175501_2_)
    {
        int j = this.getGrowingAge();
        int k = j;
        j += p_175501_1_ * 20;

        if (j > 0)
        {
            j = 0;

            if (k < 0)
            {
                this.func_175500_n();
            }
        }

        int l = j - k;
        this.setGrowingAge(j);

        if (p_175501_2_)
        {
            this.field_175502_b += l;

            if (this.field_175503_c == 0)
            {
                this.field_175503_c = 40;
            }
        }

        if (this.getGrowingAge() == 0)
        {
            this.setGrowingAge(this.field_175502_b);
        }
    }

    public void addGrowth(int p_110195_1_)
    {
        this.func_175501_a(p_110195_1_, false);
    }

    public void setGrowingAge(int p_70873_1_)
    {
        this.dataWatcher.updateObject(12, Byte.valueOf((byte)MathHelper.clamp_int(p_70873_1_, -1, 1)));
        this.field_175504_a = p_70873_1_;
        this.setScaleForAge(this.isChild());
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Age", this.getGrowingAge());
        tagCompound.setInteger("ForcedAge", this.field_175502_b);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.setGrowingAge(tagCompund.getInteger("Age"));
        this.field_175502_b = tagCompund.getInteger("ForcedAge");
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.worldObj.isRemote)
        {
            if (this.field_175503_c > 0)
            {
                if (this.field_175503_c % 4 == 0)
                {
                    this.worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, 0.0D, 0.0D, 0.0D, new int[0]);
                }

                --this.field_175503_c;
            }

            this.setScaleForAge(this.isChild());
        }
        else
        {
            int i = this.getGrowingAge();

            if (i < 0)
            {
                ++i;
                this.setGrowingAge(i);

                if (i == 0)
                {
                    this.func_175500_n();
                }
            }
            else if (i > 0)
            {
                --i;
                this.setGrowingAge(i);
            }
        }
    }

    protected void func_175500_n() {}

    public boolean isChild()
    {
        return this.getGrowingAge() < 0;
    }

    public void setScaleForAge(boolean p_98054_1_)
    {
        this.setScale(p_98054_1_ ? 0.5F : 1.0F);
    }

    protected final void setSize(float width, float height)
    {
        boolean flag = this.field_98056_d > 0.0F;
        this.field_98056_d = width;
        this.field_98057_e = height;

        if (!flag)
        {
            this.setScale(1.0F);
        }
    }

    protected final void setScale(float p_98055_1_)
    {
        super.setSize(this.field_98056_d * p_98055_1_, this.field_98057_e * p_98055_1_);
    }
}