package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ArmorStandRenderer extends RendererLivingEntity
{
    public static final ResourceLocation TEXTURE_ARMOR_STAND = new ResourceLocation("textures/entity/armorstand/wood.png");
    private static final String __OBFID = "CL_00002447";

    public ArmorStandRenderer(RenderManager p_i46195_1_)
    {
        super(p_i46195_1_, new ModelArmorStand(), 0.0F);
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
        {
            private static final String __OBFID = "CL_00002446";
            protected void func_177177_a()
            {
                this.field_177189_c = new ModelArmorStandArmor(0.5F);
                this.field_177186_d = new ModelArmorStandArmor(1.0F);
            }
        };
        this.addLayer(layerbipedarmor);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerCustomHead(this.func_177100_a().bipedHead));
    }

    protected ResourceLocation getArmorStandTexture(EntityArmorStand entityObj)
    {
        return TEXTURE_ARMOR_STAND;
    }

    public ModelArmorStand func_177100_a()
    {
        return (ModelArmorStand)super.getMainModel();
    }

    protected void func_177101_a(EntityArmorStand p_177101_1_, float p_177101_2_, float p_177101_3_, float p_177101_4_)
    {
        GlStateManager.rotate(180.0F - p_177101_3_, 0.0F, 1.0F, 0.0F);
    }

    protected boolean func_177099_b(EntityArmorStand p_177099_1_)
    {
        return p_177099_1_.getAlwaysRenderNameTag();
    }

    protected boolean canRenderName(EntityLivingBase targetEntity)
    {
        return this.func_177099_b((EntityArmorStand)targetEntity);
    }

    protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        this.func_177101_a((EntityArmorStand)p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }

    public ModelBase getMainModel()
    {
        return this.func_177100_a();
    }

    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getArmorStandTexture((EntityArmorStand)entity);
    }

    protected boolean canRenderName(Entity entity)
    {
        return this.func_177099_b((EntityArmorStand)entity);
    }
}