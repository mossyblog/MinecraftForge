package com.riagenic.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 10/9/2015.
 */
public class EntityHelper {

    private static Minecraft mc = Minecraft.getMinecraft();
    public static EntityLivingBase getClosestEntity(boolean ignoreFriends) {
        EntityLivingBase closestEntity = null;


        if (mc.theWorld == null)
            return null;
        List lst = mc.theWorld.loadedEntityList;
        if (lst == null)
            return null;

        for (Object o :  mc.theWorld.loadedEntityList)
            if (isCorrectEntity(o)) {
                EntityLivingBase en = (EntityLivingBase) o;

                if (!(o instanceof EntityPlayerSP) && !en.isDead && en.getHealth() > 0
                        && !en.getName().equals(
                        mc.thePlayer.getName())) {

                    if (closestEntity == null || mc.thePlayer.getDistanceToEntity(en) < mc.thePlayer.getDistanceToEntity(closestEntity))
                        closestEntity = en;
                }
            }
        return closestEntity;
    }
    public enum EntityType {
        BAT,
        CHICKEN,
        HORSE,
        MOOSHROOM,
        OCELOT,
        PIG,
        COW,
        RABBIT,
        SHEEP,
        SQUID,
        WOLF,
        WATERMOB,
        BLAZE,
        CAVESPIDER,
        CREEPER,
        ENDERMAN,
        ENDERMITE,
        GHAST,
        GIANTZOMBIE,
        GOLEM,
        GUARDIAN,
        IRONGOLEM,
        MAGMACUBE,
        PIGZOMBIE,
        SILVERFISH,
        SKELETON,
        SLIME,
        SNOWMAN,
        SPIDER,
        WITCH,
        ZOMBIE,
        WITHER,
        DRAGON,
        VILLAGER,
        PLAYER,
        UNKNOWN
    }
    public static EntityType getEntityType(EntityLiving entity) {

        if (!(entity instanceof EntityLiving))
            return EntityType.UNKNOWN;

        if(entity.getClass().isAssignableFrom(EntityBat.class))
            return EntityType.BAT;
        else if(entity.getClass().isAssignableFrom(EntityChicken.class))
            return EntityType.CHICKEN;
        else if(entity.getClass().isAssignableFrom(EntityHorse.class))
            return EntityType.HORSE;
        else if(entity.getClass().isAssignableFrom(EntityMooshroom.class))
            return EntityType.MOOSHROOM;
        else if(entity.getClass().isAssignableFrom(EntityOcelot.class))
            return EntityType.OCELOT;
        else if(entity.getClass().isAssignableFrom(EntityPig.class))
            return EntityType.PIG;
        else if(entity.getClass().isAssignableFrom(EntityCow.class))
            return EntityType.COW;
        else if(entity.getClass().isAssignableFrom(EntityRabbit.class))
            return EntityType.RABBIT;
        else if(entity.getClass().isAssignableFrom(EntitySheep.class))
            return EntityType.SHEEP;
        else if(entity.getClass().isAssignableFrom(EntitySquid.class))
            return EntityType.SQUID;
        else if(entity.getClass().isAssignableFrom(EntityWolf.class))
            return EntityType.WOLF;
        else if(entity.getClass().isAssignableFrom(EntityBlaze.class))
            return EntityType.BLAZE;
        else if(entity.getClass().isAssignableFrom(EntityCaveSpider.class))
            return EntityType.CAVESPIDER;
        else if(entity.getClass().isAssignableFrom(EntityCreeper.class))
            return EntityType.CREEPER;
        else if(entity.getClass().isAssignableFrom(EntityEnderman.class))
            return EntityType.ENDERMAN;
        else if(entity.getClass().isAssignableFrom(EntityEndermite.class))
            return EntityType.ENDERMITE;
        else if(entity.getClass().isAssignableFrom(EntityGiantZombie.class))
            return EntityType.GIANTZOMBIE;
        else if(entity.getClass().isAssignableFrom(EntityGolem.class))
            return EntityType.GOLEM;
        else if(entity.getClass().isAssignableFrom(EntityGuardian.class))
            return EntityType.GUARDIAN;
        else if(entity.getClass().isAssignableFrom(EntityIronGolem.class))
            return EntityType.IRONGOLEM;
        else if(entity.getClass().isAssignableFrom(EntityMagmaCube.class))
            return EntityType.MAGMACUBE;
        else if(entity.getClass().isAssignableFrom(EntitySilverfish.class))
            return EntityType.SILVERFISH;
        else if(entity.getClass().isAssignableFrom(EntityPigZombie.class) || entity.getClass().isAssignableFrom(EntityZombie.class))

            if(entity.isImmuneToFire())
                return EntityType.PIGZOMBIE;
            else
                return EntityType.ZOMBIE;


        else if(entity.getClass().isAssignableFrom(EntitySkeleton.class))
            return EntityType.SKELETON;
        else if(entity.getClass().isAssignableFrom(EntitySlime.class))
            return EntityType.SLIME;
        else if(entity.getClass().isAssignableFrom(EntitySnowman.class))
            return EntityType.SNOWMAN;
        else if(entity.getClass().isAssignableFrom(EntitySpider.class))
            return EntityType.SPIDER;
        else if(entity.getClass().isAssignableFrom(EntityWitch.class))
            return EntityType.WITCH;
        else if(entity.getClass().isAssignableFrom(EntityWither.class))
            return EntityType.WITHER;
        else if(entity.getClass().isAssignableFrom(EntityDragon.class))
            return EntityType.DRAGON;
        else if(entity.getClass().isAssignableFrom(EntityVillager.class))
            return EntityType.VILLAGER;
        else if(entity.getClass().isAssignableFrom(EntityPlayer.class))
            return EntityType.PLAYER;
        else
            return EntityType.UNKNOWN;
    }

