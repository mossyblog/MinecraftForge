package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public abstract class GuiSlot
{
    protected final Minecraft mc;
    public int width;
    public int height;
    public int top;
    public int bottom;
    public int right;
    public int left;
    public final int slotHeight;
    private int scrollUpButtonID;
    private int scrollDownButtonID;
    protected int mouseX;
    protected int mouseY;
    protected boolean field_148163_i = true;
    protected float initialClickY = -2.0F;
    protected float scrollMultiplier;
    protected float amountScrolled;
    protected int selectedElement = -1;
    protected long lastClicked;
    protected boolean field_178041_q = true;
    protected boolean showSelectionBox = true;
    protected boolean hasListHeader;
    public int headerPadding;
    private boolean enabled = true;
    private static final String __OBFID = "CL_00000679";

    public GuiSlot(Minecraft mcIn, int width, int height, int topIn, int bottomIn, int slotHeightIn)
    {
        this.mc = mcIn;
        this.width = width;
        this.height = height;
        this.top = topIn;
        this.bottom = bottomIn;
        this.slotHeight = slotHeightIn;
        this.left = 0;
        this.right = width;
    }

    public void setDimensions(int p_148122_1_, int p_148122_2_, int p_148122_3_, int p_148122_4_)
    {
        this.width = p_148122_1_;
        this.height = p_148122_2_;
        this.top = p_148122_3_;
        this.bottom = p_148122_4_;
        this.left = 0;
        this.right = p_148122_1_;
    }

    public void setShowSelectionBox(boolean p_148130_1_)
    {
        this.showSelectionBox = p_148130_1_;
    }

    protected void setHasListHeader(boolean p_148133_1_, int p_148133_2_)
    {
        this.hasListHeader = p_148133_1_;
        this.headerPadding = p_148133_2_;

        if (!p_148133_1_)
        {
            this.headerPadding = 0;
        }
    }

    protected abstract int getSize();

    protected abstract void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY);

    protected abstract boolean isSelected(int slotIndex);

    protected int getContentHeight()
    {
        return this.getSize() * this.slotHeight + this.headerPadding;
    }

    protected abstract void drawBackground();

    protected void func_178040_a(int p_178040_1_, int p_178040_2_, int p_178040_3_) {}

    protected abstract void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_);

    protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {}

    protected void func_148132_a(int p_148132_1_, int p_148132_2_) {}

    protected void func_148142_b(int p_148142_1_, int p_148142_2_) {}

    public int getSlotIndexFromScreenCoords(int p_148124_1_, int p_148124_2_)
    {
        int k = this.left + this.width / 2 - this.getListWidth() / 2;
        int l = this.left + this.width / 2 + this.getListWidth() / 2;
        int i1 = p_148124_2_ - this.top - this.headerPadding + (int)this.amountScrolled - 4;
        int j1 = i1 / this.slotHeight;
        return p_148124_1_ < this.getScrollBarX() && p_148124_1_ >= k && p_148124_1_ <= l && j1 >= 0 && i1 >= 0 && j1 < this.getSize() ? j1 : -1;
    }

    public void registerScrollButtons(int p_148134_1_, int p_148134_2_)
    {
        this.scrollUpButtonID = p_148134_1_;
        this.scrollDownButtonID = p_148134_2_;
    }

    protected void bindAmountScrolled()
    {
        int i = this.func_148135_f();

        if (i < 0)
        {
            i /= 2;
        }

        if (!this.field_148163_i && i < 0)
        {
            i = 0;
        }

        this.amountScrolled = MathHelper.clamp_float(this.amountScrolled, 0.0F, (float)i);
    }

    public int func_148135_f()
    {
        return Math.max(0, this.getContentHeight() - (this.bottom - this.top - 4));
    }

    public int getAmountScrolled()
    {
        return (int)this.amountScrolled;
    }

    public boolean isMouseYWithinSlotBounds(int p_148141_1_)
    {
        return p_148141_1_ >= this.top && p_148141_1_ <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
    }

    public void scrollBy(int p_148145_1_)
    {
        this.amountScrolled += (float)p_148145_1_;
        this.bindAmountScrolled();
        this.initialClickY = -2.0F;
    }

    public void actionPerformed(GuiButton p_148147_1_)
    {
        if (p_148147_1_.enabled)
        {
            if (p_148147_1_.id == this.scrollUpButtonID)
            {
                this.amountScrolled -= (float)(this.slotHeight * 2 / 3);
                this.initialClickY = -2.0F;
                this.bindAmountScrolled();
            }
            else if (p_148147_1_.id == this.scrollDownButtonID)
            {
                this.amountScrolled += (float)(this.slotHeight * 2 / 3);
                this.initialClickY = -2.0F;
                this.bindAmountScrolled();
            }
        }
    }

    public void drawScreen(int mouseXIn, int mouseYIn, float p_148128_3_)
    {
        if (this.field_178041_q)
        {
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;
            this.drawBackground();
            int k = this.getScrollBarX();
            int l = k + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            this.drawContainerBackground(tessellator);
            int i1 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int j1 = this.top + 4 - (int)this.amountScrolled;

            if (this.hasListHeader)
            {
                this.drawListHeader(i1, j1, tessellator);
            }

            this.drawSelectionBox(i1, j1, mouseXIn, mouseYIn);
            GlStateManager.disableDepth();
            byte b0 = 4;
            this.overlayBackground(0, this.top, 255, 255);
            this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            worldrenderer.startDrawingQuads();
            worldrenderer.setColorRGBA_I(0, 0);
            worldrenderer.addVertexWithUV((double)this.left, (double)(this.top + b0), 0.0D, 0.0D, 1.0D);
            worldrenderer.addVertexWithUV((double)this.right, (double)(this.top + b0), 0.0D, 1.0D, 1.0D);
            worldrenderer.setColorRGBA_I(0, 255);
            worldrenderer.addVertexWithUV((double)this.right, (double)this.top, 0.0D, 1.0D, 0.0D);
            worldrenderer.addVertexWithUV((double)this.left, (double)this.top, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            worldrenderer.startDrawingQuads();
            worldrenderer.setColorRGBA_I(0, 255);
            worldrenderer.addVertexWithUV((double)this.left, (double)this.bottom, 0.0D, 0.0D, 1.0D);
            worldrenderer.addVertexWithUV((double)this.right, (double)this.bottom, 0.0D, 1.0D, 1.0D);
            worldrenderer.setColorRGBA_I(0, 0);
            worldrenderer.addVertexWithUV((double)this.right, (double)(this.bottom - b0), 0.0D, 1.0D, 0.0D);
            worldrenderer.addVertexWithUV((double)this.left, (double)(this.bottom - b0), 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            int k1 = this.func_148135_f();

            if (k1 > 0)
            {
                int l1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                l1 = MathHelper.clamp_int(l1, 32, this.bottom - this.top - 8);
                int i2 = (int)this.amountScrolled * (this.bottom - this.top - l1) / k1 + this.top;

                if (i2 < this.top)
                {
                    i2 = this.top;
                }

                worldrenderer.startDrawingQuads();
                worldrenderer.setColorRGBA_I(0, 255);
                worldrenderer.addVertexWithUV((double)k, (double)this.bottom, 0.0D, 0.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)l, (double)this.bottom, 0.0D, 1.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)l, (double)this.top, 0.0D, 1.0D, 0.0D);
                worldrenderer.addVertexWithUV((double)k, (double)this.top, 0.0D, 0.0D, 0.0D);
                tessellator.draw();
                worldrenderer.startDrawingQuads();
                worldrenderer.setColorRGBA_I(8421504, 255);
                worldrenderer.addVertexWithUV((double)k, (double)(i2 + l1), 0.0D, 0.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)l, (double)(i2 + l1), 0.0D, 1.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)l, (double)i2, 0.0D, 1.0D, 0.0D);
                worldrenderer.addVertexWithUV((double)k, (double)i2, 0.0D, 0.0D, 0.0D);
                tessellator.draw();
                worldrenderer.startDrawingQuads();
                worldrenderer.setColorRGBA_I(12632256, 255);
                worldrenderer.addVertexWithUV((double)k, (double)(i2 + l1 - 1), 0.0D, 0.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)(l - 1), (double)(i2 + l1 - 1), 0.0D, 1.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)(l - 1), (double)i2, 0.0D, 1.0D, 0.0D);
                worldrenderer.addVertexWithUV((double)k, (double)i2, 0.0D, 0.0D, 0.0D);
                tessellator.draw();
            }

            this.func_148142_b(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }

    public void handleMouseInput()
    {
        if (this.isMouseYWithinSlotBounds(this.mouseY))
        {
            if (Mouse.isButtonDown(0) && this.getEnabled())
            {
                if (this.initialClickY == -1.0F)
                {
                    boolean flag = true;

                    if (this.mouseY >= this.top && this.mouseY <= this.bottom)
                    {
                        int i = this.width / 2 - this.getListWidth() / 2;
                        int j = this.width / 2 + this.getListWidth() / 2;
                        int k = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                        int l = k / this.slotHeight;

                        if (this.mouseX >= i && this.mouseX <= j && l >= 0 && k >= 0 && l < this.getSize())
                        {
                            boolean flag1 = l == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                            this.elementClicked(l, flag1, this.mouseX, this.mouseY);
                            this.selectedElement = l;
                            this.lastClicked = Minecraft.getSystemTime();
                        }
                        else if (this.mouseX >= i && this.mouseX <= j && k < 0)
                        {
                            this.func_148132_a(this.mouseX - i, this.mouseY - this.top + (int)this.amountScrolled - 4);
                            flag = false;
                        }

                        int i2 = this.getScrollBarX();
                        int i1 = i2 + 6;

                        if (this.mouseX >= i2 && this.mouseX <= i1)
                        {
                            this.scrollMultiplier = -1.0F;
                            int j1 = this.func_148135_f();

                            if (j1 < 1)
                            {
                                j1 = 1;
                            }

                            int k1 = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getContentHeight());
                            k1 = MathHelper.clamp_int(k1, 32, this.bottom - this.top - 8);
                            this.scrollMultiplier /= (float)(this.bottom - this.top - k1) / (float)j1;
                        }
                        else
                        {
                            this.scrollMultiplier = 1.0F;
                        }

                        if (flag)
                        {
                            this.initialClickY = (float)this.mouseY;
                        }
                        else
                        {
                            this.initialClickY = -2.0F;
                        }
                    }
                    else
                    {
                        this.initialClickY = -2.0F;
                    }
                }
                else if (this.initialClickY >= 0.0F)
                {
                    this.amountScrolled -= ((float)this.mouseY - this.initialClickY) * this.scrollMultiplier;
                    this.initialClickY = (float)this.mouseY;
                }
            }
            else
            {
                this.initialClickY = -1.0F;
            }

            int l1 = Mouse.getEventDWheel();

            if (l1 != 0)
            {
                if (l1 > 0)
                {
                    l1 = -1;
                }
                else if (l1 < 0)
                {
                    l1 = 1;
                }

                this.amountScrolled += (float)(l1 * this.slotHeight / 2);
            }
        }
    }

    public void setEnabled(boolean p_148143_1_)
    {
        this.enabled = p_148143_1_;
    }

    public boolean getEnabled()
    {
        return this.enabled;
    }

    public int getListWidth()
    {
        return 220;
    }

    protected void drawSelectionBox(int p_148120_1_, int p_148120_2_, int p_148120_3_, int p_148120_4_)
    {
        int i1 = this.getSize();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        for (int j1 = 0; j1 < i1; ++j1)
        {
            int k1 = p_148120_2_ + j1 * this.slotHeight + this.headerPadding;
            int l1 = this.slotHeight - 4;

            if (k1 > this.bottom || k1 + l1 < this.top)
            {
                this.func_178040_a(j1, p_148120_1_, k1);
            }

            if (this.showSelectionBox && this.isSelected(j1))
            {
                int i2 = this.left + (this.width / 2 - this.getListWidth() / 2);
                int j2 = this.left + this.width / 2 + this.getListWidth() / 2;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableTexture2D();
                worldrenderer.startDrawingQuads();
                worldrenderer.setColorOpaque_I(8421504);
                worldrenderer.addVertexWithUV((double)i2, (double)(k1 + l1 + 2), 0.0D, 0.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)j2, (double)(k1 + l1 + 2), 0.0D, 1.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)j2, (double)(k1 - 2), 0.0D, 1.0D, 0.0D);
                worldrenderer.addVertexWithUV((double)i2, (double)(k1 - 2), 0.0D, 0.0D, 0.0D);
                worldrenderer.setColorOpaque_I(0);
                worldrenderer.addVertexWithUV((double)(i2 + 1), (double)(k1 + l1 + 1), 0.0D, 0.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)(j2 - 1), (double)(k1 + l1 + 1), 0.0D, 1.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)(j2 - 1), (double)(k1 - 1), 0.0D, 1.0D, 0.0D);
                worldrenderer.addVertexWithUV((double)(i2 + 1), (double)(k1 - 1), 0.0D, 0.0D, 0.0D);
                tessellator.draw();
                GlStateManager.enableTexture2D();
            }

            this.drawSlot(j1, p_148120_1_, k1, l1, p_148120_3_, p_148120_4_);
        }
    }

    protected int getScrollBarX()
    {
        return this.width / 2 + 124;
    }

    protected void overlayBackground(int p_148136_1_, int p_148136_2_, int p_148136_3_, int p_148136_4_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        worldrenderer.startDrawingQuads();
        worldrenderer.setColorRGBA_I(4210752, p_148136_4_);
        worldrenderer.addVertexWithUV((double)this.left, (double)p_148136_2_, 0.0D, 0.0D, (double)((float)p_148136_2_ / f));
        worldrenderer.addVertexWithUV((double)(this.left + this.width), (double)p_148136_2_, 0.0D, (double)((float)this.width / f), (double)((float)p_148136_2_ / f));
        worldrenderer.setColorRGBA_I(4210752, p_148136_3_);
        worldrenderer.addVertexWithUV((double)(this.left + this.width), (double)p_148136_1_, 0.0D, (double)((float)this.width / f), (double)((float)p_148136_1_ / f));
        worldrenderer.addVertexWithUV((double)this.left, (double)p_148136_1_, 0.0D, 0.0D, (double)((float)p_148136_1_ / f));
        tessellator.draw();
    }

    public void setSlotXBoundsFromLeft(int p_148140_1_)
    {
        this.left = p_148140_1_;
        this.right = p_148140_1_ + this.width;
    }

    public int getSlotHeight()
    {
        return this.slotHeight;
    }

    protected void drawContainerBackground(Tessellator tessellator)
    {
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f1 = 32.0F;
        worldrenderer.startDrawingQuads();
        worldrenderer.setColorOpaque_I(2105376);
        worldrenderer.addVertexWithUV((double)this.left, (double)this.bottom, 0.0D, (double)((float)this.left / f1), (double)((float)(this.bottom + (int)this.amountScrolled) / f1));
        worldrenderer.addVertexWithUV((double)this.right, (double)this.bottom, 0.0D, (double)((float)this.right / f1), (double)((float)(this.bottom + (int)this.amountScrolled) / f1));
        worldrenderer.addVertexWithUV((double)this.right, (double)this.top, 0.0D, (double)((float)this.right / f1), (double)((float)(this.top + (int)this.amountScrolled) / f1));
        worldrenderer.addVertexWithUV((double)this.left, (double)this.top, 0.0D, (double)((float)this.left / f1), (double)((float)(this.top + (int)this.amountScrolled) / f1));
        tessellator.draw();
    }
}