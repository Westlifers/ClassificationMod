package top.yougi.classification.networking.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.RandomStringUtils;
import top.yougi.classification.capability.ChestCapabilityProvider;

import java.util.Objects;
import java.util.function.Supplier;

public class RightClickedBlockEntityC2SPacket {
    private BlockPos blockPos;

    public RightClickedBlockEntityC2SPacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public RightClickedBlockEntityC2SPacket(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            assert player != null;
            ServerLevel level = player.getLevel();
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            assert blockEntity != null;

            blockEntity.getCapability(ChestCapabilityProvider.CHEST_CAPABILITY).ifPresent(
                cap -> {
                    if (Objects.equals(cap.getClassName(), "")) {  // cap.getClassName() == ""
                        cap.setClassName(RandomStringUtils.randomAlphabetic(5));
                    }
                }
            );
        });
        ctx.get().setPacketHandled(true);
    }
}
