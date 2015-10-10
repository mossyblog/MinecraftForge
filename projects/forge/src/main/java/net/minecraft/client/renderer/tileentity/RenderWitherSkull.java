package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWitherSkull extends Render
{
    private static final ResourceLocation invulnerableWitherTextures = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
    private static final ResourceLocation witherTextures = new ResourceLocation("textures/entity/wither/wither.png");
    private final ModelSkeletonHead skeletonHeadModel = new ModelSkeletonHead();
    private static final String __OBFID = "CL_00001035";

    public RenderWitherSkull(RenderManager p_i46129_1_)
    {
        super(p_i46129_1_);
    }

    private float func_82400_a(float p_82400_1_, float p_82400_2_, float p_82400_3_)
    {
        float f3;

        for (f3 = p_82400_2_ - p_82400_1_; f3 < -180.0F; f3 += 360.0F)
        {
            ;
        }

        while (f3 >= 180.0F)
        {
            f3 -= 360.0F;
        }

        return p_82400_1_ + p_82400_3_ * f3;
    }

    public void doRender(EntityWitherSkull entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        float f2 = this.func_82400_a(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
        float f3 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        GlStateManager.translate((float)x, (float)y, (float)z);
        float f4 = 0.0625F;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();
        this.bindEntityTexture(entity);
        this.skeletonHeadModel.render(entity, 0.0F, 0.0F, 0.0F, f2, f3, f4);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
    }

    protected ResourceLocation func_180564_a(EntityWitherSkull p_180564_1_)
    {
        return p_180564_1_.isInvulnerable() ? invulnerableWitherTextures : witherTextures;
    }

    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.func_180564_a((EntityWitherSkull)entity);
    }

    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.doRender((EntityWitherSkull)entity, x, y, z, p_76986_8_, partialTicks);
    }
}