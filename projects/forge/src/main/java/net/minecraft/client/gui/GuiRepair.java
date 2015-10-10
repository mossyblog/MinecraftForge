package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiRepair extends GuiContainer implements ICrafting
{
    private static final ResourceLocation anvilResource = new ResourceLocation("textures/gui/container/anvil.png");
    private ContainerRepair anvil;
    private GuiTextField nameField;
    private InventoryPlayer playerInventory;
    private static final String __OBFID = "CL_00000738";

    public GuiRepair(InventoryPlayer p_i45508_1_, World worldIn)
    {
        super(new ContainerRepair(p_i45508_1_, worldIn, Minecraft.getMinecraft().thePlayer));
        this.playerInventory = p_i45508_1_;
        this.anvil = (ContainerRepair)this.inventorySlots;
    }

    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.nameField = new GuiTextField(0, this.fontRendererObj, i + 62, j + 24, 103, 12);
        this.nameField.setTextColor(-1);
        this.nameField.setDisabledTextColour(-1);
        this.nameField.setEnableBackgroundDrawing(false);
        this.nameField.setMaxStringLength(40);
        this.inventorySlots.removeCraftingFromCrafters(this);
        this.inventorySlots.addCraftingToCrafters(this);
    }

    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        this.inventorySlots.removeCraftingFromCrafters(this);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.fontRendererObj.drawString(I18n.format("container.repair", new Object[0]), 60, 6, 4210752);

        if (this.anvil.maximumCost > 0)
        {
            int k = 8453920;
            boolean flag = true;
            String s = I18n.format("container.repair.cost", new Object[] {Integer.valueOf(this.anvil.maximumCost)});

            if (this.anvil.maximumCost >= 40 && !this.mc.thePlayer.capabilities.isCreativeMode)
            {
                s = I18n.format("container.repair.expensive", new Object[0]);
                k = 16736352;
            }
            else if (!this.anvil.getSlot(2).getHasStack())
            {
                flag = false;
            }
            else if (!this.anvil.getSlot(2).canTakeStack(this.playerInventory.player))
            {
                k = 16736352;
            }

            if (flag)
            {
                int l = -16777216 | (k & 16579836) >> 2 | k & -16777216;
                int i1 = this.xSize - 8 - this.fontRendererObj.getStringWidth(s);
                byte b0 = 67;

                if (this.fontRendererObj.getUnicodeFlag())
                {
                    drawRect(i1 - 3, b0 - 2, this.xSize - 7, b0 + 10, -16777216);
                    drawRect(i1 - 2, b0 - 1, this.xSize - 8, b0 + 9, -12895429);
                }
                else
                {
                    this.fontRendererObj.drawString(s, i1, b0 + 1, l);
                    this.fontRendererObj.drawString(s, i1 + 1, b0, l);
                    this.fontRendererObj.drawString(s, i1 + 1, b0 + 1, l);
                }

                this.fontRendererObj.drawString(s, i1, b0, k);
            }
        }

        GlStateManager.enableLighting();
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (this.nameField.textboxKeyTyped(typedChar, keyCode))
        {
            this.renameItem();
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    private void renameItem()
    {
        String s = this.nameField.getText();
        Slot slot = this.anvil.getSlot(0);

        if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && s.equals(slot.getStack().getDisplayName()))
        {
            s = "";
        }

        this.anvil.updateItemName(s);
        this.mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("MC|ItemName", (new PacketBuffer(Unpooled.buffer())).writeString(s)));
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.nameField.drawTextBox();
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(anvilResource);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(k + 59, l + 20, 0, this.ySize + (this.anvil.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

        if ((this.anvil.getSlot(0).getHasStack() || this.anvil.getSlot(1).getHasStack()) && !this.anvil.getSlot(2).getHasStack())
        {
            this.drawTexturedModalRect(k + 99, l + 45, this.xSize, 0, 28, 21);
        }
    }

    public void sendContainerAndContentsToPlayer(Container containerToSend, List itemsList)
    {
        this.sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
    }

    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
    {
        if (slotInd == 0)
        {
            this.nameField.setText(stack == null ? "" : stack.getDisplayName());
            this.nameField.setEnabled(stack != null);

            if (stack != null)
            {
                this.renameItem();
            }
        }
    }

    public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue) {}

    public void func_175173_a(Container p_175173_1_, IInventory p_175173_2_) {}
}