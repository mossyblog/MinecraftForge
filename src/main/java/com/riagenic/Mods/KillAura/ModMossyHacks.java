package com.riagenic.Mods.KillAura;

import com.riagenic.UI.UITextAlignment;
import com.riagenic.UI.UIRenderer;
import com.riagenic.Utils.EntityHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.init.Items;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

import static com.riagenic.MossyClient.Mossy;

/**
 * Created by Scott on 10/9/2015.
 */
@Mod(modid = "ModMossyHacks", name = ModMossyHacks.MODID, version = ModMossyHacks.VERSION)
public class ModMossyHacks {

    public static final String MODID = "ModMossyHacks";
    public static final String VERSION = "1.0";
    public static final Logger logger = LogManager.getLogger(MODID);
    public static boolean IsEnabled = false;
    private static Minecraft mc = Minecraft.getMinecraft();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("Pre-Init");
        MinecraftForge.EVENT_BUS.register(this);

        // Register the Initial Gamma Setting prior to Loading.
        previousGammaSetting = mc.gameSettings.gammaSetting;
    }

    float ticks;
    public static EntityLivingBase entityToRender;

    float previousGammaSetting;

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        ticks = event.partialTicks;

        if(mc.theWorld != null) {
            ClosestEntityOverlay(EntityHelper.getClosestEntity(true));
        }
        Minecraft.getMinecraft().entityRenderer.enableLightmap();

        // Brightness Mod.
        if (Mossy.getMods().IsBrightnessEnabled || Mossy.getMods().IsXrayEnabled) {
            if (mc.gameSettings.gammaSetting < 16F) {
                mc.gameSettings.gammaSetting += 0.5F;
            }
        } else if (mc.gameSettings.gammaSetting > 0.5F) {
            if (mc.gameSettings.gammaSetting < mc.gameSettings.gammaSetting)
                mc.gameSettings.gammaSetting = 0.5F;
            else
                mc.gameSettings.gammaSetting -= 0.5F;
        }
    }

    private void ClosestEntityOverlay(EntityLivingBase entity) {

        // Safety first - don't assume!.
        if(entity == null)
            return;

        double dist = entity.getDistanceToEntity(mc.thePlayer);

        // Safe to Attack.
        if(dist < 255) {

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

            // Renderer
            UIRenderer.RenderFloatingText(multilineOverlayMessage, x, scaleH, z, scale, Color.white, Color.red, UITextAlignment.CENTER, true,ticks);
            UIRenderer.RenderFloatingItemIcon(x, y + entity.height - 2, z, Items.cake, ticks);
        }
    }
}
