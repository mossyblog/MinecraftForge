package net.minecraft.client.multiplayer;

import com.riagenic.HAXE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.riagenic.MossyClient.*;

@SideOnly(Side.CLIENT)
public class PlayerControllerMP
{
    private final Minecraft mc;
    private final NetHandlerPlayClient netClientHandler;
    private BlockPos currentBlock = new BlockPos(-1, -1, -1);
    private ItemStack currentItemHittingBlock;
    private float curBlockDamageMP;
    private float stepSoundTickCounter;
    private int blockHitDelay;
    private boolean isHittingBlock;
    private WorldSettings.GameType currentGameType;
    private int currentPlayerItem;
    private static final String __OBFID = "CL_00000881";

    public PlayerControllerMP(Minecraft mcIn, NetHandlerPlayClient p_i45062_2_)
    {
        this.currentGameType = WorldSettings.GameType.SURVIVAL;
        this.mc = mcIn;
        this.netClientHandler = p_i45062_2_;
    }

    public static void clickBlockCreative(Minecraft mcIn, PlayerControllerMP p_178891_1_, BlockPos p_178891_2_, EnumFacing p_178891_3_)
    {
        if (!mcIn.theWorld.extinguishFire(mcIn.thePlayer, p_178891_2_, p_178891_3_))
        {
            p_178891_1_.onPlayerDestroyBlock(p_178891_2_, p_178891_3_);
        }
    }

    public void setPlayerCapabilities(EntityPlayer p_78748_1_)
    {
        this.currentGameType.configurePlayerCapabilities(p_78748_1_.capabilities);
    }

    public boolean isSpectator()
    {
        return this.currentGameType == WorldSettings.GameType.SPECTATOR;
    }

    public void setGameType(WorldSettings.GameType p_78746_1_)
    {
        this.currentGameType = p_78746_1_;
        this.currentGameType.configurePlayerCapabilities(this.mc.thePlayer.capabilities);
    }

    public void flipPlayer(EntityPlayer playerIn)
    {
        playerIn.rotationYaw = -180.0F;
    }

    public boolean shouldDrawHUD()
    {
        return this.currentGameType.isSurvivalOrAdventure();
    }

    public boolean onPlayerDestroyBlock(BlockPos pos, EnumFacing side)
    {
        if (this.currentGameType.isAdventure())
        {
            if (this.currentGameType == WorldSettings.GameType.SPECTATOR)
            {
                return false;
            }

            if (!this.mc.thePlayer.isAllowEdit())
            {
                Block block = this.mc.theWorld.getBlockState(pos).getBlock();
                ItemStack itemstack = this.mc.thePlayer.getCurrentEquippedItem();

                if (itemstack == null)
                {
                    return false;
                }

                if (!itemstack.canDestroy(block))
                {
                    return false;
                }
            }
        }

        ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
        if (stack != null && stack.getItem() != null && stack.getItem().onBlockStartBreak(stack, pos, mc.thePlayer))
        {
            return false;
        }

        if (this.currentGameType.isCreative() && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
        {
            return false;
        }
        else
        {
            WorldClient worldclient = this.mc.theWorld;
            IBlockState iblockstate = worldclient.getBlockState(pos);
            Block block1 = iblockstate.getBlock();

            if (block1.getMaterial() == Material.air)
            {
                return false;
            }
            else
            {
                worldclient.playAuxSFX(2001, pos, Block.getStateId(iblockstate));
                boolean flag = block1.removedByPlayer(worldclient, pos, mc.thePlayer, false);

                if (flag)
                {
                    block1.onBlockDestroyedByPlayer(worldclient, pos, iblockstate);
                }

                this.currentBlock = new BlockPos(this.currentBlock.getX(), -1, this.currentBlock.getZ());

                if (!this.currentGameType.isCreative())
                {
                    ItemStack itemstack1 = this.mc.thePlayer.getCurrentEquippedItem();

                    if (itemstack1 != null)
                    {
                        itemstack1.onBlockDestroyed(worldclient, block1, pos, this.mc.thePlayer);

                        if (itemstack1.stackSize == 0)
                        {
                            this.mc.thePlayer.destroyCurrentEquippedItem();
                        }
                    }
                }

                return flag;
            }
        }
    }
    // TODO : HAXED - clickBlock -
    /*================================ HAXE ================================================*/
    public boolean clickBlock(BlockPos loc, EnumFacing face)
    {
        Block block;

        if (this.currentGameType.isAdventure())
        {
            if (this.currentGameType == WorldSettings.GameType.SPECTATOR)
            {
                return false;
            }

            if (!this.mc.thePlayer.isAllowEdit())
            {
                block = this.mc.theWorld.getBlockState(loc).getBlock();
                ItemStack itemstack = this.mc.thePlayer.getCurrentEquippedItem();

                if (itemstack == null)
                {
                    return false;
                }

                if (!itemstack.canDestroy(block))
                {
                    return false;
                }
            }
        }

        if (!this.mc.theWorld.getWorldBorder().contains(loc))
        {
            return false;
        }
        else
        {
            if (this.currentGameType.isCreative())
            {
                this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
                clickBlockCreative(this.mc, this, loc, face);

                this.blockHitDelay = Mossy.getMods().IsFastBreakEnabled ? 0 : 5;
            }
            else if (!this.isHittingBlock || !this.isHittingPosition(loc))
            {
                if (this.isHittingBlock)
                {
                    this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, face));
                }

                this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
                block = this.mc.theWorld.getBlockState(loc).getBlock();
                boolean flag = block.getMaterial() != Material.air;

                if (flag && this.curBlockDamageMP == 0.0F)
                {
                    block.onBlockClicked(this.mc.theWorld, loc, this.mc.thePlayer);
                }

                if (flag && block.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, loc) >= 1.0F)
                {
                    this.onPlayerDestroyBlock(loc, face);
                }
                else
                {
                    this.isHittingBlock = true;
                    this.currentBlock = loc;
                    this.currentItemHittingBlock = this.mc.thePlayer.getHeldItem();
                    this.curBlockDamageMP = 0.0F;
                    this.stepSoundTickCounter = 0.0F;
                    this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
                }
            }

