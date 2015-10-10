package net.minecraft.client.gui.inventory;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CreativeCrafting implements ICrafting
{
    private final Minecraft mc;
    private static final String __OBFID = "CL_00000751";

    public CreativeCrafting(Minecraft mc)
    {
        this.mc = mc;
    }

    public void sendContainerAndContentsToPlayer(Container containerToSend, List itemsList) {}

    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
    {
        this.mc.playerController.sendSlotPacket(stack, slotInd);
    }

    public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue) {}

    public void func_175173_a(Container p_175173_1_, IInventory p_175173_2_) {}
}