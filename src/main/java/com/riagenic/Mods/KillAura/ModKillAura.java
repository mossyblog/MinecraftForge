package com.riagenic.Mods.KillAura;

import com.riagenic.Events.EventManager;
import com.riagenic.UI.UITextAlignment;
import com.riagenic.UI.UIRenderer;
import com.riagenic.Utils.EntityHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Items;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Scott on 10/9/2015.
 */
@Mod(modid = "ModKillAura", name = ModKillAura.MODID, version = ModKillAura.VERSION)
public class ModKillAura {

    public static final String MODID = "ModKillAura";
    public static final String VERSION = "1.0";
    public static final Logger logger = LogManager.getLogger(MODID);
    private static Minecraft mc = Minecraft.getMinecraft();
    public EventManager eventManager;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("Pre-Init");
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        eventManager = new EventManager();
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ServerTickEvent event) {
    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        ticks = event.partialTicks;
        if(mc.theWorld != null) {
            for(int i = 0; i < Math.min(EntityHelper.getCloseEntities(true, 6F).size(), 64); i++)
            {
                KillEntity(EntityHelper.getClosestEntity(true));
            }
        }
    }

    float ticks;

    private void KillEntity(EntityLivingBase entity) {

        // Safety first - don't assume!.
        if(entity == null)
            return;

        double entityRangeMax = 55.0D;
        double entityRange = mc.thePlayer.getDistanceSqToEntity(entity);

        // Safe to Attack.
        if(entity.getDistanceToEntity(mc.thePlayer) < 6) {

            // First Attack at safe range.
            mc.playerController.attackEntity(Minecraft.getMinecraft().thePlayer, entity);
            EntityHelper.faceEntityPacket(entity);

            // Now Decorate as we attack.
            float x = (float) entity.posX;
            float y = (float) entity.posY+ 1;
            float z = (float) entity.posZ;

            ArrayList multilineOverlayArrayList = new ArrayList(4);
            multilineOverlayArrayList.add(entity.getName());
            multilineOverlayArrayList.add(entity.getMaxHealth() + " / " + entity.getHealth());
            multilineOverlayArrayList.add("DIST: " + Math.floor(entity.getDistanceToEntity(mc.thePlayer)));
            String[] multilineOverlayMessage = new String[1];
            multilineOverlayMessage = (String[]) multilineOverlayArrayList.toArray(multilineOverlayMessage);

            // Renderer
            UIRenderer.RenderFloatingText(multilineOverlayMessage, x, y+entity.height, z, 0.010f, Color.white, Color.red, UITextAlignment.CENTER, true,ticks);
            UIRenderer.RenderFloatingItemIcon(x, y + entity.height - 2, z, Items.diamond_sword, ticks);
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {



        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            int scaleF = event.resolution.getScaleFactor();
            ResourceLocation icon = new ResourceLocation("riagenic:textures/icon_killaura_48x48.png");

            String msg = "FooFOO";

            BlockPos playerpos = mc.thePlayer.getPosition();
            int x = playerpos.getX();
            int y = playerpos.getY();
            int z = playerpos.getZ()+20;

        }
    }
    // EntityHorse animal = (EntityHorse) mc.objectMouseOver.entityHit;
//        MovingObjectPosition rayPos = mc.getRenderViewEntity().rayTrace(255,1);
    //int blockId = Block.get(objectMouseOver.blockX, objectMouseOver.blockY, objectMouseOver.blockZ);
    //;Chunk chnk = mc.theWorld.getChunkFromBlockCoords(rayPos.getBlockPos());
    // System.out.println(mc.objectMouseOver.entityHit);
    // Render the Texture
//            UIRenderer.RenderItemTexture(100, 200, Item.getByNameOrId("diamond_pickaxe"), 16, 16);
//            UIRenderer.RenderCustomTexture(1, 1, 0, 0, 148, 148, icon,scaleF);
//            UIRenderer.RenderFloatingText(msg,x,y,z, Color.RED.getRGB(),true,event.partialTicks);
//            UIRenderer.RenderFloatingItemIcon(playerpos.getX()-10,playerpos.getY(),playerpos.getZ()-10,Item.getByNameOrId("diamond_pickaxe"),event.partialTicks);
//    UIRenderer.RenderFloatingItemIcon(x, y + entity.height, z, Items.apple, event.partialTicks);
}
