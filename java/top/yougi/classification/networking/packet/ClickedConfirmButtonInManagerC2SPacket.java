package top.yougi.classification.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;
import top.yougi.classification.capability.LevelCapabilityProvider;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ClickedConfirmButtonInManagerC2SPacket {
    private List<String> items;
    private String ClassName;

    public ClickedConfirmButtonInManagerC2SPacket(List<String> items, String ClassName) {
        this.ClassName = ClassName;
        this.items = items;
    }

    public ClickedConfirmButtonInManagerC2SPacket(FriendlyByteBuf buf) {
        this.ClassName = buf.readUtf();
        this.items = buf.readList(FriendlyByteBuf::readUtf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.ClassName);
        buf.writeCollection(this.items, FriendlyByteBuf::writeUtf);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerLevel level = ctx.get().getSender().getLevel();
            level.getCapability(LevelCapabilityProvider.LEVEL_CAPABILITY).ifPresent(
                cap -> {
                    Map<String, List<String>> map = cap.getClassMap();
                    map.put(this.ClassName, this.items);
                    cap.setClassMap(map);
                }
            );
        });
        ctx.get().setPacketHandled(true);
    }
}