            return true;
        }
    }
    /*================================ HAXE ================================================*/
    public void resetBlockRemoving()
    {
        if (this.isHittingBlock)
        {
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, EnumFacing.DOWN));
            this.isHittingBlock = false;
            this.curBlockDamageMP = 0.0F;
            this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.currentBlock, -1);
        }
    }

    @HAXE(Side.CLIENT)
    // TODO : HAXED - onPlayerDamageBlock -
    /*================================ HAXE ================================================*/
    public boolean onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing)
    {

        this.syncCurrentPlayItem();

        if (this.blockHitDelay > 0)
        {
            --this.blockHitDelay;
            return true;
        }
        else if (this.currentGameType.isCreative() && this.mc.theWorld.getWorldBorder().contains(posBlock))
        {
            this.blockHitDelay = Mossy.getMods().IsFastBreakEnabled ? 0 : 5;
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, posBlock, directionFacing));
            clickBlockCreative(this.mc, this, posBlock, directionFacing);
            return true;
        }
        else if (this.isHittingPosition(posBlock))
        {
            Block block = this.mc.theWorld.getBlockState(posBlock).getBlock();

            if (block.getMaterial() == Material.air)
            {
                this.isHittingBlock = false;
                return false;
            }
            else
            {
                float blckDmgAmt = block.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, posBlock);


                // PUMP UP THE SPEED YO...
                if(Mossy.getMods().IsFastBreakEnabled && !currentGameType.isCreative()) {
                    blckDmgAmt *= Mossy.getMods().FastBreakSpeed;
                }

                this.curBlockDamageMP += blckDmgAmt;

                if (this.stepSoundTickCounter % 4.0F == 0.0F)
                {
                    this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(block.stepSound.getStepSound()), (block.stepSound.getVolume() + 1.0F) / 8.0F, block.stepSound.getFrequency() * 0.5F, (float)posBlock.getX() + 0.5F, (float)posBlock.getY() + 0.5F, (float)posBlock.getZ() + 0.5F));
                }

                ++this.stepSoundTickCounter;

                if (this.curBlockDamageMP >= 1.0F)
                {
                    this.isHittingBlock = false;
                    this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posBlock, directionFacing));
                    this.onPlayerDestroyBlock(posBlock, directionFacing);
                    this.curBlockDamageMP = 0.0F;
                    this.stepSoundTickCounter = 0.0F;
                    this.blockHitDelay = Mossy.getMods().IsFastBreakEnabled ? 0 : 5;
                } else if( !this.currentGameType.isCreative() && Mossy.getMods().IsFastBreakEnabled ) {
                    this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posBlock, directionFacing));
                }

                this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
                return true;
            }
        }
        else
        {
            return this.clickBlock(posBlock, directionFacing);
        }
    }
    /*================================ HAXE ================================================*/

    public float getBlockReachDistance()
    {
        return this.currentGameType.isCreative() ? 5.0F : 4.5F;
    }

    public void updateController()
    {
        this.syncCurrentPlayItem();

        if (this.netClientHandler.getNetworkManager().isChannelOpen())
        {
            this.netClientHandler.getNetworkManager().processReceivedPackets();
        }
        else
        {
            this.netClientHandler.getNetworkManager().checkDisconnected();
        }
    }

    private boolean isHittingPosition(BlockPos pos)
    {
        ItemStack itemstack = this.mc.thePlayer.getHeldItem();
        boolean flag = this.currentItemHittingBlock == null && itemstack == null;

        if (this.currentItemHittingBlock != null && itemstack != null)
        {
            flag = itemstack.getItem() == this.currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.currentItemHittingBlock) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.currentItemHittingBlock.getMetadata());
        }

        return pos.equals(this.currentBlock) && flag;
    }

    private void syncCurrentPlayItem()
    {
        int i = this.mc.thePlayer.inventory.currentItem;

        if (i != this.currentPlayerItem)
        {
            this.currentPlayerItem = i;
            this.netClientHandler.addToSendQueue(new C09PacketHeldItemChange(this.currentPlayerItem));
        }
    }

    public boolean func_178890_a(EntityPlayerSP p_178890_1_, WorldClient p_178890_2_, ItemStack p_178890_3_, BlockPos p_178890_4_, EnumFacing p_178890_5_, Vec3 p_178890_6_)
    {
        this.syncCurrentPlayItem();
        float f = (float)(p_178890_6_.xCoord - (double)p_178890_4_.getX());
        float f1 = (float)(p_178890_6_.yCoord - (double)p_178890_4_.getY());
        float f2 = (float)(p_178890_6_.zCoord - (double)p_178890_4_.getZ());
        boolean flag = false;

        if (!this.mc.theWorld.getWorldBorder().contains(p_178890_4_))
        {
            return false;
        }
        else
        {
            if (this.currentGameType != WorldSettings.GameType.SPECTATOR)
            {

                if (p_178890_3_ != null &&
                    p_178890_3_.getItem() != null &&
                    p_178890_3_.getItem().onItemUseFirst(p_178890_3_, p_178890_1_, p_178890_2_, p_178890_4_, p_178890_5_, f, f1, f2))
                {
                        return true;
                }

                IBlockState iblockstate = p_178890_2_.getBlockState(p_178890_4_);

                if ((!p_178890_1_.isSneaking() || p_178890_1_.getHeldItem() == null || p_178890_1_.getHeldItem().getItem().doesSneakBypassUse(p_178890_2_, p_178890_4_, p_178890_1_)))
                {
                    flag = iblockstate.getBlock().onBlockActivated(p_178890_2_, p_178890_4_, iblockstate, p_178890_1_, p_178890_5_, f, f1, f2);
                }

                if (!flag && p_178890_3_ != null && p_178890_3_.getItem() instanceof ItemBlock)
                {
                    ItemBlock itemblock = (ItemBlock)p_178890_3_.getItem();

                    if (!itemblock.canPlaceBlockOnSide(p_178890_2_, p_178890_4_, p_178890_5_, p_178890_1_, p_178890_3_))
                    {
                        return false;
                    }
                }
            }

            this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(p_178890_4_, p_178890_5_.getIndex(), p_178890_1_.inventory.getCurrentItem(), f, f1, f2));

            if (!flag && this.currentGameType != WorldSettings.GameType.SPECTATOR)
            {
                if (p_178890_3_ == null)
                {
                    return false;
                }
                else if (this.currentGameType.isCreative())
                {
                    int i = p_178890_3_.getMetadata();
                    int j = p_178890_3_.stackSize;
                    boolean flag1 = p_178890_3_.onItemUse(p_178890_1_, p_178890_2_, p_178890_4_, p_178890_5_, f, f1, f2);
                    p_178890_3_.setItemDamage(i);
                    p_178890_3_.stackSize = j;
                    return flag1;
                }
                else
                {
                    if (!p_178890_3_.onItemUse(p_178890_1_, p_178890_2_, p_178890_4_, p_178890_5_, f, f1, f2)) return false;
                    if (p_178890_3_.stackSize <= 0) net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(p_178890_1_, p_178890_3_);
                    return true;
                }
            }
            else
            {
                return true;
            }
        }
    }

    public boolean sendUseItem(EntityPlayer playerIn, World worldIn, ItemStack itemStackIn)
    {
        if (this.currentGameType == WorldSettings.GameType.SPECTATOR)
        {
            return false;
        }
        else
        {
            this.syncCurrentPlayItem();
            this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(playerIn.inventory.getCurrentItem()));
            int i = itemStackIn.stackSize;
            ItemStack itemstack1 = itemStackIn.useItemRightClick(worldIn, playerIn);

            if (itemstack1 == itemStackIn && (itemstack1 == null || itemstack1.stackSize == i))
            {
                return false;
            }
            else
            {
                playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = itemstack1;

                if (itemstack1.stackSize <= 0)
                {
                    playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = null;
                    net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(playerIn, itemstack1);
                }

                return true;
            }
        }
    }

    public EntityPlayerSP func_178892_a(World worldIn, StatFileWriter p_178892_2_)
    {
        return new EntityPlayerSP(this.mc, worldIn, this.netClientHandler, p_178892_2_);
    }

    public void attackEntity(EntityPlayer playerIn, Entity targetEntity)
    {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.ATTACK));

        if (this.currentGameType != WorldSettings.GameType.SPECTATOR)
        {
            playerIn.attackTargetEntityWithCurrentItem(targetEntity);
        }
    }

    public boolean interactWithEntitySendPacket(EntityPlayer playerIn, Entity targetEntity)
    {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.INTERACT));
        return this.currentGameType != WorldSettings.GameType.SPECTATOR && playerIn.interactWith(targetEntity);
    }

    public boolean func_178894_a(EntityPlayer p_178894_1_, Entity p_178894_2_, MovingObjectPosition p_178894_3_)
    {
        this.syncCurrentPlayItem();
        Vec3 vec3 = new Vec3(p_178894_3_.hitVec.xCoord - p_178894_2_.posX, p_178894_3_.hitVec.yCoord - p_178894_2_.posY, p_178894_3_.hitVec.zCoord - p_178894_2_.posZ);
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(p_178894_2_, vec3));
        return this.currentGameType != WorldSettings.GameType.SPECTATOR && p_178894_2_.func_174825_a(p_178894_1_, vec3);
    }

    public ItemStack windowClick(int windowId, int slotId, int mouseButtonClicked, int p_78753_4_, EntityPlayer playerIn)
    {
        short short1 = playerIn.openContainer.getNextTransactionID(playerIn.inventory);
        ItemStack itemstack = playerIn.openContainer.slotClick(slotId, mouseButtonClicked, p_78753_4_, playerIn);
        this.netClientHandler.addToSendQueue(new C0EPacketClickWindow(windowId, slotId, mouseButtonClicked, p_78753_4_, itemstack, short1));
        return itemstack;
    }

    public void sendEnchantPacket(int p_78756_1_, int p_78756_2_)
    {
        this.netClientHandler.addToSendQueue(new C11PacketEnchantItem(p_78756_1_, p_78756_2_));
    }

    public void sendSlotPacket(ItemStack itemStackIn, int slotId)
    {
        if (this.currentGameType.isCreative())
        {
            this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(slotId, itemStackIn));
        }
    }

    public void sendPacketDropItem(ItemStack itemStackIn)
    {
        if (this.currentGameType.isCreative() && itemStackIn != null)
        {
            this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(-1, itemStackIn));
        }
    }

    public void onStoppedUsingItem(EntityPlayer playerIn)
    {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        playerIn.stopUsingItem();
    }

    public boolean gameIsSurvivalOrAdventure()
    {
        return this.currentGameType.isSurvivalOrAdventure();
    }

    public boolean isNotCreative()
    {
        return !this.currentGameType.isCreative();
    }

    public boolean isInCreativeMode()
    {
        return this.currentGameType.isCreative();
    }

    public boolean extendedReach()
    {
        return this.currentGameType.isCreative();
    }

    public boolean isRidingHorse()
    {
        return this.mc.thePlayer.isRiding() && this.mc.thePlayer.ridingEntity instanceof EntityHorse;
    }

    public boolean isSpectatorMode()
    {
        return this.currentGameType == WorldSettings.GameType.SPECTATOR;
    }

    public WorldSettings.GameType getCurrentGameType()
    {
        return this.currentGameType;
    }
}