    public static ArrayList<EntityLivingBase> getCloseEntities(boolean ignoreFriends, float range)
    {
        ArrayList<EntityLivingBase> closeEntities = new ArrayList<EntityLivingBase>();
        for(Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
            if (isCorrectEntity(o)) {
                EntityLivingBase en = (EntityLivingBase)o;
                if(!(o instanceof EntityPlayerSP) && !en.isDead && en.getHealth() > 0 && !en.getName().equals(mc.thePlayer.getName())&& mc.thePlayer.getDistanceToEntity(en) <= range)
                    closeEntities.add(en);
            }
        return closeEntities;
    }
    public static boolean lookChanged;
    public static float yaw;
    public static float pitch;

    public synchronized static void faceEntityPacket(EntityLivingBase entity)
    {
        float[] rotations = getRotationsNeeded(entity);
        if(rotations != null)
        {
            yaw = limitAngleChange( mc.thePlayer.prevRotationYaw, rotations[0], 55);// NoCheat+
            pitch = rotations[1];
            lookChanged = true;
        }
    }
    private final static float limitAngleChange(final float current,
                                                final float intended, final float maxChange)
    {
        float change = intended - current;
        if(change > maxChange)
            change = maxChange;
        else if(change < -maxChange)
            change = -maxChange;
        return current + change;
    }

    public static float[] getRotationsNeeded(Entity entity)
    {
        if(entity == null)
            return null;
        double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        double diffY;
        if(entity instanceof EntityLivingBase)
        {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            diffY =
                    entityLivingBase.posY
                            + entityLivingBase.getEyeHeight()
                            * 0.9
                            - (Minecraft.getMinecraft().thePlayer.posY + Minecraft
                            .getMinecraft().thePlayer.getEyeHeight());
        }else
            diffY =
                    (entity.getBoundingBox().minY + entity.getBoundingBox().maxY)
                            / 2.0D
                            - (Minecraft.getMinecraft().thePlayer.posY + Minecraft
                            .getMinecraft().thePlayer.getEyeHeight());
        double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw =
                (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
        return new float[]{
                Minecraft.getMinecraft().thePlayer.rotationYaw
                        + MathHelper.wrapAngleTo180_float(yaw
                        - Minecraft.getMinecraft().thePlayer.rotationYaw),
                Minecraft.getMinecraft().thePlayer.rotationPitch
                        + MathHelper.wrapAngleTo180_float(pitch
                        - Minecraft.getMinecraft().thePlayer.rotationPitch)};

    }
    public static boolean isCorrectEntity(Object o) {

        // If it aint an entity, go away.
        if (!(o instanceof Entity))
            return false;

        // Villagers, Bats, Squids, Animals etc,
        if (o instanceof EntityAgeable || o instanceof EntityAmbientCreature || o instanceof EntityWaterMob) {
            return !((Entity) o).hasCustomName() || checkName(((Entity) o).getCustomNameTag());
        }

        // Mobs
        if (o instanceof EntityMob || o instanceof EntitySlime) {
            return !((Entity) o).hasCustomName() || checkName(((Entity) o).getCustomNameTag());
        }

        // Golems
        if (o instanceof EntityGolem) {
            return !((Entity) o).hasCustomName() || checkName(((Entity) o).getCustomNameTag());
        }

        // Players
        if (o instanceof EntityPlayer)
            return (((EntityPlayer) o).isPlayerSleeping() || !((EntityPlayer) o).isPlayerSleeping())
                    && (checkName(((EntityPlayer) o).getDisplayName().getFormattedText()));

        // Invisible Entities
        if (((Entity) o).isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
            return o instanceof EntityLiving || o instanceof EntityPlayer;


        return false;
    }
    public static EntityLivingBase searchEntityByNameRaw(String name)
    {
        EntityLivingBase newEntity = null;
        for(Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
            if(isCorrectEntity(o))
            {
                EntityLivingBase en = (EntityLivingBase)o;
                if(!(o instanceof EntityPlayerSP) && !en.isDead)
                    if(newEntity == null && en.getName().equals(name))
                        newEntity = en;
            }
        return newEntity;
    }
    private static boolean checkName(String name) {
        // check colors
        String[] colors =
                {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c",
                        "d", "e", "f"};
        boolean unknownColor = true;
        for (int i = 0; i < 16; i++) {
        }
        // unknown color / no color => white
        return (unknownColor || !name.contains("§"));
    }


}
