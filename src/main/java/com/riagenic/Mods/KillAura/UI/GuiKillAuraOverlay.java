package com.riagenic.Mods.KillAura.UI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Scott on 10/9/2015.
 */
public class GuiKillAuraOverlay extends Gui {

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {

        Minecraft mc = Minecraft.getMinecraft();
        mc.fontRendererObj.drawString("worked", 20, 20, 0xFF,true);


        if(!event.isCancelable() && event.type == RenderGameOverlayEvent.ElementType.EXPERIENCE) {


            if(!mc.thePlayer.capabilities.isCreativeMode) {
                int posX = event.resolution.getScaledWidth() / 2 + 10;
                int posY = event.resolution.getScaledHeight() - 48;

                // Render the Texture
                mc.renderEngine.bindTexture( new ResourceLocation("riagenic:textures/killaura_overlay_horiz.jpg"));

                // Tattoo the Texutre.
                mc.ingameGUI.drawTexturedModalRect(posX, posY, 0, 0, 48, 48);
            }
        }
    }
}
