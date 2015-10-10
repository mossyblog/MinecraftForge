package net.minecraft.client.renderer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Tessellator
{
    private WorldRenderer worldRenderer;
    private WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
    private static final Tessellator instance = new Tessellator(2097152);
    private static final String __OBFID = "CL_00000960";

    public static Tessellator getInstance()
    {
        return instance;
    }

    public Tessellator(int bufferSize)
    {
        this.worldRenderer = new WorldRenderer(bufferSize);
    }

    public int draw()
    {
        return this.vboUploader.draw(this.worldRenderer, this.worldRenderer.finishDrawing());
    }

    public WorldRenderer getWorldRenderer()
    {
        return this.worldRenderer;
    }
}