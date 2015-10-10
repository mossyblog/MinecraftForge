package net.minecraft.server.management;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerManager
{
    private static final Logger pmLogger = LogManager.getLogger();
    private final WorldServer theWorldServer;
    private final List players = Lists.newArrayList();
    private final LongHashMap playerInstances = new LongHashMap();
    private final List playerInstancesToUpdate = Lists.newArrayList();
    private final List playerInstanceList = Lists.newArrayList();
    private int playerViewRadius;
    private long previousTotalWorldTime;
    private final int[][] xzDirectionsConst = new int[][] {{1, 0}, {0, 1}, { -1, 0}, {0, -1}};
    private static final String __OBFID = "CL_00001434";

    public PlayerManager(WorldServer serverWorld)
    {
        this.theWorldServer = serverWorld;
        this.setPlayerViewRadius(serverWorld.getMinecraftServer().getConfigurationManager().getViewDistance());
    }

    public WorldServer getMinecraftServer()
    {
        return this.theWorldServer;
    }

    public void updatePlayerInstances()
    {
        long i = this.theWorldServer.getTotalWorldTime();
        int j;
        PlayerManager.PlayerInstance playerinstance;

        if (i - this.previousTotalWorldTime > 8000L)
        {
            this.previousTotalWorldTime = i;

            for (j = 0; j < this.playerInstanceList.size(); ++j)
            {
                playerinstance = (PlayerManager.PlayerInstance)this.playerInstanceList.get(j);
                playerinstance.onUpdate();
                playerinstance.processChunk();
            }
        }
        else
        {
            for (j = 0; j < this.playerInstancesToUpdate.size(); ++j)
            {
                playerinstance = (PlayerManager.PlayerInstance)this.playerInstancesToUpdate.get(j);
                playerinstance.onUpdate();
            }
        }

        this.playerInstancesToUpdate.clear();

        if (this.players.isEmpty())
        {
            WorldProvider worldprovider = this.theWorldServer.provider;

            if (!worldprovider.canRespawnHere())
            {
                this.theWorldServer.theChunkProviderServer.unloadAllChunks();
            }
        }
    }

    public boolean hasPlayerInstance(int p_152621_1_, int p_152621_2_)
    {
        long k = (long)p_152621_1_ + 2147483647L | (long)p_152621_2_ + 2147483647L << 32;
        return this.playerInstances.getValueByKey(k) != null;
    }

    private PlayerManager.PlayerInstance getPlayerInstance(int p_72690_1_, int p_72690_2_, boolean p_72690_3_)
    {
        long k = (long)p_72690_1_ + 2147483647L | (long)p_72690_2_ + 2147483647L << 32;
        PlayerManager.PlayerInstance playerinstance = (PlayerManager.PlayerInstance)this.playerInstances.getValueByKey(k);

        if (playerinstance == null && p_72690_3_)
        {
            playerinstance = new PlayerManager.PlayerInstance(p_72690_1_, p_72690_2_);
            this.playerInstances.add(k, playerinstance);
            this.playerInstanceList.add(playerinstance);
        }

        return playerinstance;
    }

    public void markBlockForUpdate(BlockPos p_180244_1_)
    {
        int i = p_180244_1_.getX() >> 4;
        int j = p_180244_1_.getZ() >> 4;
        PlayerManager.PlayerInstance playerinstance = this.getPlayerInstance(i, j, false);

        if (playerinstance != null)
        {
            playerinstance.flagChunkForUpdate(p_180244_1_.getX() & 15, p_180244_1_.getY(), p_180244_1_.getZ() & 15);
        }
    }

    public void addPlayer(EntityPlayerMP p_72683_1_)
    {
        int i = (int)p_72683_1_.posX >> 4;
        int j = (int)p_72683_1_.posZ >> 4;
        p_72683_1_.managedPosX = p_72683_1_.posX;
        p_72683_1_.managedPosZ = p_72683_1_.posZ;
        // Load nearby chunks first
        List<ChunkCoordIntPair> chunkList = new ArrayList<ChunkCoordIntPair>();

        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k)
        {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l)
            {
                chunkList.add(new ChunkCoordIntPair(k, l));
            }
        }

        java.util.Collections.sort(chunkList, new net.minecraftforge.common.util.ChunkCoordComparator(p_72683_1_));

        for (ChunkCoordIntPair pair : chunkList)
        {
            this.getPlayerInstance(pair.chunkXPos, pair.chunkZPos, true).addPlayer(p_72683_1_);
        }

        this.players.add(p_72683_1_);
        this.filterChunkLoadQueue(p_72683_1_);
    }

    public void filterChunkLoadQueue(EntityPlayerMP p_72691_1_)
    {
        ArrayList arraylist = Lists.newArrayList(p_72691_1_.loadedChunks);
        int i = 0;
        int j = this.playerViewRadius;
        int k = (int)p_72691_1_.posX >> 4;
        int l = (int)p_72691_1_.posZ >> 4;
        int i1 = 0;
        int j1 = 0;
        ChunkCoordIntPair chunkcoordintpair = this.getPlayerInstance(k, l, true).chunkCoords;
        p_72691_1_.loadedChunks.clear();

        if (arraylist.contains(chunkcoordintpair))
        {
            p_72691_1_.loadedChunks.add(chunkcoordintpair);
        }

        int k1;

        for (k1 = 1; k1 <= j * 2; ++k1)
        {
            for (int l1 = 0; l1 < 2; ++l1)
            {
                int[] aint = this.xzDirectionsConst[i++ % 4];

                for (int i2 = 0; i2 < k1; ++i2)
                {
                    i1 += aint[0];
                    j1 += aint[1];
                    chunkcoordintpair = this.getPlayerInstance(k + i1, l + j1, true).chunkCoords;

                    if (arraylist.contains(chunkcoordintpair))
                    {
                        p_72691_1_.loadedChunks.add(chunkcoordintpair);
                    }
                }
            }
        }

        i %= 4;

        for (k1 = 0; k1 < j * 2; ++k1)
        {
            i1 += this.xzDirectionsConst[i][0];
            j1 += this.xzDirectionsConst[i][1];
            chunkcoordintpair = this.getPlayerInstance(k + i1, l + j1, true).chunkCoords;

            if (arraylist.contains(chunkcoordintpair))
            {
                p_72691_1_.loadedChunks.add(chunkcoordintpair);
            }
        }
    }

    public void removePlayer(EntityPlayerMP p_72695_1_)
    {
        int i = (int)p_72695_1_.managedPosX >> 4;
        int j = (int)p_72695_1_.managedPosZ >> 4;

        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k)
        {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l)
            {
                PlayerManager.PlayerInstance playerinstance = this.getPlayerInstance(k, l, false);

                if (playerinstance != null)
                {
                    playerinstance.removePlayer(p_72695_1_);
                }
            }
        }

        this.players.remove(p_72695_1_);
    }

    private boolean overlaps(int p_72684_1_, int p_72684_2_, int p_72684_3_, int p_72684_4_, int p_72684_5_)
    {
        int j1 = p_72684_1_ - p_72684_3_;
        int k1 = p_72684_2_ - p_72684_4_;
        return j1 >= -p_72684_5_ && j1 <= p_72684_5_ ? k1 >= -p_72684_5_ && k1 <= p_72684_5_ : false;
    }

    public void updateMountedMovingPlayer(EntityPlayerMP p_72685_1_)
    {
        int i = (int)p_72685_1_.posX >> 4;
        int j = (int)p_72685_1_.posZ >> 4;
        double d0 = p_72685_1_.managedPosX - p_72685_1_.posX;
        double d1 = p_72685_1_.managedPosZ - p_72685_1_.posZ;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 >= 64.0D)
        {
            int k = (int)p_72685_1_.managedPosX >> 4;
            int l = (int)p_72685_1_.managedPosZ >> 4;
            int i1 = this.playerViewRadius;
            int j1 = i - k;
            int k1 = j - l;
            List<ChunkCoordIntPair> chunksToLoad = new ArrayList<ChunkCoordIntPair>();

            if (j1 != 0 || k1 != 0)
            {
                for (int l1 = i - i1; l1 <= i + i1; ++l1)
                {
                    for (int i2 = j - i1; i2 <= j + i1; ++i2)
                    {
                        if (!this.overlaps(l1, i2, k, l, i1))
                        {
                            chunksToLoad.add(new ChunkCoordIntPair(l1, i2));
                        }

                        if (!this.overlaps(l1 - j1, i2 - k1, i, j, i1))
                        {
                            PlayerManager.PlayerInstance playerinstance = this.getPlayerInstance(l1 - j1, i2 - k1, false);

                            if (playerinstance != null)
                            {
                                playerinstance.removePlayer(p_72685_1_);
                            }
                        }
                    }
                }

                this.filterChunkLoadQueue(p_72685_1_);
                p_72685_1_.managedPosX = p_72685_1_.posX;
                p_72685_1_.managedPosZ = p_72685_1_.posZ;
                // send nearest chunks first
                java.util.Collections.sort(chunksToLoad, new net.minecraftforge.common.util.ChunkCoordComparator(p_72685_1_));

                for (ChunkCoordIntPair pair : chunksToLoad)
                {
                    this.getPlayerInstance(pair.chunkXPos, pair.chunkZPos, true).addPlayer(p_72685_1_);
                }

                if (i1 > 1 || i1 < -1 || j1 > 1 || j1 < -1)
                {
                    java.util.Collections.sort(p_72685_1_.loadedChunks, new net.minecraftforge.common.util.ChunkCoordComparator(p_72685_1_));
                }
            }
        }
    }

    public boolean isPlayerWatchingChunk(EntityPlayerMP p_72694_1_, int p_72694_2_, int p_72694_3_)
    {
        PlayerManager.PlayerInstance playerinstance = this.getPlayerInstance(p_72694_2_, p_72694_3_, false);
        return playerinstance != null && playerinstance.playersWatchingChunk.contains(p_72694_1_) && !p_72694_1_.loadedChunks.contains(playerinstance.chunkCoords);
    }

    public void setPlayerViewRadius(int p_152622_1_)
    {
        p_152622_1_ = MathHelper.clamp_int(p_152622_1_, 3, 32);

        if (p_152622_1_ != this.playerViewRadius)
        {
            int j = p_152622_1_ - this.playerViewRadius;
            ArrayList arraylist = Lists.newArrayList(this.players);
            Iterator iterator = arraylist.iterator();

            while (iterator.hasNext())
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)iterator.next();
                int k = (int)entityplayermp.posX >> 4;
                int l = (int)entityplayermp.posZ >> 4;
                int i1;
                int j1;

                if (j > 0)
                {
                    for (i1 = k - p_152622_1_; i1 <= k + p_152622_1_; ++i1)
                    {
                        for (j1 = l - p_152622_1_; j1 <= l + p_152622_1_; ++j1)
                        {
                            PlayerManager.PlayerInstance playerinstance = this.getPlayerInstance(i1, j1, true);

                            if (!playerinstance.playersWatchingChunk.contains(entityplayermp))
                            {
                                playerinstance.addPlayer(entityplayermp);
                            }
                        }
                    }
                }
                else
                {
                    for (i1 = k - this.playerViewRadius; i1 <= k + this.playerViewRadius; ++i1)
                    {
                        for (j1 = l - this.playerViewRadius; j1 <= l + this.playerViewRadius; ++j1)
                        {
                            if (!this.overlaps(i1, j1, k, l, p_152622_1_))
                            {
                                this.getPlayerInstance(i1, j1, true).removePlayer(entityplayermp);
                            }
                        }
                    }
                }
            }

            this.playerViewRadius = p_152622_1_;
        }
    }

    public static int getFurthestViewableBlock(int p_72686_0_)
    {
        return p_72686_0_ * 16 - 16;
    }

    class PlayerInstance
    {
        private final List playersWatchingChunk = Lists.newArrayList();
        private final ChunkCoordIntPair chunkCoords;
        private short[] locationOfBlockChange = new short[64];
        private int numBlocksToUpdate;
        private int flagsYAreasToUpdate;
        private long previousWorldTime;
        private final java.util.HashMap<EntityPlayerMP, Runnable> players = new java.util.HashMap<EntityPlayerMP, Runnable>();
        private boolean loaded = false;
        private Runnable loadedRunnable = new Runnable()
        {
            public void run()
            {
                PlayerInstance.this.loaded = true;
            }
        };
        private static final String __OBFID = "CL_00001435";

        public PlayerInstance(int p_i1518_2_, int p_i1518_3_)
        {
            this.chunkCoords = new ChunkCoordIntPair(p_i1518_2_, p_i1518_3_);
            PlayerManager.this.getMinecraftServer().theChunkProviderServer.loadChunk(p_i1518_2_, p_i1518_3_, this.loadedRunnable);
        }

        public void addPlayer(EntityPlayerMP p_73255_1_)
        {
            if (this.playersWatchingChunk.contains(p_73255_1_))
            {
                PlayerManager.pmLogger.debug("Failed to add player. {} already is in chunk {}, {}", new Object[] {p_73255_1_, Integer.valueOf(this.chunkCoords.chunkXPos), Integer.valueOf(this.chunkCoords.chunkZPos)});
            }
            else
            {
                if (this.playersWatchingChunk.isEmpty())
                {
                    this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
                }

                this.playersWatchingChunk.add(p_73255_1_);
                Runnable playerRunnable = null;
                if (this.loaded)
                {
                p_73255_1_.loadedChunks.add(this.chunkCoords);
                }
                else
                {
                    final EntityPlayerMP tmp = p_73255_1_;
                    playerRunnable = new Runnable()
                    {
                        public void run()
                        {
                            tmp.loadedChunks.add(PlayerInstance.this.chunkCoords);
                        }
                    };
                    PlayerManager.this.getMinecraftServer().theChunkProviderServer.loadChunk(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos, playerRunnable);
                }
                this.players.put(p_73255_1_, playerRunnable);
            }
        }

        public void removePlayer(EntityPlayerMP p_73252_1_)
        {
            if (this.playersWatchingChunk.contains(p_73252_1_))
            {
                // If we haven't loaded yet don't load the chunk just so we can clean it up
                if (!this.loaded)
                {
                    net.minecraftforge.common.chunkio.ChunkIOExecutor.dropQueuedChunkLoad(PlayerManager.this.getMinecraftServer(), this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos, this.players.get(p_73252_1_));
                    this.playersWatchingChunk.remove(p_73252_1_);
                    this.players.remove(p_73252_1_);

                    if (this.playersWatchingChunk.isEmpty())
                    {
                        net.minecraftforge.common.chunkio.ChunkIOExecutor.dropQueuedChunkLoad(PlayerManager.this.getMinecraftServer(), this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos, this.loadedRunnable);
                        long i = (long) this.chunkCoords.chunkXPos + 2147483647L | (long) this.chunkCoords.chunkZPos + 2147483647L << 32;
                        PlayerManager.this.playerInstances.remove(i);
                        PlayerManager.this.playerInstanceList.remove(this);
                    }

                    return;
                }

                Chunk chunk = PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);

                if (chunk.isPopulated())
                {
                    p_73252_1_.playerNetServerHandler.sendPacket(new S21PacketChunkData(chunk, true, 0));
                }

                this.players.remove(p_73252_1_);
                this.playersWatchingChunk.remove(p_73252_1_);
                p_73252_1_.loadedChunks.remove(this.chunkCoords);

                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.ChunkWatchEvent.UnWatch(chunkCoords, p_73252_1_));

                if (this.playersWatchingChunk.isEmpty())
                {
                    long i = (long)this.chunkCoords.chunkXPos + 2147483647L | (long)this.chunkCoords.chunkZPos + 2147483647L << 32;
                    this.increaseInhabitedTime(chunk);
                    PlayerManager.this.playerInstances.remove(i);
                    PlayerManager.this.playerInstanceList.remove(this);

                    if (this.numBlocksToUpdate > 0)
                    {
                        PlayerManager.this.playerInstancesToUpdate.remove(this);
                    }

                    PlayerManager.this.getMinecraftServer().theChunkProviderServer.dropChunk(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
                }
            }
        }

        public void processChunk()
        {
            this.increaseInhabitedTime(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos));
        }

        private void increaseInhabitedTime(Chunk p_111196_1_)
        {
            p_111196_1_.setInhabitedTime(p_111196_1_.getInhabitedTime() + PlayerManager.this.theWorldServer.getTotalWorldTime() - this.previousWorldTime);
            this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
        }

        public void flagChunkForUpdate(int p_151253_1_, int p_151253_2_, int p_151253_3_)
        {
            if (this.numBlocksToUpdate == 0)
            {
                PlayerManager.this.playerInstancesToUpdate.add(this);
            }

            this.flagsYAreasToUpdate |= 1 << (p_151253_2_ >> 4);

            //Forge; Cache everything, so always run
            {
                short short1 = (short)(p_151253_1_ << 12 | p_151253_3_ << 8 | p_151253_2_);

                for (int l = 0; l < this.numBlocksToUpdate; ++l)
                {
                    if (this.locationOfBlockChange[l] == short1)
                    {
                        return;
                    }
                }

                if (numBlocksToUpdate == locationOfBlockChange.length)
                {
                    locationOfBlockChange = java.util.Arrays.copyOf(locationOfBlockChange, locationOfBlockChange.length << 1);
                }
                this.locationOfBlockChange[this.numBlocksToUpdate++] = short1;
            }
        }

        public void sendToAllPlayersWatchingChunk(Packet p_151251_1_)
        {
            for (int i = 0; i < this.playersWatchingChunk.size(); ++i)
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)this.playersWatchingChunk.get(i);

                if (!entityplayermp.loadedChunks.contains(this.chunkCoords))
                {
                    entityplayermp.playerNetServerHandler.sendPacket(p_151251_1_);
                }
            }
        }

        @SuppressWarnings("unused")
        public void onUpdate()
        {
            if (this.numBlocksToUpdate != 0)
            {
                int i;
                int j;
                int k;

                if (this.numBlocksToUpdate == 1)
                {
                    i = (this.locationOfBlockChange[0] >> 12 & 15) + this.chunkCoords.chunkXPos * 16;
                    j = this.locationOfBlockChange[0] & 255;
                    k = (this.locationOfBlockChange[0] >> 8 & 15) + this.chunkCoords.chunkZPos * 16;
                    BlockPos blockpos = new BlockPos(i, j, k);
                    this.sendToAllPlayersWatchingChunk(new S23PacketBlockChange(PlayerManager.this.theWorldServer, blockpos));

                    if (PlayerManager.this.theWorldServer.getBlockState(blockpos).getBlock().hasTileEntity(PlayerManager.this.theWorldServer.getBlockState(blockpos)))
                    {
                        this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos));
                    }
                }
                else
                {
                    int i1;

                    if (this.numBlocksToUpdate >= net.minecraftforge.common.ForgeModContainer.clumpingThreshold)
                    {
                        i = this.chunkCoords.chunkXPos * 16;
                        j = this.chunkCoords.chunkZPos * 16;
                        this.sendToAllPlayersWatchingChunk(new S21PacketChunkData(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos), false, this.flagsYAreasToUpdate));

                        // Forge: Grabs ALL tile entities is costly on a modded server, only send needed ones
                        for (k = 0; false && k < 16; ++k)
                        {
                            if ((this.flagsYAreasToUpdate & 1 << k) != 0)
                            {
                                i1 = k << 4;
                                List list = PlayerManager.this.theWorldServer.func_147486_a(i, i1, j, i + 16, i1 + 16, j + 16);

                                for (int l = 0; l < list.size(); ++l)
                                {
                                    this.sendTileToAllPlayersWatchingChunk((TileEntity)list.get(l));
                                }
                            }
                        }
                    }
                    else
                    {
                        this.sendToAllPlayersWatchingChunk(new S22PacketMultiBlockChange(this.numBlocksToUpdate, this.locationOfBlockChange, PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos)));
                    }

                    { //Forge: Send only the tile entities that are updated, Adding this brace lets us keep the indent and the patch small
                        WorldServer world = PlayerManager.this.theWorldServer;
                        for (i = 0; i < this.numBlocksToUpdate; ++i)
                        {
                            j = (this.locationOfBlockChange[i] >> 12 & 15) + this.chunkCoords.chunkXPos * 16;
                            k = this.locationOfBlockChange[i] & 255;
                            i1 = (this.locationOfBlockChange[i] >> 8 & 15) + this.chunkCoords.chunkZPos * 16;
                            BlockPos blockpos1 = new BlockPos(j, k, i1);

                            if (world.getBlockState(blockpos1).getBlock().hasTileEntity(world.getBlockState(blockpos1)))
                            {
                                this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos1));
                            }
                        }
                    }
                }

                this.numBlocksToUpdate = 0;
                this.flagsYAreasToUpdate = 0;
            }
        }

        private void sendTileToAllPlayersWatchingChunk(TileEntity p_151252_1_)
        {
            if (p_151252_1_ != null)
            {
                Packet packet = p_151252_1_.getDescriptionPacket();

                if (packet != null)
                {
                    this.sendToAllPlayersWatchingChunk(packet);
                }
            }
        }
    }
}