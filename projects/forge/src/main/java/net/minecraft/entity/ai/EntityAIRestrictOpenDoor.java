package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIRestrictOpenDoor extends EntityAIBase
{
    private EntityCreature entityObj;
    private VillageDoorInfo frontDoor;
    private static final String __OBFID = "CL_00001610";

    public EntityAIRestrictOpenDoor(EntityCreature p_i1651_1_)
    {
        this.entityObj = p_i1651_1_;

        if (!(p_i1651_1_.getNavigator() instanceof PathNavigateGround))
        {
            throw new IllegalArgumentException("Unsupported mob type for RestrictOpenDoorGoal");
        }
    }

    public boolean shouldExecute()
    {
        if (this.entityObj.worldObj.isDaytime())
        {
            return false;
        }
        else
        {
            BlockPos blockpos = new BlockPos(this.entityObj);
            Village village = this.entityObj.worldObj.getVillageCollection().getNearestVillage(blockpos, 16);

            if (village == null)
            {
                return false;
            }
            else
            {
                this.frontDoor = village.getNearestDoor(blockpos);
                return this.frontDoor == null ? false : (double)this.frontDoor.getDistanceToInsideBlockSq(blockpos) < 2.25D;
            }
        }
    }

    public boolean continueExecuting()
    {
        return this.entityObj.worldObj.isDaytime() ? false : !this.frontDoor.func_179851_i() && this.frontDoor.func_179850_c(new BlockPos(this.entityObj));
    }

    public void startExecuting()
    {
        ((PathNavigateGround)this.entityObj.getNavigator()).func_179688_b(false);
        ((PathNavigateGround)this.entityObj.getNavigator()).func_179691_c(false);
    }

    public void resetTask()
    {
        ((PathNavigateGround)this.entityObj.getNavigator()).func_179688_b(true);
        ((PathNavigateGround)this.entityObj.getNavigator()).func_179691_c(true);
        this.frontDoor = null;
    }

    public void updateTask()
    {
        this.frontDoor.incrementDoorOpeningRestrictionCounter();
    }
}