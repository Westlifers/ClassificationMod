package top.yougi.classification.libs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;

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
}
