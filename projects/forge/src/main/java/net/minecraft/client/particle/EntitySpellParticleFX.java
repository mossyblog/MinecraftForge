package net.minecraft.client.particle;

import java.util.Random;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntitySpellParticleFX extends EntityFX
{
    private static final Random field_174848_a = new Random();
    private int baseSpellTextureIndex = 128;
    private static final String __OBFID = "CL_00000926";

    protected EntitySpellParticleFX(World worldIn, double p_i1229_2_, double p_i1229_4_, double p_i1229_6_, double p_i1229_8_, double p_i1229_10_, double p_i1229_12_)
    {
        super(worldIn, p_i1229_2_, p_i1229_4_, p_i1229_6_, 0.5D - field_174848_a.nextDouble(), p_i1229_10_, 0.5D - field_174848_a.nextDouble());
        this.motionY *= 0.20000000298023224D;

        if (p_i1229_8_ == 0.0D && p_i1229_12_ == 0.0D)
        {
            this.motionX *= 0.10000000149011612D;
            this.motionZ *= 0.10000000149011612D;
        }

        this.particleScale *= 0.75F;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.noClip = false;
    }

    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        float f6 = ((float)this.particleAge + p_180434_3_) / (float)this.particleMaxAge * 32.0F;
        f6 = MathHelper.clamp_float(f6, 0.0F, 1.0F);
        super.func_180434_a(p_180434_1_, p_180434_2_, p_180434_3_, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        this.setParticleTextureIndex(this.baseSpellTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
        this.motionY += 0.004D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    public void setBaseSpellTextureIndex(int p_70589_1_)
    {
        this.baseSpellTextureIndex = p_70589_1_;
    }

    @SideOnly(Side.CLIENT)
    public static class AmbientMobFactory implements IParticleFactory
        {
            private static final String __OBFID = "CL_00002585";

            public EntityFX getEntityFX(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_)
            {
                EntitySpellParticleFX entityspellparticlefx = new EntitySpellParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
                entityspellparticlefx.setAlphaF(0.15F);
                entityspellparticlefx.setRBGColorF((float)p_178902_9_, (float)p_178902_11_, (float)p_178902_13_);
                return entityspellparticlefx;
            }
        }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
        {
            private static final String __OBFID = "CL_00002582";

            public EntityFX getEntityFX(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_)
            {
                return new EntitySpellParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
            }
        }

    @SideOnly(Side.CLIENT)
    public static class InstantFactory implements IParticleFactory
        {
            private static final String __OBFID = "CL_00002584";

            public EntityFX getEntityFX(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_)
            {
                EntitySpellParticleFX entityspellparticlefx = new EntitySpellParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
                ((EntitySpellParticleFX)entityspellparticlefx).setBaseSpellTextureIndex(144);
                return entityspellparticlefx;
            }
        }

    @SideOnly(Side.CLIENT)
    public static class MobFactory implements IParticleFactory
        {
            private static final String __OBFID = "CL_00002583";

            public EntityFX getEntityFX(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_)
            {
                EntitySpellParticleFX entityspellparticlefx = new EntitySpellParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
                entityspellparticlefx.setRBGColorF((float)p_178902_9_, (float)p_178902_11_, (float)p_178902_13_);
                return entityspellparticlefx;
            }
        }

    @SideOnly(Side.CLIENT)
    public static class WitchFactory implements IParticleFactory
        {
            private static final String __OBFID = "CL_00002581";

            public EntityFX getEntityFX(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_)
            {
                EntitySpellParticleFX entityspellparticlefx = new EntitySpellParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
                ((EntitySpellParticleFX)entityspellparticlefx).setBaseSpellTextureIndex(144);
                float f = worldIn.rand.nextFloat() * 0.5F + 0.35F;
                entityspellparticlefx.setRBGColorF(1.0F * f, 0.0F * f, 1.0F * f);
                return entityspellparticlefx;
            }
        }
}