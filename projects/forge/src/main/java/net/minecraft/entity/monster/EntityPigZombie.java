package net.minecraft.entity.monster;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityPigZombie extends EntityZombie
{
    private static final UUID field_110189_bq = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
    private static final AttributeModifier field_110190_br = (new AttributeModifier(field_110189_bq, "Attacking speed boost", 0.05D, 0)).setSaved(false);
    private int angerLevel;
    private int randomSoundDelay;
    private UUID field_175459_bn;
    private static final String __OBFID = "CL_00001693";

    public EntityPigZombie(World worldIn)
    {
        super(worldIn);
        this.isImmuneToFire = true;
    }

    public void setRevengeTarget(EntityLivingBase livingBase)
    {
        super.setRevengeTarget(livingBase);

        if (livingBase != null)
        {
            this.field_175459_bn = livingBase.getUniqueID();
        }
    }

    protected void applyEntityAI()
    {
        this.targetTasks.addTask(1, new EntityPigZombie.AIHurtByAggressor());
        this.targetTasks.addTask(2, new EntityPigZombie.AITargetAggressor());
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(reinforcementChance).setBaseValue(0.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D);
    }

    public void onUpdate()
    {
        super.onUpdate();
    }

    protected void updateAITasks()
    {
        IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);

        if (this.isAngry())
        {
            if (!this.isChild() && !iattributeinstance.func_180374_a(field_110190_br))
            {
                iattributeinstance.applyModifier(field_110190_br);
            }

            --this.angerLevel;
        }
        else if (iattributeinstance.func_180374_a(field_110190_br))
        {
            iattributeinstance.removeModifier(field_110190_br);
        }

        if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0)
        {
            this.playSound("mob.zombiepig.zpigangry", this.getSoundVolume() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }

        if (this.angerLevel > 0 && this.field_175459_bn != null && this.getAITarget() == null)
        {
            EntityPlayer entityplayer = this.worldObj.getPlayerEntityByUUID(this.field_175459_bn);
            this.setRevengeTarget(entityplayer);
            this.attackingPlayer = entityplayer;
            this.recentlyHit = this.getRevengeTimer();
        }

        super.updateAITasks();
    }

    public boolean getCanSpawnHere()
    {
        return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    public boolean handleLavaMovement()
    {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setShort("Anger", (short)this.angerLevel);

        if (this.field_175459_bn != null)
        {
            tagCompound.setString("HurtBy", this.field_175459_bn.toString());
        }
        else
        {
            tagCompound.setString("HurtBy", "");
        }
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.angerLevel = tagCompund.getShort("Anger");
        String s = tagCompund.getString("HurtBy");

        if (s.length() > 0)
        {
            this.field_175459_bn = UUID.fromString(s);
            EntityPlayer entityplayer = this.worldObj.getPlayerEntityByUUID(this.field_175459_bn);
            this.setRevengeTarget(entityplayer);

            if (entityplayer != null)
            {
                this.attackingPlayer = entityplayer;
                this.recentlyHit = this.getRevengeTimer();
            }
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {

        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            Entity entity = source.getEntity();

            if (entity instanceof EntityPlayer)
            {
                this.becomeAngryAt(entity);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    private void becomeAngryAt(Entity p_70835_1_)
    {
        this.angerLevel = 400 + this.rand.nextInt(400);
        this.randomSoundDelay = this.rand.nextInt(40);

        if (p_70835_1_ instanceof EntityLivingBase)
        {
            this.setRevengeTarget((EntityLivingBase)p_70835_1_);
        }
    }

    public boolean isAngry()
    {
        return this.angerLevel > 0;
    }

    protected String getLivingSound()
    {
        return "mob.zombiepig.zpig";
    }

    protected String getHurtSound()
    {
        return "mob.zombiepig.zpighurt";
    }

    protected String getDeathSound()
    {
        return "mob.zombiepig.zpigdeath";
    }

    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        int j = this.rand.nextInt(2 + p_70628_2_);
        int k;

        for (k = 0; k < j; ++k)
        {
            this.dropItem(Items.rotten_flesh, 1);
        }

        j = this.rand.nextInt(2 + p_70628_2_);

        for (k = 0; k < j; ++k)
        {
            this.dropItem(Items.gold_nugget, 1);
        }
    }

    public boolean interact(EntityPlayer player)
    {
        return false;
    }

    protected void addRandomArmor()
    {
        this.dropItem(Items.gold_ingot, 1);
    }

    protected void func_180481_a(DifficultyInstance p_180481_1_)
    {
        this.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
    }

    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_)
    {
        super.func_180482_a(p_180482_1_, p_180482_2_);
        this.setVillager(false);
        return p_180482_2_;
    }

    class AIHurtByAggressor extends EntityAIHurtByTarget
    {
        private static final String __OBFID = "CL_00002206";

        public AIHurtByAggressor()
        {
            super(EntityPigZombie.this, true, new Class[0]);
        }

        protected void func_179446_a(EntityCreature p_179446_1_, EntityLivingBase p_179446_2_)
        {
            super.func_179446_a(p_179446_1_, p_179446_2_);

            if (p_179446_1_ instanceof EntityPigZombie)
            {
                ((EntityPigZombie)p_179446_1_).becomeAngryAt(p_179446_2_);
            }
        }
    }

    class AITargetAggressor extends EntityAINearestAttackableTarget
    {
        private static final String __OBFID = "CL_00002207";

        public AITargetAggressor()
        {
            super(EntityPigZombie.this, EntityPlayer.class, true);
        }

        public boolean shouldExecute()
        {
            return ((EntityPigZombie)this.taskOwner).isAngry() && super.shouldExecute();
        }
    }
}