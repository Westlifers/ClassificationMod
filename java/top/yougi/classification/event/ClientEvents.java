package top.yougi.classification.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.yougi.classification.Classification;
import top.yougi.classification.networking.ModMessages;
import top.yougi.classification.networking.packet.ClassificationC2SPacket;
import top.yougi.classification.networking.packet.ClickChestWhenSneakingWithMainHandEmptyC2SPacket;
import top.yougi.classification.networking.packet.VKeyPressedC2SPacket;
import top.yougi.classification.util.KeyBinding;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = Classification.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        // 按V键示例
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (KeyBinding.CLASS_GUI_KEY.consumeClick()) {
                // 发送一个相关的packet
                ModMessages.sendToServer(new VKeyPressedC2SPacket());
            }
        }

        // 空手潜行右键箱子打开分类设置页面
        @SubscribeEvent
        public static void onPlayerRightClickedChest(PlayerInteractEvent.RightClickBlock event) {
            BlockPos pos = event.getPos();
            BlockEntity entity = event.getLevel().getBlockEntity(pos);
            Player player = event.getEntity();
            if (entity != null && player.isShiftKeyDown() && player.getMainHandItem().isEmpty()) {
                ModMessages.sendToServer(new ClickChestWhenSneakingWithMainHandEmptyC2SPacket(pos));
            }
        }

        // GUI内按键分类
        @SubscribeEvent
        public static void onPlayerTappedVKeyInScreen(ScreenEvent.KeyPressed event) {
            if (event.getKeyCode() == 86) {
                ModMessages.sendToServer(new ClassificationC2SPacket());
            }
        }
    }


    @Mod.EventBusSubscriber(modid = Classification.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvent {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.CLASS_GUI_KEY);
        }
    }
}
