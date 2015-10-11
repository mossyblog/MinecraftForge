package com.riagenic.Mods.KillAura;

import com.riagenic.Events.EntityWithinRangeEvent;
import com.riagenic.MossyClient;
import com.riagenic.Options.OptionsManager;
import com.riagenic.UI.UITextAlignment;
import com.riagenic.UI.UIRenderer;
import com.riagenic.Utils.EntityHelper;
import com.riagenic.Utils.EntityHelper.EntityType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

import static com.riagenic.Utils.EntityHelper.EntityType.*;

/**
 * Created by Scott on 10/9/2015.
 */
@Mod(modid = "ModKillAura", name = ModKillAura.MODID, version = ModKillAura.VERSION)
public class ModKillAura {

    public static final String MODID = "ModKillAura";
    public static final String VERSION = "1.0";
    public static final Logger logger = LogManager.getLogger(MODID);
    public static boolean IsEnabled = false;
    private static Minecraft mc = Minecraft.getMinecraft();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("Pre-Init");
        MinecraftForge.EVENT_BUS.register(this);
    }

    float ticks;
    public static EntityLivingBase entityToRender;

    @SubscribeEvent
    public void onEntityWithinRangeEvent(EntityWithinRangeEvent event) {
        //ModKillAura.entityToRender = (EntityLivingBase) event.entity;



    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        ticks = event.partialTicks;
        if(mc.theWorld != null) {
//            for (int i = 0; i < Math.min(EntityHelper.getCloseEntities(true, 6F).size(), 64); i++) {
//                KillEntity(EntityHelper.getClosestEntity(true));
//            }
//            KillEntity(ModKillAura.entityToRender);
//            int ent1 = (int) EntityHelper.getClosestEntity(true).getDistanceToEntity(mc.thePlayer);
//            int ent2 = (int) .getDistanceToEntity(mc.thePlayer);

//            KillEntity((EntityLivingBase) mc.objectMouseOver.entityHit);
                KillEntity(EntityHelper.getClosestEntity(true));
//                KillEntity(entityToRender);
        }
        Minecraft.getMinecraft().entityRenderer.enableLightmap();
    }

    private void KillEntity(EntityLivingBase entity) {

        // Safety first - don't assume!.
        if(entity == null)
            return;
        double dist = entity.getDistanceToEntity(mc.thePlayer);
        // Safe to Attack.
        if(dist < 49) {

//            System.out.println("entityToRender" + entityToRender);
            // First Attack at safe range.
//            mc.playerController.attackEntity(Minecraft.getMinecraft().thePlayer, entity);
            EntityHelper.faceEntityPacket(entity);

            // Now Decorate as we attack.
            float x = (float) entity.posX;
            float y = (float) entity.posY+ 1;
            float z = (float) entity.posZ;

            ArrayList multilineOverlayArrayList = new ArrayList(4);
            multilineOverlayArrayList.add(entity.getName() + "(" + entity.getAge() + ")");
            multilineOverlayArrayList.add(entity.getMaxHealth() + " / " + entity.getHealth());
            multilineOverlayArrayList.add("DIST: " + Math.floor(entity.getDistanceToEntity(mc.thePlayer)));
            String[] multilineOverlayMessage = new String[1];
            multilineOverlayMessage = (String[]) multilineOverlayArrayList.toArray(multilineOverlayMessage);

            float scale = 0.010f;

            scale += dist / 600;
            float scaleH = (y+entity.height)+1;
            //scaleH -= dist / 8;

            // Renderer
            UIRenderer.RenderFloatingText(multilineOverlayMessage, x, scaleH, z, scale, Color.white, Color.red, UITextAlignment.CENTER, true,ticks);
            UIRenderer.RenderFloatingItemIcon(x, y + entity.height - 2, z, Items.cake, ticks);
        }
    }
}
