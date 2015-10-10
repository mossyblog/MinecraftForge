package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderLeashKnot extends Render
{
    private static final ResourceLocation leashKnotTextures = new ResourceLocation("textures/entity/lead_knot.png");
    private ModelLeashKnot leashKnotModel = new ModelLeashKnot();
    private static final String __OBFID = "CL_00001010";

    public RenderLeashKnot(RenderManager p_i46158_1_)
    {
        super(p_i46158_1_);
    }

    public void func_180559_a(EntityLeashKnot p_180559_1_, double p_180559_2_, double p_180559_4_, double p_180559_6_, float p_180559_8_, float p_180559_9_)
    {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.translate((float)p_180559_2_, (float)p_180559_4_, (float)p_180559_6_);
        float f2 = 0.0625F;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();
        this.bindEntityTexture(p_180559_1_);
        this.leashKnotModel.render(p_180559_1_, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f2);
        GlStateManager.popMatrix();
        super.doRender(p_180559_1_, p_180559_2_, p_180559_4_, p_180559_6_, p_180559_8_, p_180559_9_);
    }

    protected ResourceLocation getEntityTexture(EntityLeashKnot entity)
    {
        return leashKnotTextures;
    }

    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityLeashKnot)entity);
    }

    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.func_180559_a((EntityLeashKnot)entity, x, y, z, p_76986_8_, partialTicks);
    }
}