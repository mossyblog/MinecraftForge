package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerBiomeEdge extends GenLayer
{
    private static final String __OBFID = "CL_00000554";

    public GenLayerBiomeEdge(long p_i45475_1_, GenLayer p_i45475_3_)
    {
        super(p_i45475_1_);
        this.parent = p_i45475_3_;
    }

    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
    {
        int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i1 = 0; i1 < areaHeight; ++i1)
        {
            for (int j1 = 0; j1 < areaWidth; ++j1)
            {
                this.initChunkSeed((long)(j1 + areaX), (long)(i1 + areaY));
                int k1 = aint[j1 + 1 + (i1 + 1) * (areaWidth + 2)];

                if (!this.replaceBiomeEdgeIfNecessary(aint, aint1, j1, i1, areaWidth, k1, BiomeGenBase.extremeHills.biomeID, BiomeGenBase.extremeHillsEdge.biomeID) && !this.replaceBiomeEdge(aint, aint1, j1, i1, areaWidth, k1, BiomeGenBase.mesaPlateau_F.biomeID, BiomeGenBase.mesa.biomeID) && !this.replaceBiomeEdge(aint, aint1, j1, i1, areaWidth, k1, BiomeGenBase.mesaPlateau.biomeID, BiomeGenBase.mesa.biomeID) && !this.replaceBiomeEdge(aint, aint1, j1, i1, areaWidth, k1, BiomeGenBase.megaTaiga.biomeID, BiomeGenBase.taiga.biomeID))
                {
                    int l1;
                    int i2;
                    int j2;
                    int k2;

                    if (k1 == BiomeGenBase.desert.biomeID)
                    {
                        l1 = aint[j1 + 1 + (i1 + 1 - 1) * (areaWidth + 2)];
                        i2 = aint[j1 + 1 + 1 + (i1 + 1) * (areaWidth + 2)];
                        j2 = aint[j1 + 1 - 1 + (i1 + 1) * (areaWidth + 2)];
                        k2 = aint[j1 + 1 + (i1 + 1 + 1) * (areaWidth + 2)];

                        if (l1 != BiomeGenBase.icePlains.biomeID && i2 != BiomeGenBase.icePlains.biomeID && j2 != BiomeGenBase.icePlains.biomeID && k2 != BiomeGenBase.icePlains.biomeID)
                        {
                            aint1[j1 + i1 * areaWidth] = k1;
                        }
                        else
                        {
                            aint1[j1 + i1 * areaWidth] = BiomeGenBase.extremeHillsPlus.biomeID;
                        }
                    }
                    else if (k1 == BiomeGenBase.swampland.biomeID)
                    {
                        l1 = aint[j1 + 1 + (i1 + 1 - 1) * (areaWidth + 2)];
                        i2 = aint[j1 + 1 + 1 + (i1 + 1) * (areaWidth + 2)];
                        j2 = aint[j1 + 1 - 1 + (i1 + 1) * (areaWidth + 2)];
                        k2 = aint[j1 + 1 + (i1 + 1 + 1) * (areaWidth + 2)];

                        if (l1 != BiomeGenBase.desert.biomeID && i2 != BiomeGenBase.desert.biomeID && j2 != BiomeGenBase.desert.biomeID && k2 != BiomeGenBase.desert.biomeID && l1 != BiomeGenBase.coldTaiga.biomeID && i2 != BiomeGenBase.coldTaiga.biomeID && j2 != BiomeGenBase.coldTaiga.biomeID && k2 != BiomeGenBase.coldTaiga.biomeID && l1 != BiomeGenBase.icePlains.biomeID && i2 != BiomeGenBase.icePlains.biomeID && j2 != BiomeGenBase.icePlains.biomeID && k2 != BiomeGenBase.icePlains.biomeID)
                        {
                            if (l1 != BiomeGenBase.jungle.biomeID && k2 != BiomeGenBase.jungle.biomeID && i2 != BiomeGenBase.jungle.biomeID && j2 != BiomeGenBase.jungle.biomeID)
                            {
                                aint1[j1 + i1 * areaWidth] = k1;
                            }
                            else
                            {
                                aint1[j1 + i1 * areaWidth] = BiomeGenBase.jungleEdge.biomeID;
                            }
                        }
                        else
                        {
                            aint1[j1 + i1 * areaWidth] = BiomeGenBase.plains.biomeID;
                        }
                    }
                    else
                    {
                        aint1[j1 + i1 * areaWidth] = k1;
                    }
                }
            }
        }

        return aint1;
    }

    private boolean replaceBiomeEdgeIfNecessary(int[] p_151636_1_, int[] p_151636_2_, int p_151636_3_, int p_151636_4_, int p_151636_5_, int p_151636_6_, int p_151636_7_, int p_151636_8_)
    {
        if (!biomesEqualOrMesaPlateau(p_151636_6_, p_151636_7_))
        {
            return false;
        }
        else
        {
            int k1 = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 - 1) * (p_151636_5_ + 2)];
            int l1 = p_151636_1_[p_151636_3_ + 1 + 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
            int i2 = p_151636_1_[p_151636_3_ + 1 - 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
            int j2 = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 + 1) * (p_151636_5_ + 2)];

            if (this.canBiomesBeNeighbors(k1, p_151636_7_) && this.canBiomesBeNeighbors(l1, p_151636_7_) && this.canBiomesBeNeighbors(i2, p_151636_7_) && this.canBiomesBeNeighbors(j2, p_151636_7_))
            {
                p_151636_2_[p_151636_3_ + p_151636_4_ * p_151636_5_] = p_151636_6_;
            }
            else
            {
                p_151636_2_[p_151636_3_ + p_151636_4_ * p_151636_5_] = p_151636_8_;
            }

            return true;
        }
    }

    private boolean replaceBiomeEdge(int[] p_151635_1_, int[] p_151635_2_, int p_151635_3_, int p_151635_4_, int p_151635_5_, int p_151635_6_, int p_151635_7_, int p_151635_8_)
    {
        if (p_151635_6_ != p_151635_7_)
        {
            return false;
        }
        else
        {
            int k1 = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 - 1) * (p_151635_5_ + 2)];
            int l1 = p_151635_1_[p_151635_3_ + 1 + 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
            int i2 = p_151635_1_[p_151635_3_ + 1 - 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
            int j2 = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 + 1) * (p_151635_5_ + 2)];

            if (biomesEqualOrMesaPlateau(k1, p_151635_7_) && biomesEqualOrMesaPlateau(l1, p_151635_7_) && biomesEqualOrMesaPlateau(i2, p_151635_7_) && biomesEqualOrMesaPlateau(j2, p_151635_7_))
            {
                p_151635_2_[p_151635_3_ + p_151635_4_ * p_151635_5_] = p_151635_6_;
            }
            else
            {
                p_151635_2_[p_151635_3_ + p_151635_4_ * p_151635_5_] = p_151635_8_;
            }

            return true;
        }
    }

    private boolean canBiomesBeNeighbors(int p_151634_1_, int p_151634_2_)
    {
        if (biomesEqualOrMesaPlateau(p_151634_1_, p_151634_2_))
        {
            return true;
        }
        else
        {
            BiomeGenBase biomegenbase = BiomeGenBase.getBiome(p_151634_1_);
            BiomeGenBase biomegenbase1 = BiomeGenBase.getBiome(p_151634_2_);

            if (biomegenbase != null && biomegenbase1 != null)
            {
                BiomeGenBase.TempCategory tempcategory = biomegenbase.getTempCategory();
                BiomeGenBase.TempCategory tempcategory1 = biomegenbase1.getTempCategory();
                return tempcategory == tempcategory1 || tempcategory == BiomeGenBase.TempCategory.MEDIUM || tempcategory1 == BiomeGenBase.TempCategory.MEDIUM;
            }
            else
            {
                return false;
            }
        }
    }
}