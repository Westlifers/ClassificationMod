package top.yougi.classification.networking.packet;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkEvent;
import top.yougi.classification.capability.ChestCapabilityProvider;
import top.yougi.classification.capability.LevelCapabilityProvider;
import top.yougi.classification.libs.Libs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ClassificationC2SPacket {

    public ClassificationC2SPacket() {

    }

    public ClassificationC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            ServerLevel level = player.getLevel();
            BlockPos playerPos = player.getOnPos();
            List<BlockEntity> blockEntities = Libs.getAllChestEntitiesInRange(playerPos, level);
            level.getCapability(LevelCapabilityProvider.LEVEL_CAPABILITY).ifPresent(capability -> {
                Map<String, List<String>> map = capability.getClassMap();


                for (BlockEntity blockEntity: blockEntities) {
                    blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(cap -> {
                        blockEntity.getCapability(ChestCapabilityProvider.CHEST_CAPABILITY).ifPresent(cap_ -> {

                            List<ItemStack> itemStacks = new ArrayList<>();
                            for (ItemStack itemStack: player.getInventory().items) {
                                if (!itemStack.isEmpty()) {
                                    itemStacks.add(itemStack);
                                }
                            }
                            for (ItemStack itemStack: itemStacks) {
                                if (!I18n.get(itemStack.getDescriptionId()).equals("??????")) {
                                    // ?????????????????????????????????????????????????????????????????????
                                    if (map.get(cap_.getClassName()).contains(itemStack.getDescriptionId())) {
                                        // ????????????????????????slot?????????????????????
                                        for (int i=0; i<(Libs.isSingleChest(blockEntity.getBlockState())?27:54); i++) {
                                            System.out.println(i);
                                            if (cap.getStackInSlot(i).isEmpty()) {
                                                // case1: ??????????????????????????????
                                                player.getInventory().setItem(player.getInventory().findSlotMatchingItem(itemStack), ItemStack.EMPTY);
                                                cap.insertItem(i, itemStack, false);
                                                break;
                                            }
                                            else {
                                                // case2: ???????????????????????????????????????????????????????????????????????????????????????
                                                if (cap.getStackInSlot(i).getItem() != itemStack.getItem()) {
                                                    continue;
                                                }
                                                int countAvailable = cap.getStackInSlot(i).getMaxStackSize() - cap.getStackInSlot(i).getCount();  // ??????????????????
                                                if (itemStack.getCount() <= countAvailable) {
                                                    // ??????????????????????????????
                                                    player.getInventory().setItem(player.getInventory().findSlotMatchingItem(itemStack), ItemStack.EMPTY);
                                                    cap.insertItem(i, itemStack, false);
                                                    break;
                                                }
                                                else {
                                                    // ????????????????????????????????????????????????????????????????????????
                                                    int w = player.getInventory().findSlotMatchingItem(itemStack);
                                                    itemStack.setCount(itemStack.getCount() - countAvailable);

                                                    player.getInventory().setItem(w, itemStack);
                                                    cap.getStackInSlot(i).setCount(itemStack.getMaxStackSize());
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        });
                    });
                }


            });
        });
        ctx.get().setPacketHandled(true);
    }
}
