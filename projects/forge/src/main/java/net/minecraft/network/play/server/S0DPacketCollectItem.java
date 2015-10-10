package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S0DPacketCollectItem implements Packet
{
    private int field_149357_a;
    private int field_149356_b;
    private static final String __OBFID = "CL_00001339";

    public S0DPacketCollectItem() {}

    public S0DPacketCollectItem(int p_i45232_1_, int p_i45232_2_)
    {
        this.field_149357_a = p_i45232_1_;
        this.field_149356_b = p_i45232_2_;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149357_a = buf.readVarIntFromBuffer();
        this.field_149356_b = buf.readVarIntFromBuffer();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.field_149357_a);
        buf.writeVarIntToBuffer(this.field_149356_b);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleCollectItem(this);
    }

    @SideOnly(Side.CLIENT)
    public int func_149354_c()
    {
        return this.field_149357_a;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public int func_149353_d()
    {
        return this.field_149356_b;
    }
}