package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S02PacketChat implements Packet
{
    private IChatComponent chatComponent;
    private byte field_179842_b;
    private static final String __OBFID = "CL_00001289";

    public S02PacketChat() {}

    public S02PacketChat(IChatComponent component)
    {
        this(component, (byte)1);
    }

    public S02PacketChat(IChatComponent p_i45986_1_, byte p_i45986_2_)
    {
        this.chatComponent = p_i45986_1_;
        this.field_179842_b = p_i45986_2_;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.chatComponent = buf.readChatComponent();
        this.field_179842_b = buf.readByte();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeChatComponent(this.chatComponent);
        buf.writeByte(this.field_179842_b);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleChat(this);
    }

    @SideOnly(Side.CLIENT)
    public IChatComponent func_148915_c()
    {
        return this.chatComponent;
    }

    public boolean isChat()
    {
        return this.field_179842_b == 1 || this.field_179842_b == 2;
    }

    @SideOnly(Side.CLIENT)
    public byte func_179841_c()
    {
        return this.field_179842_b;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}