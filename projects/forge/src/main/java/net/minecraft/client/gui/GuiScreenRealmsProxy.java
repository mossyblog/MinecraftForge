package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiScreenRealmsProxy extends GuiScreen
{
    private RealmsScreen field_154330_a;
    private static final String __OBFID = "CL_00001847";

    public GuiScreenRealmsProxy(RealmsScreen p_i1087_1_)
    {
        this.field_154330_a = p_i1087_1_;
        super.buttonList = Collections.synchronizedList(Lists.newArrayList());
    }

    public RealmsScreen func_154321_a()
    {
        return this.field_154330_a;
    }

    public void initGui()
    {
        this.field_154330_a.init();
        super.initGui();
    }

    public void func_154325_a(String p_154325_1_, int p_154325_2_, int p_154325_3_, int p_154325_4_)
    {
        super.drawCenteredString(this.fontRendererObj, p_154325_1_, p_154325_2_, p_154325_3_, p_154325_4_);
    }

    public void func_154322_b(String p_154322_1_, int p_154322_2_, int p_154322_3_, int p_154322_4_)
    {
        super.drawString(this.fontRendererObj, p_154322_1_, p_154322_2_, p_154322_3_, p_154322_4_);
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        this.field_154330_a.blit(x, y, textureX, textureY, width, height);
        super.drawTexturedModalRect(x, y, textureX, textureY, width, height);
    }

    public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
    {
        super.drawGradientRect(left, top, right, bottom, startColor, endColor);
    }

    public void drawDefaultBackground()
    {
        super.drawDefaultBackground();
    }

    public boolean doesGuiPauseGame()
    {
        return super.doesGuiPauseGame();
    }

    public void drawWorldBackground(int tint)
    {
        super.drawWorldBackground(tint);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.field_154330_a.render(mouseX, mouseY, partialTicks);
    }

    public void renderToolTip(ItemStack stack, int x, int y)
    {
        super.renderToolTip(stack, x, y);
    }

    public void drawCreativeTabHoveringText(String tabName, int mouseX, int mouseY)
    {
        super.drawCreativeTabHoveringText(tabName, mouseX, mouseY);
    }

    public void drawHoveringText(List textLines, int x, int y)
    {
        super.drawHoveringText(textLines, x, y);
    }

    public void updateScreen()
    {
        this.field_154330_a.tick();
        super.updateScreen();
    }

    public int func_154329_h()
    {
        return this.fontRendererObj.FONT_HEIGHT;
    }

    public int func_154326_c(String p_154326_1_)
    {
        return this.fontRendererObj.getStringWidth(p_154326_1_);
    }

    public void func_154319_c(String p_154319_1_, int p_154319_2_, int p_154319_3_, int p_154319_4_)
    {
        this.fontRendererObj.drawStringWithShadow(p_154319_1_, (float)p_154319_2_, (float)p_154319_3_, p_154319_4_);
    }

    public List func_154323_a(String p_154323_1_, int p_154323_2_)
    {
        return this.fontRendererObj.listFormattedStringToWidth(p_154323_1_, p_154323_2_);
    }

    public final void actionPerformed(GuiButton button) throws IOException
    {
        this.field_154330_a.buttonClicked(((GuiButtonRealmsProxy)button).getRealmsButton());
    }

    public void func_154324_i()
    {
        super.buttonList.clear();
    }

    public void func_154327_a(RealmsButton p_154327_1_)
    {
        super.buttonList.add(p_154327_1_.getProxy());
    }

    public List func_154320_j()
    {
        ArrayList arraylist = Lists.newArrayListWithExpectedSize(super.buttonList.size());
        Iterator iterator = super.buttonList.iterator();

        while (iterator.hasNext())
        {
            GuiButton guibutton = (GuiButton)iterator.next();
            arraylist.add(((GuiButtonRealmsProxy)guibutton).getRealmsButton());
        }

        return arraylist;
    }

    public void func_154328_b(RealmsButton p_154328_1_)
    {
        super.buttonList.remove(p_154328_1_);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        this.field_154330_a.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void handleMouseInput() throws IOException
    {
        this.field_154330_a.mouseEvent();
        super.handleMouseInput();
    }

    public void handleKeyboardInput() throws IOException
    {
        this.field_154330_a.keyboardEvent();
        super.handleKeyboardInput();
    }

    public void mouseReleased(int mouseX, int mouseY, int state)
    {
        this.field_154330_a.mouseReleased(mouseX, mouseY, state);
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        this.field_154330_a.mouseDragged(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException
    {
        this.field_154330_a.keyPressed(typedChar, keyCode);
    }

    public void confirmClicked(boolean result, int id)
    {
        this.field_154330_a.confirmResult(result, id);
    }

    public void onGuiClosed()
    {
        this.field_154330_a.removed();
        super.onGuiClosed();
    }
}