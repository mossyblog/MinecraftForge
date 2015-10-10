package net.minecraft.item;

import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.block.BlockDispenser;
import net.minecraft.command.IEntitySelector;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmor extends Item
{
    private static final int[] maxDamageArray = new int[] {11, 16, 15, 13};
    public static final String[] EMPTY_SLOT_NAMES = new String[] {"minecraft:items/empty_armor_slot_helmet", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_boots"};
    private static final IBehaviorDispenseItem dispenserBehavior = new BehaviorDefaultDispenseItem()
    {
        private static final String __OBFID = "CL_00001767";
        protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
        {
            BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
            int i = blockpos.getX();
            int j = blockpos.getY();
            int k = blockpos.getZ();
            AxisAlignedBB axisalignedbb = new AxisAlignedBB((double)i, (double)j, (double)k, (double)(i + 1), (double)(j + 1), (double)(k + 1));
            List list = source.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, Predicates.and(IEntitySelector.NOT_SPECTATING, new IEntitySelector.ArmoredMob(stack)));

            if (list.size() > 0)
            {
                EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(0);
                int l = 0;// Forge: We fix the indexes. Mojang Stop hard coding this!
                int i1 = EntityLiving.getArmorPosition(stack);
                ItemStack itemstack1 = stack.copy();
                itemstack1.stackSize = 1;
                entitylivingbase.setCurrentItemOrArmor(i1 - l, itemstack1);

                if (entitylivingbase instanceof EntityLiving)
                {
                    ((EntityLiving)entitylivingbase).setEquipmentDropChance(i1, 2.0F);
                }

                --stack.stackSize;
                return stack;
            }
            else
            {
                return super.dispenseStack(source, stack);
            }
        }
    };
    public final int armorType;
    public final int damageReduceAmount;
    public final int renderIndex;
    private final ItemArmor.ArmorMaterial material;
    private static final String __OBFID = "CL_00001766";

    public ItemArmor(ItemArmor.ArmorMaterial material, int renderIndex, int armorType)
    {
        this.material = material;
        this.armorType = armorType;
        this.renderIndex = renderIndex;
        this.damageReduceAmount = material.getDamageReductionAmount(armorType);
        this.setMaxDamage(material.getDurability(armorType));
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabCombat);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserBehavior);
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
        if (renderPass > 0)
        {
            return 16777215;
        }
        else
        {
            int j = this.getColor(stack);

            if (j < 0)
            {
                j = 16777215;
            }

            return j;
        }
    }

    public int getItemEnchantability()
    {
        return this.material.getEnchantability();
    }

    public ItemArmor.ArmorMaterial getArmorMaterial()
    {
        return this.material;
    }

    public boolean hasColor(ItemStack stack)
    {
        return this.material != ItemArmor.ArmorMaterial.LEATHER ? false : (!stack.hasTagCompound() ? false : (!stack.getTagCompound().hasKey("display", 10) ? false : stack.getTagCompound().getCompoundTag("display").hasKey("color", 3)));
    }

    public int getColor(ItemStack stack)
    {
        if (this.material != ItemArmor.ArmorMaterial.LEATHER)
        {
            return -1;
        }
        else
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound();

            if (nbttagcompound != null)
            {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

                if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3))
                {
                    return nbttagcompound1.getInteger("color");
                }
            }

            return 10511680;
        }
    }

    public void removeColor(ItemStack stack)
    {
        if (this.material == ItemArmor.ArmorMaterial.LEATHER)
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound();

            if (nbttagcompound != null)
            {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

                if (nbttagcompound1.hasKey("color"))
                {
                    nbttagcompound1.removeTag("color");
                }
            }
        }
    }

    public void setColor(ItemStack stack, int color)
    {
        if (this.material != ItemArmor.ArmorMaterial.LEATHER)
        {
            throw new UnsupportedOperationException("Can\'t dye non-leather!");
        }
        else
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound();

            if (nbttagcompound == null)
            {
                nbttagcompound = new NBTTagCompound();
                stack.setTagCompound(nbttagcompound);
            }

            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

            if (!nbttagcompound.hasKey("display", 10))
            {
                nbttagcompound.setTag("display", nbttagcompound1);
            }

            nbttagcompound1.setInteger("color", color);
        }
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return this.material.getRepairItem() == repair.getItem() ? true : super.getIsRepairable(toRepair, repair);
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        int i = EntityLiving.getArmorPosition(itemStackIn) - 1;
        ItemStack itemstack1 = playerIn.getCurrentArmor(i);

        if (itemstack1 == null)
        {
            playerIn.setCurrentItemOrArmor(i + 1, itemStackIn.copy()); //Forge: Vanilla bug fix associated with fixed setCurrentItemOrArmor indexs for players.
            itemStackIn.stackSize = 0;
        }

        return itemStackIn;
    }

    public static enum ArmorMaterial
    {
        LEATHER("leather", 5, new int[]{1, 3, 2, 1}, 15),
        CHAIN("chainmail", 15, new int[]{2, 5, 4, 1}, 12),
        IRON("iron", 15, new int[]{2, 6, 5, 2}, 9),
        GOLD("gold", 7, new int[]{2, 5, 3, 1}, 25),
        DIAMOND("diamond", 33, new int[]{3, 8, 6, 3}, 10);
        private final String name;
        private final int maxDamageFactor;
        private final int[] damageReductionAmountArray;
        private final int enchantability;

        private static final String __OBFID = "CL_00001768";

        //Added by forge for custom Armor materials.
        public Item customCraftingMaterial = null;

        private ArmorMaterial(String name, int p_i45789_4_, int[] reductionAmounts, int enchantability)
        {
            this.name = name;
            this.maxDamageFactor = p_i45789_4_;
            this.damageReductionAmountArray = reductionAmounts;
            this.enchantability = enchantability;
        }

        public int getDurability(int armorType)
        {
            return ItemArmor.maxDamageArray[armorType] * this.maxDamageFactor;
        }

        public int getDamageReductionAmount(int armorType)
        {
            return this.damageReductionAmountArray[armorType];
        }

        public int getEnchantability()
        {
            return this.enchantability;
        }

        public Item getRepairItem()
        {
            switch (this)
            {
                case LEATHER: return Items.leather;
                case CHAIN:   return Items.iron_ingot;
                case GOLD:    return Items.gold_ingot;
                case IRON:    return Items.iron_ingot;
                case DIAMOND: return Items.diamond;
                default:      return customCraftingMaterial;
            }
        }

        @SideOnly(Side.CLIENT)
        public String getName()
        {
            return this.name;
        }
    }
}