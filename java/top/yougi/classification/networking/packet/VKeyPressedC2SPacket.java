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
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.RandomStringUtils;
import top.yougi.classification.capability.LevelCapabilityProvider;
import top.yougi.classification.client.gui.ClassManagerMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
//            NetworkHooks.openScreen(player, new MenuProvider() {
//                @Override
//                public Component getDisplayName() {
//                    return Component.literal("ClassManager");
//                }
//
//                @Override
//                public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
//                    return new ClassManagerMenu(id, inventory,null);
//                }
//            }, pos);
            // 临时测试
            String Classname = "Classname" + RandomStringUtils.randomAlphabetic(5);
            List<String> items = new ArrayList<>();
            for (int i=0; i<9; i++){
                items.add(RandomStringUtils.randomAlphabetic(2));
            }
            level.getCapability(LevelCapabilityProvider.LEVEL_CAPABILITY).ifPresent(cap -> {
                Map<String, List<String>> map = cap.getClassMap();
                map.put(Classname, items);
                cap.setClassMap(map);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
