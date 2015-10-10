package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItemWitch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWitch extends RenderLiving
{
    private static final ResourceLocation witchTextures = new ResourceLocation("textures/entity/witch.png");
    private static final String __OBFID = "CL_00001033";

    public RenderWitch(RenderManager p_i46131_1_)
    {
        super(p_i46131_1_, new ModelWitch(0.0F), 0.5F);
        this.addLayer(new LayerHeldItemWitch(this));
    }

    public void func_180590_a(EntityWitch p_180590_1_, double p_180590_2_, double p_180590_4_, double p_180590_6_, float p_180590_8_, float p_180590_9_)
    {
        ((ModelWitch)this.mainModel).field_82900_g = p_180590_1_.getHeldItem() != null;
        super.doRender((EntityLiving)p_180590_1_, p_180590_2_, p_180590_4_, p_180590_6_, p_180590_8_, p_180590_9_);
    }

    protected ResourceLocation func_180589_a(EntityWitch p_180589_1_)
    {
        return witchTextures;
    }

    public void func_82422_c()
    {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    protected void preRenderCallback(EntityWitch p_77041_1_, float p_77041_2_)
    {
        float f1 = 0.9375F;
        GlStateManager.scale(f1, f1, f1);
    }

    public void doRender(EntityLiving entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.func_180590_a((EntityWitch)entity, x, y, z, p_76986_8_, partialTicks);
    }

    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_)
    {
        this.preRenderCallback((EntityWitch)p_77041_1_, p_77041_2_);
    }

    public void doRender(EntityLivingBase entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.func_180590_a((EntityWitch)entity, x, y, z, p_76986_8_, partialTicks);
    }

    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.func_180589_a((EntityWitch)entity);
    }

    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.func_180590_a((EntityWitch)entity, x, y, z, p_76986_8_, partialTicks);
    }
}