package top.yougi.classification.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import top.yougi.classification.Classification;
import top.yougi.classification.networking.packet.ClickChestWhenSneakingC2SPacket;
import top.yougi.classification.networking.packet.ClickedConfirmButtonC2SPacket;
import top.yougi.classification.networking.packet.RightClickedBlockEntityC2SPacket;
import top.yougi.classification.networking.packet.VKeyPressedC2SPacket;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id(){
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Classification.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(VKeyPressedC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(VKeyPressedC2SPacket::new)
                .encoder(VKeyPressedC2SPacket::toBytes)
                .consumerMainThread(VKeyPressedC2SPacket::handle)
                .add();

        net.messageBuilder(RightClickedBlockEntityC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RightClickedBlockEntityC2SPacket::new)
                .encoder(RightClickedBlockEntityC2SPacket::toBytes)
                .consumerMainThread(RightClickedBlockEntityC2SPacket::handle)
                .add();

        net.messageBuilder(ClickedConfirmButtonC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ClickedConfirmButtonC2SPacket::new)
                .encoder(ClickedConfirmButtonC2SPacket::toBytes)
                .consumerMainThread(ClickedConfirmButtonC2SPacket::handle)
                .add();

        net.messageBuilder(ClickChestWhenSneakingC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ClickChestWhenSneakingC2SPacket::new)
                .encoder(ClickChestWhenSneakingC2SPacket::toBytes)
                .consumerMainThread(ClickChestWhenSneakingC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
