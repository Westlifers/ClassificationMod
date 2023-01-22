package top.yougi.classification.event;

import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.RandomStringUtils;
import top.yougi.classification.Classification;
import top.yougi.classification.networking.ModMessages;
import top.yougi.classification.networking.packet.VKeyPressedC2SPacket;
import top.yougi.classification.util.KeyBinding;

import java.util.ArrayList;
import java.util.List;

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
    }


    @Mod.EventBusSubscriber(modid = Classification.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvent {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.CLASS_GUI_KEY);
        }
    }
}
