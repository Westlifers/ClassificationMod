package top.yougi.classification.networking.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import top.yougi.classification.capability.ChestCapabilityProvider;
import top.yougi.classification.libs.Libs;

import java.util.function.Supplier;

public class ClickedConfirmButtonInChestC2SPacket {
    private String className;
    private BlockPos pos;

    public ClickedConfirmButtonInChestC2SPacket(String className, BlockPos pos) {
        this.className = className;
        this.pos = pos;
    }

    public ClickedConfirmButtonInChestC2SPacket(FriendlyByteBuf buf) {
        this.className = buf.readUtf();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.className);
        buf.writeBlockPos(this.pos);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            ServerLevel level = player.getLevel();
            BlockState state = level.getBlockState(this.pos);
            BlockEntity blockEntity = level.getBlockEntity(this.pos);

            blockEntity.getCapability(ChestCapabilityProvider.CHEST_CAPABILITY).ifPresent(cap -> {
                cap.setClassName(this.className);
            });

            if (state.getBlock() instanceof ChestBlock) {
                if (!Libs.isSingleChest(state)) {

                    BlockEntity nearBlockEntity = Libs.getNearChestEntity(state, this.pos, level);

                    if (nearBlockEntity != null && nearBlockEntity.getCapability(ChestCapabilityProvider.CHEST_CAPABILITY).isPresent()) {
                        nearBlockEntity.getCapability(ChestCapabilityProvider.CHEST_CAPABILITY).ifPresent(cap -> {
                            cap.setClassName(this.className);
                        });
                    }
                }
            }

        });
        ctx.get().setPacketHandled(true);
    }
}
