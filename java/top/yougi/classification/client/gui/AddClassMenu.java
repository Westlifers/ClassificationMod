package top.yougi.classification.client.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import top.yougi.classification.init.ClassificationModMenus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AddClassMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
	public final static HashMap<String, Object> guistate = new HashMap<>();
	public final Level level;
	public final Player entity;
	public BlockPos pos;
	private IItemHandler internal;
	private final Map<Integer, Slot> customSlots = new HashMap<>();
	private boolean bound = false;

	public AddClassMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		super(ClassificationModMenus.ADD_CLASS.get(), id);
		this.entity = inv.player;
		this.level = inv.player.level;
		this.internal = new ItemStackHandler(0);
		if (extraData != null) {
			this.pos = extraData.readBlockPos();
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		return ItemStack.EMPTY;
	}

	public Map<Integer, Slot> get() {
		return customSlots;
	}
}
