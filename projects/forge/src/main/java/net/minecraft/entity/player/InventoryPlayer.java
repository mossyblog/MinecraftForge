package net.minecraft.entity.player;

import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.command.server.CommandTestForBlock;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ReportedException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InventoryPlayer implements IInventory
{
    public ItemStack[] mainInventory = new ItemStack[36];
    public ItemStack[] armorInventory = new ItemStack[4];
    public int currentItem;
    public EntityPlayer player;
    private ItemStack itemStack;
    public boolean inventoryChanged;
    private static final String __OBFID = "CL_00001709";

    public InventoryPlayer(EntityPlayer playerIn)
    {
        this.player = playerIn;
    }

    public ItemStack getCurrentItem()
    {
        return this.currentItem < 9 && this.currentItem >= 0 ? this.mainInventory[this.currentItem] : null;
    }

    public static int getHotbarSize()
    {
        return 9;
    }

    private int getInventorySlotContainItem(Item itemIn)
    {
        for (int i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == itemIn)
            {
                return i;
            }
        }

        return -1;
    }

    @SideOnly(Side.CLIENT)
    private int getInventorySlotContainItemAndDamage(Item p_146024_1_, int p_146024_2_)
    {
        for (int j = 0; j < this.mainInventory.length; ++j)
        {
            if (this.mainInventory[j] != null && this.mainInventory[j].getItem() == p_146024_1_ && this.mainInventory[j].getMetadata() == p_146024_2_)
            {
                return j;
            }
        }

        return -1;
    }

    private int storeItemStack(ItemStack p_70432_1_)
    {
        for (int i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == p_70432_1_.getItem() && this.mainInventory[i].isStackable() && this.mainInventory[i].stackSize < this.mainInventory[i].getMaxStackSize() && this.mainInventory[i].stackSize < this.getInventoryStackLimit() && (!this.mainInventory[i].getHasSubtypes() || this.mainInventory[i].getMetadata() == p_70432_1_.getMetadata()) && ItemStack.areItemStackTagsEqual(this.mainInventory[i], p_70432_1_))
            {
                return i;
            }
        }

        return -1;
    }

    public int getFirstEmptyStack()
    {
        for (int i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] == null)
            {
                return i;
            }
        }

        return -1;
    }

    @SideOnly(Side.CLIENT)
    public void setCurrentItem(Item p_146030_1_, int p_146030_2_, boolean p_146030_3_, boolean p_146030_4_)
    {
        ItemStack itemstack = this.getCurrentItem();
        int j = p_146030_3_ ? this.getInventorySlotContainItemAndDamage(p_146030_1_, p_146030_2_) : this.getInventorySlotContainItem(p_146030_1_);

        if (j >= 0 && j < 9)
        {
            this.currentItem = j;
        }
        else if (p_146030_4_ && p_146030_1_ != null)
        {
            int k = this.getFirstEmptyStack();

            if (k >= 0 && k < 9)
            {
                this.currentItem = k;
            }

            if (itemstack == null || !itemstack.isItemEnchantable() || this.getInventorySlotContainItemAndDamage(itemstack.getItem(), itemstack.getItemDamage()) != this.currentItem)
            {
                int l = this.getInventorySlotContainItemAndDamage(p_146030_1_, p_146030_2_);
                int i1;

                if (l >= 0)
                {
                    i1 = this.mainInventory[l].stackSize;
                    this.mainInventory[l] = this.mainInventory[this.currentItem];
                }
                else
                {
                    i1 = 1;
                }

                this.mainInventory[this.currentItem] = new ItemStack(p_146030_1_, i1, p_146030_2_);
            }
        }
    }

    public int func_174925_a(Item p_174925_1_, int p_174925_2_, int p_174925_3_, NBTTagCompound p_174925_4_)
    {
        int k = 0;
        int l;
        ItemStack itemstack;
        int i1;

        for (l = 0; l < this.mainInventory.length; ++l)
        {
            itemstack = this.mainInventory[l];

            if (itemstack != null && (p_174925_1_ == null || itemstack.getItem() == p_174925_1_) && (p_174925_2_ <= -1 || itemstack.getMetadata() == p_174925_2_) && (p_174925_4_ == null || CommandTestForBlock.func_175775_a(p_174925_4_, itemstack.getTagCompound(), true)))
            {
                i1 = p_174925_3_ <= 0 ? itemstack.stackSize : Math.min(p_174925_3_ - k, itemstack.stackSize);
                k += i1;

                if (p_174925_3_ != 0)
                {
                    this.mainInventory[l].stackSize -= i1;

                    if (this.mainInventory[l].stackSize == 0)
                    {
                        this.mainInventory[l] = null;
                    }

                    if (p_174925_3_ > 0 && k >= p_174925_3_)
                    {
                        return k;
                    }
                }
            }
        }

        for (l = 0; l < this.armorInventory.length; ++l)
        {
            itemstack = this.armorInventory[l];

            if (itemstack != null && (p_174925_1_ == null || itemstack.getItem() == p_174925_1_) && (p_174925_2_ <= -1 || itemstack.getMetadata() == p_174925_2_) && (p_174925_4_ == null || CommandTestForBlock.func_175775_a(p_174925_4_, itemstack.getTagCompound(), false)))
            {
                i1 = p_174925_3_ <= 0 ? itemstack.stackSize : Math.min(p_174925_3_ - k, itemstack.stackSize);
                k += i1;

                if (p_174925_3_ != 0)
                {
                    this.armorInventory[l].stackSize -= i1;

                    if (this.armorInventory[l].stackSize == 0)
                    {
                        this.armorInventory[l] = null;
                    }

                    if (p_174925_3_ > 0 && k >= p_174925_3_)
                    {
                        return k;
                    }
                }
            }
        }

        if (this.itemStack != null)
        {
            if (p_174925_1_ != null && this.itemStack.getItem() != p_174925_1_)
            {
                return k;
            }

            if (p_174925_2_ > -1 && this.itemStack.getMetadata() != p_174925_2_)
            {
                return k;
            }

            if (p_174925_4_ != null && !CommandTestForBlock.func_175775_a(p_174925_4_, this.itemStack.getTagCompound(), false))
            {
                return k;
            }

            l = p_174925_3_ <= 0 ? this.itemStack.stackSize : Math.min(p_174925_3_ - k, this.itemStack.stackSize);
            k += l;

            if (p_174925_3_ != 0)
            {
                this.itemStack.stackSize -= l;

                if (this.itemStack.stackSize == 0)
                {
                    this.itemStack = null;
                }

                if (p_174925_3_ > 0 && k >= p_174925_3_)
                {
                    return k;
                }
            }
        }

        return k;
    }

    @SideOnly(Side.CLIENT)
    public void changeCurrentItem(int p_70453_1_)
    {
        if (p_70453_1_ > 0)
        {
            p_70453_1_ = 1;
        }

        if (p_70453_1_ < 0)
        {
            p_70453_1_ = -1;
        }

        for (this.currentItem -= p_70453_1_; this.currentItem < 0; this.currentItem += 9)
        {
            ;
        }

        while (this.currentItem >= 9)
        {
            this.currentItem -= 9;
        }
    }

    private int storePartialItemStack(ItemStack p_70452_1_)
    {
        Item item = p_70452_1_.getItem();
        int i = p_70452_1_.stackSize;
        int j = this.storeItemStack(p_70452_1_);

        if (j < 0)
        {
            j = this.getFirstEmptyStack();
        }

        if (j < 0)
        {
            return i;
        }
        else
        {
            if (this.mainInventory[j] == null)
            {
                this.mainInventory[j] = new ItemStack(item, 0, p_70452_1_.getMetadata());

                if (p_70452_1_.hasTagCompound())
                {
                    this.mainInventory[j].setTagCompound((NBTTagCompound)p_70452_1_.getTagCompound().copy());
                }
            }

            int k = i;

            if (i > this.mainInventory[j].getMaxStackSize() - this.mainInventory[j].stackSize)
            {
                k = this.mainInventory[j].getMaxStackSize() - this.mainInventory[j].stackSize;
            }

            if (k > this.getInventoryStackLimit() - this.mainInventory[j].stackSize)
            {
                k = this.getInventoryStackLimit() - this.mainInventory[j].stackSize;
            }

            if (k == 0)
            {
                return i;
            }
            else
            {
                i -= k;
                this.mainInventory[j].stackSize += k;
                this.mainInventory[j].animationsToGo = 5;
                return i;
            }
        }
    }

    public void decrementAnimations()
    {
        for (int i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null)
            {
                this.mainInventory[i].updateAnimation(this.player.worldObj, this.player, i, this.currentItem == i);
            }
        }

        for (int i = 0; i < armorInventory.length; i++)
        {
            if (armorInventory[i] != null)
            {
                armorInventory[i].getItem().onArmorTick(player.worldObj, player, armorInventory[i]);
            }
        }
    }

    public boolean consumeInventoryItem(Item p_146026_1_)
    {
        int i = this.getInventorySlotContainItem(p_146026_1_);

        if (i < 0)
        {
            return false;
        }
        else
        {
            if (--this.mainInventory[i].stackSize <= 0)
            {
                this.mainInventory[i] = null;
            }

            return true;
        }
    }

    public boolean hasItem(Item p_146028_1_)
    {
        int i = this.getInventorySlotContainItem(p_146028_1_);
        return i >= 0;
    }

    public boolean addItemStackToInventory(final ItemStack p_70441_1_)
    {
        if (p_70441_1_ != null && p_70441_1_.stackSize != 0 && p_70441_1_.getItem() != null)
        {
            try
            {
                int i;

                if (p_70441_1_.isItemDamaged())
                {
                    i = this.getFirstEmptyStack();

                    if (i >= 0)
                    {
                        this.mainInventory[i] = ItemStack.copyItemStack(p_70441_1_);
                        this.mainInventory[i].animationsToGo = 5;
                        p_70441_1_.stackSize = 0;
                        return true;
                    }
                    else if (this.player.capabilities.isCreativeMode)
                    {
                        p_70441_1_.stackSize = 0;
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    do
                    {
                        i = p_70441_1_.stackSize;
                        p_70441_1_.stackSize = this.storePartialItemStack(p_70441_1_);
                    }
                    while (p_70441_1_.stackSize > 0 && p_70441_1_.stackSize < i);

                    if (p_70441_1_.stackSize == i && this.player.capabilities.isCreativeMode)
                    {
                        p_70441_1_.stackSize = 0;
                        return true;
                    }
                    else
                    {
                        return p_70441_1_.stackSize < i;
                    }
                }
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
                crashreportcategory.addCrashSection("Item ID", Integer.valueOf(Item.getIdFromItem(p_70441_1_.getItem())));
                crashreportcategory.addCrashSection("Item data", Integer.valueOf(p_70441_1_.getMetadata()));
                crashreportcategory.addCrashSectionCallable("Item name", new Callable()
                {
                    private static final String __OBFID = "CL_00001710";
                    public String call()
                    {
                        return p_70441_1_.getDisplayName();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        else
        {
            return false;
        }
    }

    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack[] aitemstack = this.mainInventory;

        if (index >= this.mainInventory.length)
        {
            aitemstack = this.armorInventory;
            index -= this.mainInventory.length;
        }

        if (aitemstack[index] != null)
        {
            ItemStack itemstack;

            if (aitemstack[index].stackSize <= count)
            {
                itemstack = aitemstack[index];
                aitemstack[index] = null;
                return itemstack;
            }
            else
            {
                itemstack = aitemstack[index].splitStack(count);

                if (aitemstack[index].stackSize == 0)
                {
                    aitemstack[index] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index)
    {
        ItemStack[] aitemstack = this.mainInventory;

        if (index >= this.mainInventory.length)
        {
            aitemstack = this.armorInventory;
            index -= this.mainInventory.length;
        }

        if (aitemstack[index] != null)
        {
            ItemStack itemstack = aitemstack[index];
            aitemstack[index] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack[] aitemstack = this.mainInventory;

        if (index >= aitemstack.length)
        {
            index -= aitemstack.length;
            aitemstack = this.armorInventory;
        }

        aitemstack[index] = stack;
    }

    public float getStrVsBlock(Block p_146023_1_)
    {
        float f = 1.0F;

        if (this.mainInventory[this.currentItem] != null)
        {
            f *= this.mainInventory[this.currentItem].getStrVsBlock(p_146023_1_);
        }

        return f;
    }

    public NBTTagList writeToNBT(NBTTagList p_70442_1_)
    {
        int i;
        NBTTagCompound nbttagcompound;

        for (i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null)
            {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.mainInventory[i].writeToNBT(nbttagcompound);
                p_70442_1_.appendTag(nbttagcompound);
            }
        }

        for (i = 0; i < this.armorInventory.length; ++i)
        {
            if (this.armorInventory[i] != null)
            {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)(i + 100));
                this.armorInventory[i].writeToNBT(nbttagcompound);
                p_70442_1_.appendTag(nbttagcompound);
            }
        }

        return p_70442_1_;
    }

    public void readFromNBT(NBTTagList p_70443_1_)
    {
        this.mainInventory = new ItemStack[36];
        this.armorInventory = new ItemStack[4];

        for (int i = 0; i < p_70443_1_.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = p_70443_1_.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

            if (itemstack != null)
            {
                if (j >= 0 && j < this.mainInventory.length)
                {
                    this.mainInventory[j] = itemstack;
                }

                if (j >= 100 && j < this.armorInventory.length + 100)
                {
                    this.armorInventory[j - 100] = itemstack;
                }
            }
        }
    }

    public int getSizeInventory()
    {
        return this.mainInventory.length + 4;
    }

    public ItemStack getStackInSlot(int index)
    {
        ItemStack[] aitemstack = this.mainInventory;

        if (index >= aitemstack.length)
        {
            index -= aitemstack.length;
            aitemstack = this.armorInventory;
        }

        return aitemstack[index];
    }

    public String getName()
    {
        return "container.inventory";
    }

    public boolean hasCustomName()
    {
        return false;
    }

    public IChatComponent getDisplayName()
    {
        return (IChatComponent)(this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean func_146025_b(Block p_146025_1_)
    {
        if (p_146025_1_.getMaterial().isToolNotRequired())
        {
            return true;
        }
        else
        {
            ItemStack itemstack = this.getStackInSlot(this.currentItem);
            return itemstack != null ? itemstack.canHarvestBlock(p_146025_1_) : false;
        }
    }

    public ItemStack armorItemInSlot(int p_70440_1_)
    {
        return this.armorInventory[p_70440_1_];
    }

    public int getTotalArmorValue()
    {
        int i = 0;

        for (int j = 0; j < this.armorInventory.length; ++j)
        {
            if (this.armorInventory[j] != null && this.armorInventory[j].getItem() instanceof ItemArmor)
            {
                int k = ((ItemArmor)this.armorInventory[j].getItem()).damageReduceAmount;
                i += k;
            }
        }

        return i;
    }

    public void damageArmor(float p_70449_1_)
    {
        p_70449_1_ /= 4.0F;

        if (p_70449_1_ < 1.0F)
        {
            p_70449_1_ = 1.0F;
        }

        for (int i = 0; i < this.armorInventory.length; ++i)
        {
            if (this.armorInventory[i] != null && this.armorInventory[i].getItem() instanceof ItemArmor)
            {
                this.armorInventory[i].damageItem((int)p_70449_1_, this.player);

                if (this.armorInventory[i].stackSize == 0)
                {
                    this.armorInventory[i] = null;
                }
            }
        }
    }

    public void dropAllItems()
    {
        int i;

        for (i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null)
            {
                this.player.dropItem(this.mainInventory[i], true, false);
                this.mainInventory[i] = null;
            }
        }

        for (i = 0; i < this.armorInventory.length; ++i)
        {
            if (this.armorInventory[i] != null)
            {
                this.player.dropItem(this.armorInventory[i], true, false);
                this.armorInventory[i] = null;
            }
        }
    }

    public void markDirty()
    {
        this.inventoryChanged = true;
    }

    public void setItemStack(ItemStack p_70437_1_)
    {
        this.itemStack = p_70437_1_;
    }

    public ItemStack getItemStack()
    {
        return this.itemStack;
    }

    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.player.isDead ? false : player.getDistanceSqToEntity(this.player) <= 64.0D;
    }

    public boolean hasItemStack(ItemStack p_70431_1_)
    {
        int i;

        for (i = 0; i < this.armorInventory.length; ++i)
        {
            if (this.armorInventory[i] != null && this.armorInventory[i].isItemEqual(p_70431_1_))
            {
                return true;
            }
        }

        for (i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null && this.mainInventory[i].isItemEqual(p_70431_1_))
            {
                return true;
            }
        }

        return false;
    }

    public void openInventory(EntityPlayer player) {}

    public void closeInventory(EntityPlayer player) {}

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    public void copyInventory(InventoryPlayer p_70455_1_)
    {
        int i;

        for (i = 0; i < this.mainInventory.length; ++i)
        {
            this.mainInventory[i] = ItemStack.copyItemStack(p_70455_1_.mainInventory[i]);
        }

        for (i = 0; i < this.armorInventory.length; ++i)
        {
            this.armorInventory[i] = ItemStack.copyItemStack(p_70455_1_.armorInventory[i]);
        }

        this.currentItem = p_70455_1_.currentItem;
    }

    public int getField(int id)
    {
        return 0;
    }

    public void setField(int id, int value) {}

    public int getFieldCount()
    {
        return 0;
    }

    public void clear()
    {
        int i;

        for (i = 0; i < this.mainInventory.length; ++i)
        {
            this.mainInventory[i] = null;
        }

        for (i = 0; i < this.armorInventory.length; ++i)
        {
            this.armorInventory[i] = null;
        }
    }
}