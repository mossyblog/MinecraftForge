package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpider extends RenderLiving
{
    private static final ResourceLocation spiderTextures = new ResourceLocation("textures/entity/spider/spider.png");
    private static final String __OBFID = "CL_00001027";

    public RenderSpider(RenderManager p_i46139_1_)
    {
        super(p_i46139_1_, new ModelSpider(), 1.0F);
        this.addLayer(new LayerSpiderEyes(this));
    }

    protected float getDeathMaxRotation(EntitySpider p_77037_1_)
    {
        return 180.0F;
    }

    protected ResourceLocation getEntityTexture(EntitySpider entity)
    {
        return spiderTextures;
    }

    protected float getDeathMaxRotation(EntityLivingBase p_77037_1_)
    {
        return this.getDeathMaxRotation((EntitySpider)p_77037_1_);
    }

    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntitySpider)entity);
    }
}