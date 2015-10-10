package net.minecraft.world.gen.layer;

import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import static net.minecraftforge.common.BiomeManager.BiomeType.*;

public class GenLayerBiome extends GenLayer
{
    private List<BiomeEntry>[] biomes = new ArrayList[BiomeManager.BiomeType.values().length];

    private final ChunkProviderSettings field_175973_g;
    private static final String __OBFID = "CL_00000555";

    public GenLayerBiome(long p_i45560_1_, GenLayer p_i45560_3_, WorldType p_i45560_4_, String p_i45560_5_)
    {
        super(p_i45560_1_);
        this.parent = p_i45560_3_;

        for (BiomeManager.BiomeType type : BiomeManager.BiomeType.values())
        {
            com.google.common.collect.ImmutableList<BiomeEntry> biomesToAdd = BiomeManager.getBiomes(type);
            int idx = type.ordinal();

            if (biomes[idx] == null) biomes[idx] = new ArrayList<BiomeEntry>();
            if (biomesToAdd != null) biomes[idx].addAll(biomesToAdd);
        }

        int desertIdx = DESERT.ordinal();

        biomes[desertIdx].add(new BiomeEntry(BiomeGenBase.desert, 30));
        biomes[desertIdx].add(new BiomeEntry(BiomeGenBase.savanna, 20));
        biomes[desertIdx].add(new BiomeEntry(BiomeGenBase.plains, 10));

        if (p_i45560_4_ == WorldType.DEFAULT_1_1)
        {
            biomes[desertIdx].clear();
            biomes[desertIdx].add(new BiomeEntry(BiomeGenBase.desert, 10));
            biomes[desertIdx].add(new BiomeEntry(BiomeGenBase.forest, 10));
            biomes[desertIdx].add(new BiomeEntry(BiomeGenBase.extremeHills, 10));
            biomes[desertIdx].add(new BiomeEntry(BiomeGenBase.swampland, 10));
            biomes[desertIdx].add(new BiomeEntry(BiomeGenBase.plains, 10));
            biomes[desertIdx].add(new BiomeEntry(BiomeGenBase.taiga, 10));
            this.field_175973_g = null;
        }
        else if (p_i45560_4_ == WorldType.CUSTOMIZED)
        {
            this.field_175973_g = ChunkProviderSettings.Factory.func_177865_a(p_i45560_5_).func_177864_b();
        }
        else
        {
            this.field_175973_g = null;
        }
    }

    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
    {
        int[] aint = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i1 = 0; i1 < areaHeight; ++i1)
        {
            for (int j1 = 0; j1 < areaWidth; ++j1)
            {
                this.initChunkSeed((long)(j1 + areaX), (long)(i1 + areaY));
                int k1 = aint[j1 + i1 * areaWidth];
                int l1 = (k1 & 3840) >> 8;
                k1 &= -3841;

                if (this.field_175973_g != null && this.field_175973_g.fixedBiome >= 0)
                {
                    aint1[j1 + i1 * areaWidth] = this.field_175973_g.fixedBiome;
                }
                else if (isBiomeOceanic(k1))
                {
                    aint1[j1 + i1 * areaWidth] = k1;
                }
                else if (k1 == BiomeGenBase.mushroomIsland.biomeID)
                {
                    aint1[j1 + i1 * areaWidth] = k1;
                }
                else if (k1 == 1)
                {
                    if (l1 > 0)
                    {
                        if (this.nextInt(3) == 0)
                        {
                            aint1[j1 + i1 * areaWidth] = BiomeGenBase.mesaPlateau.biomeID;
                        }
                        else
                        {
                            aint1[j1 + i1 * areaWidth] = BiomeGenBase.mesaPlateau_F.biomeID;
                        }
                    }
                    else
                    {
                        aint1[j1 + i1 * areaWidth] = getWeightedBiomeEntry(DESERT).biome.biomeID;
                    }
                }
                else if (k1 == 2)
                {
                    if (l1 > 0)
                    {
                        aint1[j1 + i1 * areaWidth] = BiomeGenBase.jungle.biomeID;
                    }
                    else
                    {
                        aint1[j1 + i1 * areaWidth] = getWeightedBiomeEntry(WARM).biome.biomeID;
                    }
                }
                else if (k1 == 3)
                {
                    if (l1 > 0)
                    {
                        aint1[j1 + i1 * areaWidth] = BiomeGenBase.megaTaiga.biomeID;
                    }
                    else
                    {
                        aint1[j1 + i1 * areaWidth] = getWeightedBiomeEntry(COOL).biome.biomeID;
                    }
                }
                else if (k1 == 4)
                {
                    aint1[j1 + i1 * areaWidth] = getWeightedBiomeEntry(ICY).biome.biomeID;
                }
                else
                {
                    aint1[j1 + i1 * areaWidth] = BiomeGenBase.mushroomIsland.biomeID;
                }
            }
        }

        return aint1;
    }

    protected BiomeEntry getWeightedBiomeEntry(BiomeManager.BiomeType type)
    {
        List<BiomeEntry> biomeList = biomes[type.ordinal()];
        int totalWeight = WeightedRandom.getTotalWeight(biomeList);
        int weight = BiomeManager.isTypeListModded(type)?nextInt(totalWeight):nextInt(totalWeight / 10) * 10;
        return (BiomeEntry)WeightedRandom.getRandomItem(biomeList, weight);
    }
}