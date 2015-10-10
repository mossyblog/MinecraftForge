package net.minecraft.inventory;

import java.util.List;
import net.minecraft.item.ItemStack;

public interface ICrafting
{
    void sendContainerAndContentsToPlayer(Container containerToSend, List itemsList);

    void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack);

    void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue);

    void func_175173_a(Container p_175173_1_, IInventory p_175173_2_);
}