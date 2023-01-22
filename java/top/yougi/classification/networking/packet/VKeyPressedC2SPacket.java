package top.yougi.classification.networking.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import top.yougi.classification.client.gui.ClassManagerMenu;

import java.util.function.Supplier;

public class VKeyPressedC2SPacket {

    public VKeyPressedC2SPacket() {

    }

    public VKeyPressedC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            assert player != null;
            ServerLevel level = player.getLevel();

            BlockPos pos = player.getOnPos();
            NetworkHooks.openScreen(player, new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.literal("ClassManager");
                }

                @Override
                public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                    return new ClassManagerMenu(id, inventory,null);
                }
            }, pos);
        });
        ctx.get().setPacketHandled(true);
    }
}
