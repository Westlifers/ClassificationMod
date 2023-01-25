package top.yougi.classification.libs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import top.yougi.classification.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

public class Libs {
    public static BlockEntity getNearChestEntity(BlockState state, BlockPos pos, Level level) {
        ChestType type = state.getValue(BlockStateProperties.CHEST_TYPE);
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockPos nearPos;
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

        return level.getBlockEntity(nearPos);
    }

    public static Boolean isSingleChest(BlockState state) {
        ChestType type = state.getValue(BlockStateProperties.CHEST_TYPE);
        return type.equals(ChestType.SINGLE);
    }

    public static List<BlockEntity> getAllChestEntitiesInRange(BlockPos playerPos, ServerLevel level) {
        int range = ModConfig.searchRange.get();
        List<BlockEntity> blockEntities = new ArrayList<>();
        for (int x = (int) Math.floor(playerPos.getX()-range); x < (int) Math.ceil(playerPos.getX()+range); x++) {
            for (int y = (int) Math.floor(playerPos.getY()-range); y < (int) Math.ceil(playerPos.getY()+range); y++) {
                for (int z = (int) Math.floor(playerPos.getZ()-range); z < (int) Math.ceil(playerPos.getZ()+range); z++) {
                    if (level.getBlockEntity(new BlockPos(x, y, z)) instanceof ChestBlockEntity) {
                        blockEntities.add(level.getBlockEntity(new BlockPos(x, y, z)));
                    }
                }
            }
        }
        // 要把相邻箱子给去掉，因为相连的箱子的block entity是同一个
        List<BlockEntity> finalBlockEntities = new ArrayList<>();
        for (BlockEntity entity: blockEntities) {
            if (!Libs.isSingleChest(entity.getBlockState())) {
                if (!finalBlockEntities.contains(Libs.getNearChestEntity(entity.getBlockState(), entity.getBlockPos(), level))) {
                    finalBlockEntities.add(entity);
                }
            }
            else {
                finalBlockEntities.add(entity);
            }
        }
        return finalBlockEntities;
    }
}
