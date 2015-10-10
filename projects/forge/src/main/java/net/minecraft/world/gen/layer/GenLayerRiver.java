package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerRiver extends GenLayer
{
    private static final String __OBFID = "CL_00000566";

    public GenLayerRiver(long p_i2128_1_, GenLayer p_i2128_3_)
    {
        super(p_i2128_1_);
        super.parent = p_i2128_3_;
    }

    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
    {
        int i1 = areaX - 1;
        int j1 = areaY - 1;
        int k1 = areaWidth + 2;
        int l1 = areaHeight + 2;
        int[] aint = this.parent.getInts(i1, j1, k1, l1);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i2 = 0; i2 < areaHeight; ++i2)
        {
            for (int j2 = 0; j2 < areaWidth; ++j2)
            {
                int k2 = this.func_151630_c(aint[j2 + 0 + (i2 + 1) * k1]);
                int l2 = this.func_151630_c(aint[j2 + 2 + (i2 + 1) * k1]);
                int i3 = this.func_151630_c(aint[j2 + 1 + (i2 + 0) * k1]);
                int j3 = this.func_151630_c(aint[j2 + 1 + (i2 + 2) * k1]);
                int k3 = this.func_151630_c(aint[j2 + 1 + (i2 + 1) * k1]);

                if (k3 == k2 && k3 == i3 && k3 == l2 && k3 == j3)
                {
                    aint1[j2 + i2 * areaWidth] = -1;
                }
                else
                {
                    aint1[j2 + i2 * areaWidth] = BiomeGenBase.river.biomeID;
                }
            }
        }

        return aint1;
    }

    private int func_151630_c(int p_151630_1_)
    {
        return p_151630_1_ >= 2 ? 2 + (p_151630_1_ & 1) : p_151630_1_;
    }
}