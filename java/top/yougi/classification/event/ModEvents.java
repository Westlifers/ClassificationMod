package top.yougi.classification.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import top.yougi.classification.Classification;
import top.yougi.classification.capability.ChestCapability;
import top.yougi.classification.capability.ChestCapabilityProvider;
import top.yougi.classification.capability.LevelCapabilityProvider;
import top.yougi.classification.commands.DeleteClassCommand;
import top.yougi.classification.commands.ListAllClassCommand;
import top.yougi.classification.commands.ShowDetailOfClassCommand;

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
           ChestType type = state.getValue(BlockStateProperties.CHEST_TYPE);
           Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

           BlockPos nearPos;
           if (!type.equals(ChestType.SINGLE)) {
               if (type.equals(ChestType.LEFT)) {
                   if (facing.equals(Direction.NORTH)) {
                       nearPos = pos.offset(1, 0, 0);
                   }
                   else if (facing.equals(Direction.SOUTH)) {
                       nearPos = pos.offset(-1, 0, 0);
                   }
                   else if (facing.equals(Direction.EAST)) {
                       nearPos = pos.offset(0, 0, 1);
                   }
                   else {
                       nearPos = pos.offset(0, 0, -1);
                   }
               }
               else {
                   if (facing.equals(Direction.NORTH)) {
                       nearPos = pos.offset(-1, 0, 0);
                   }
                   else if (facing.equals(Direction.SOUTH)) {
                       nearPos = pos.offset(1, 0, 0);
                   }
                   else if (facing.equals(Direction.EAST)) {
                       nearPos = pos.offset(0, 0, -1);
                   }
                   else {
                       nearPos = pos.offset(0, 0, 1);
                   }
               }

               BlockEntity nearBlockEntity = level.getBlockEntity(nearPos);
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

}
