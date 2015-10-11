package com.riagenic.UI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Scott on 10/9/2015.
 */
public class UIRenderer {

    private static Minecraft mc = Minecraft.getMinecraft();

    /**
     * Renders a previously bound texture (with mc.getTextureManager().bindTexture())
     *
     * @param x
     * @param y
     * @param textureAtlasSprite
     * @param width
     * @param height
     * @param zLevel
     */
    private static void RenderTexture(int x, int y, TextureAtlasSprite textureAtlasSprite, int width, int height, double zLevel) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV((double) (x), (double) (y + height), (double) zLevel, (double) textureAtlasSprite.getMinU(), (double) textureAtlasSprite.getMaxV());
        worldrenderer.addVertexWithUV((double) (x + width), (double) (y + height), (double) zLevel, (double) textureAtlasSprite.getMaxU(), (double) textureAtlasSprite.getMaxV());
        worldrenderer.addVertexWithUV((double) (x + width), (double) (y), (double) zLevel, (double) textureAtlasSprite.getMaxU(), (double) textureAtlasSprite.getMinV());
        worldrenderer.addVertexWithUV((double) (x), (double) (y), (double) zLevel, (double) textureAtlasSprite.getMinU(), (double) textureAtlasSprite.getMinV());
        tessellator.draw();
    }

    /**
     * Renders a texture at the specified location
     * Copy/pasted static version of UI.func_175175_a()
     *
     * @param x
     * @param y
     * @param item
     * @param width
     * @param height
     */
    public static void RenderItemTexture(int x, int y, Item item, int width, int height) {
        IBakedModel iBakedModel = mc.getRenderItem().getItemModelMesher().getItemModel(new ItemStack(item));
        TextureAtlasSprite textureAtlasSprite = mc.getTextureMapBlocks().getAtlasSprite(iBakedModel.getTexture().getIconName());
        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        RenderTexture(x, y, textureAtlasSprite, width, height, 0);
    }

    public static void RenderCustomTexture(int x, int y, int u, int v, int width, int height, ResourceLocation resourceLocation, float scale) {

        x = (int) (x / scale);
        y = (int) (y / scale);
        int w = (int) (width / scale)/3;
        int h = (int) (height / scale)/3;

        GL11.glEnable(GL11.GL_BLEND);    //for a transparent texture
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(255f, 255f, 255f, 255f);    //fixes transparency issue when a InfoLine Notification is displayed

        if (resourceLocation != null)
            mc.getTextureManager().bindTexture(resourceLocation);
        mc.ingameGUI.drawScaledCustomSizeModalRect(x,y,u,v,width,height,w,h,width,height);
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * Renders an Item icon in the 3D world at the specified coordinates
     * @param item
     * @param x
     * @param y
     * @param z
     * @param partialTickTime
     */
    public static void RenderFloatingItemIcon(float x, float y, float z, Item item, float partialTickTime)
    {
        RenderManager renderManager = mc.getRenderManager();

        float playerX = (float) (mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * partialTickTime);
        float playerY = (float) (mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * partialTickTime);
        float playerZ = (float) (mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * partialTickTime);

        float dx = x-playerX;
        float dy = y-playerY;
        float dz = z-playerZ;
        float scale = 0.025f;

        GL11.glColor4f(1f, 1f, 1f, 0.75f);
        GL11.glPushMatrix();
        GL11.glTranslatef(dx, dy, dz);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        RenderItemTexture(-8, -8, item, 16, 16);

        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }


    /**
     * Renders floating text in the 3D world at a specific position.
     *
     * @param text                  The text to render
     * @param x                     X coordinate in the game world
     * @param y                     Y coordinate in the game world
     * @param z                     Z coordinate in the game world
     * @param foreground            0xRRGGBB text color
     * @param background            0xRRGGBB text color
     * @param renderBlackBackground render a pretty black border behind the text?
     * @param partialTickTime       Usually taken from RenderWorldLastEvent.partialTicks variable
     */
    public static void RenderFloatingText(String text,
                                          float x,
                                          float y,
                                          float z,
                                          Color foreground,
                                          Color background,
                                          boolean renderBlackBackground,
                                          float partialTickTime) {
        String textArray[] = {text};
        float scale = 0.05f;
        UITextAlignment align = UITextAlignment.CENTER;
        RenderFloatingText(textArray, x, y, z, scale, foreground, background, align, renderBlackBackground, partialTickTime);
    }


    /**
     * Renders floating text in the 3D world at a specific position.
     *
     * @param text                  The text to render
     * @param x                     X coordinate in the game world
     * @param y                     Y coordinate in the game world
     * @param z                     Z coordinate in the game world
     * @param foreground            0xRRGGBB text color
     * @param background            0xRRGGBB text color
     * @param renderBlackBackground render a pretty black border behind the text?
     * @param partialTickTime       Usually taken from RenderWorldLastEvent.partialTicks variable
     */
    public static void RenderFloatingText(String text,
                                          float x,
                                          float y,
                                          float z,
                                          float scale,
                                          Color foreground,
                                          Color background,
                                          UITextAlignment align,
                                          boolean renderBlackBackground,
                                          float partialTickTime) {
        String textArray[] = {text};
        RenderFloatingText(textArray, x, y, z, scale, foreground, background, align, renderBlackBackground, partialTickTime);
    }
    /**
     * Renders floating lines of text in the 3D world at a specific position.
     *
     * @param text                  The string array of text to render
     * @param x                     X coordinate in the game world
     * @param y                     Y coordinate in the game world
     * @param z                     Z coordinate in the game world
     * @param foreground            0xRRGGBB text color
     * @param background            0xRRGGBB text color
     * @param renderBlackBackground render a pretty black border behind the text?
     * @param partialTickTime       Usually taken from RenderWorldLastEvent.partialTicks variable
     */
    public static void RenderFloatingText(String[] text, float x, float y, float z, float scale, Color foreground, Color background, UITextAlignment align, boolean renderBlackBackground, float partialTickTime) {
        //Thanks to Electric-Expansion mod for the majority of this code
        //https://github.com/Alex-hawks/Electric-Expansion/blob/master/src/electricexpansion/client/render/RenderFloatingText.java

        RenderManager renderManager = mc.getRenderManager();

        float playerX = (float) (mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * partialTickTime);
        float playerY = (float) (mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * partialTickTime);
        float playerZ = (float) (mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * partialTickTime);

        float dx = x - playerX;
        float dy = y - playerY;
        float dz = z - playerZ;
        float distance = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
        int lineHeight = 10;
        int paddingH = 4;

        GL11.glColor4f(1f, 1f, 1f, 0.05f);
        GL11.glPushMatrix();
        GL11.glTranslatef(dx, dy, dz);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int textWidth = 0;
        ArrayList msgArray = new ArrayList(text.length);

        for (String thisMessage : text) {
            int thisMessageWidth = mc.fontRendererObj.getStringWidth(thisMessage);

            if (thisMessageWidth > textWidth)
                textWidth = thisMessageWidth;

            msgArray.add(thisMessageWidth);
        }





        if (renderBlackBackground) {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            worldrenderer.startDrawingQuads();
            int stringMiddle = textWidth / 2;
            GlStateManager.color(background.getRed(), background.getGreen(), background.getBlue(), 0.5F);
            worldrenderer.addVertex(-stringMiddle - 1, -1 + 0, 0.0D);
            worldrenderer.addVertex(-stringMiddle - 1, 8 + lineHeight * text.length - lineHeight+paddingH, 0.0D);
            worldrenderer.addVertex(stringMiddle + 1, 8 + lineHeight * text.length - lineHeight+paddingH, 0.0D);
            worldrenderer.addVertex(stringMiddle + 1, -1 + 0, 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        int i = 0;
        for (int n = 0; n < text.length; n++) {
            String message = text[n];
            int nMsgW = (int) msgArray.get(n);

            // Alignment = LEFT


            // Alignment = CENTER
            int txtPosX = -textWidth / 2;
            switch (align) {
                case CENTER:
                    txtPosX -= nMsgW / 2 - textWidth / 2;
                    break;

                // Alignment = RIGHT
                case RIGHT:
                    txtPosX += textWidth - nMsgW - 2;
                    break;
                default:
                    txtPosX = 1;
                    break;
            }
            mc.fontRendererObj.drawString(message, txtPosX, i * lineHeight + paddingH / 2, foreground.getRGB());
            i++;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }

    /**
     * Displays a short notification to the user. Uses the Minecraft code to display messages.
     *
     * @param message the message to be displayed
     */
    public static void DisplayNotification(String message) {
        mc.ingameGUI.setRecordPlaying(message, false);
    }


}


