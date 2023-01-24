package top.yougi.classification.event;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import top.yougi.classification.Classification;
import top.yougi.classification.capability.ChestCapability;
import top.yougi.classification.capability.ChestCapabilityProvider;
import top.yougi.classification.capability.LevelCapabilityProvider;
import top.yougi.classification.commands.DeleteClassCommand;
import top.yougi.classification.commands.ListAllClassCommand;
import top.yougi.classification.commands.ShowDetailOfClassCommand;
import top.yougi.classification.libs.Libs;
import top.yougi.classification.networking.ModMessages;
import top.yougi.classification.networking.packet.SyncLevelDataS2CPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Classification.MODID)
public class ModEvents {

    // 为所有箱子在生成时绑定一个空classname
    @SubscribeEvent
    public static void onAttachCapabilitiesChest(AttachCapabilitiesEvent<BlockEntity> event) {
        if (!(event.getObject() instanceof ChestBlockEntity)) {
            return;
        }
        if (event.getObject() != null) {
            if (!event.getObject().getCapability(ChestCapabilityProvider.CHEST_CAPABILITY).isPresent()) {
                event.addCapability(new ResourceLocation(Classification.MODID, "properties"),
                        new ChestCapabilityProvider());
            }
        }
    }

    // 玩家放置箱子时调整capability
    @SubscribeEvent
    public static void onPlayerPlaceChest(BlockEvent.EntityPlaceEvent event) {
        BlockPos pos = event.getPos();
        Level level = event.getEntity().getLevel();
        BlockState state = event.getPlacedBlock();
        BlockEntity blockEntity = level.getBlockEntity(pos);

       if (state.getBlock() instanceof ChestBlock) {
           if (!Libs.isSingleChest(state)) {

               BlockEntity nearBlockEntity = Libs.getNearChestEntity(state, pos, level);

               if (nearBlockEntity.getCapability(ChestCapabilityProvider.CHEST_CAPABILITY).isPresent()) {
                   nearBlockEntity.getCapability(ChestCapabilityProvider.CHEST_CAPABILITY).ifPresent(cap -> {
                       blockEntity.getCapability(ChestCapabilityProvider.CHEST_CAPABILITY).ifPresent(cap_ -> {
                           cap_.setClassName(cap.getClassName());
                       });
                   });
               }
           }
       }
    }

    // 为世界绑定LevelCapability
    @SubscribeEvent
    public static void onAttachCapabilitiesLevel(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject() != null) {
            if (!event.getObject().getCapability(LevelCapabilityProvider.LEVEL_CAPABILITY).isPresent()) {
                event.addCapability(new ResourceLocation(Classification.MODID, "properties"),
                        new LevelCapabilityProvider());
            }
        }
    }

    // 注册Capability
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ChestCapability.class);
    }

    // 注册命令
    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        new ListAllClassCommand(event.getDispatcher());
        new ShowDetailOfClassCommand(event.getDispatcher());
        new DeleteClassCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            if (event.player.getRandom().nextFloat() < 0.005f) {
                ServerPlayer player = (ServerPlayer) event.player;
                ServerLevel level = player.getLevel();
                level.getCapability(LevelCapabilityProvider.LEVEL_CAPABILITY).ifPresent(cap -> {
                    List<String> classNames = new ArrayList<>();
                    for (Map.Entry<String, List<String>> entry: cap.getClassMap().entrySet()) {
                        classNames.add(entry.getKey());
                    }
                    ModMessages.sendToPlayer(new SyncLevelDataS2CPacket(classNames), player);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getLevel().getCapability(LevelCapabilityProvider.LEVEL_CAPABILITY).ifPresent(cap -> {
                    List<String> classNames = new ArrayList<>();
                    for (Map.Entry<String, List<String>> entry: cap.getClassMap().entrySet()) {
                        classNames.add(entry.getKey());
                    }
                    ModMessages.sendToPlayer(new SyncLevelDataS2CPacket(classNames), player);
                });
            }
        }
    }

}
