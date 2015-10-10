package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.LongHashMap;

public class BiomeCache
{
    private final WorldChunkManager chunkManager;
    private long lastCleanupTime;
    private LongHashMap cacheMap = new LongHashMap();
    private List cache = Lists.newArrayList();
    private static final String __OBFID = "CL_00000162";

    public BiomeCache(WorldChunkManager chunkManagerIn)
    {
        this.chunkManager = chunkManagerIn;
    }

    public BiomeCache.Block getBiomeCacheBlock(int x, int z)
    {
        x >>= 4;
        z >>= 4;
        long k = (long)x & 4294967295L | ((long)z & 4294967295L) << 32;
        BiomeCache.Block block = (BiomeCache.Block)this.cacheMap.getValueByKey(k);

        if (block == null)
        {
            block = new BiomeCache.Block(x, z);
            this.cacheMap.add(k, block);
            this.cache.add(block);
        }

        block.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
        return block;
    }

    public BiomeGenBase func_180284_a(int x, int z, BiomeGenBase p_180284_3_)
    {
        BiomeGenBase biomegenbase1 = this.getBiomeCacheBlock(x, z).getBiomeGenAt(x, z);
        return biomegenbase1 == null ? p_180284_3_ : biomegenbase1;
    }

    public void cleanupCache()
    {
        long i = MinecraftServer.getCurrentTimeMillis();
        long j = i - this.lastCleanupTime;

        if (j > 7500L || j < 0L)
        {
            this.lastCleanupTime = i;

            for (int k = 0; k < this.cache.size(); ++k)
            {
                BiomeCache.Block block = (BiomeCache.Block)this.cache.get(k);
                long l = i - block.lastAccessTime;

                if (l > 30000L || l < 0L)
                {
                    this.cache.remove(k--);
                    long i1 = (long)block.xPosition & 4294967295L | ((long)block.zPosition & 4294967295L) << 32;
                    this.cacheMap.remove(i1);
                }
            }
        }
    }

    public BiomeGenBase[] getCachedBiomes(int x, int z)
    {
        return this.getBiomeCacheBlock(x, z).biomes;
    }

    public class Block
    {
        public float[] rainfallValues = new float[256];
        public BiomeGenBase[] biomes = new BiomeGenBase[256];
        public int xPosition;
        public int zPosition;
        public long lastAccessTime;
        private static final String __OBFID = "CL_00000163";

        public Block(int x, int z)
        {
            this.xPosition = x;
            this.zPosition = z;
            BiomeCache.this.chunkManager.getRainfall(this.rainfallValues, x << 4, z << 4, 16, 16);
            BiomeCache.this.chunkManager.getBiomeGenAt(this.biomes, x << 4, z << 4, 16, 16, false);
        }

        public BiomeGenBase getBiomeGenAt(int x, int z)
        {
            return this.biomes[x & 15 | (z & 15) << 4];
        }
    }
}