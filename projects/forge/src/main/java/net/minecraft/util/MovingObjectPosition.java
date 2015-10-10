package net.minecraft.util;

import net.minecraft.entity.Entity;

public class MovingObjectPosition
{
    private BlockPos blockPos;
    public MovingObjectPosition.MovingObjectType typeOfHit;
    public EnumFacing sideHit;
    public Vec3 hitVec;
    public Entity entityHit;
    private static final String __OBFID = "CL_00000610";

    /** Used to determine what sub-segment is hit */
    public int subHit = -1;

    /** Used to add extra hit info */
    public Object hitInfo = null;

    public MovingObjectPosition(Vec3 p_i45551_1_, EnumFacing p_i45551_2_, BlockPos p_i45551_3_)
    {
        this(MovingObjectPosition.MovingObjectType.BLOCK, p_i45551_1_, p_i45551_2_, p_i45551_3_);
    }

    public MovingObjectPosition(Vec3 p_i45552_1_, EnumFacing p_i45552_2_)
    {
        this(MovingObjectPosition.MovingObjectType.BLOCK, p_i45552_1_, p_i45552_2_, BlockPos.ORIGIN);
    }

    public MovingObjectPosition(Entity p_i2304_1_)
    {
        this(p_i2304_1_, new Vec3(p_i2304_1_.posX, p_i2304_1_.posY, p_i2304_1_.posZ));
    }

    public MovingObjectPosition(MovingObjectPosition.MovingObjectType p_i45553_1_, Vec3 p_i45553_2_, EnumFacing p_i45553_3_, BlockPos p_i45553_4_)
    {
        this.typeOfHit = p_i45553_1_;
        this.blockPos = p_i45553_4_;
        this.sideHit = p_i45553_3_;
        this.hitVec = new Vec3(p_i45553_2_.xCoord, p_i45553_2_.yCoord, p_i45553_2_.zCoord);
    }

    public MovingObjectPosition(Entity p_i45482_1_, Vec3 p_i45482_2_)
    {
        this.typeOfHit = MovingObjectPosition.MovingObjectType.ENTITY;
        this.entityHit = p_i45482_1_;
        this.hitVec = p_i45482_2_;
    }

    public BlockPos getBlockPos()
    {
        return this.blockPos;
    }

    public String toString()
    {
        return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.blockPos + ", f=" + this.sideHit + ", pos=" + this.hitVec + ", entity=" + this.entityHit + '}';
    }

    public static enum MovingObjectType
    {
        MISS,
        BLOCK,
        ENTITY;

        private static final String __OBFID = "CL_00000611";
    }
}