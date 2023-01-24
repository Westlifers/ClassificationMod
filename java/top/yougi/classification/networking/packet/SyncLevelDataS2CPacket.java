package top.yougi.classification.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import top.yougi.classification.client.ClientLevelData;

import java.util.List;
import java.util.function.Supplier;

public class SyncLevelDataS2CPacket {
    private List<String> classNames;

    public SyncLevelDataS2CPacket(List<String> classNames) {
        this.classNames = classNames;
    }

    public SyncLevelDataS2CPacket(FriendlyByteBuf buf) {
        this.classNames = buf.readList(FriendlyByteBuf::readUtf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(this.classNames, FriendlyByteBuf::writeUtf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevelData.setClassNames(this.classNames);
        });
        ctx.get().setPacketHandled(true);
        return true;
    }
}
