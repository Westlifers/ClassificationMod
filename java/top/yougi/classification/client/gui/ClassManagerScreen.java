
package top.yougi.classification.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.yougi.classification.networking.ModMessages;
import top.yougi.classification.networking.packet.ClickedConfirmButtonC2SPacket;

import java.util.*;

public class ClassManagerScreen extends AbstractContainerScreen<ClassManagerMenu> {
	private final static HashMap<String, Object> guistate = ClassManagerMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	EditBox ClassName;

	public ClassManagerScreen(ClassManagerMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 210;
		this.imageHeight = 180;
	}

	private static final ResourceLocation texture = new ResourceLocation("classification:textures/screens/class_manager.png");

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderTooltip(ms, mouseX, mouseY);
		ClassName.render(ms, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void renderBg(PoseStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShaderTexture(0, texture);
		this.blit(ms, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		if (ClassName.isFocused())
			return ClassName.keyPressed(key, b, c);
		return super.keyPressed(key, b, c);
	}

	@Override
	public void containerTick() {
		super.containerTick();
		ClassName.tick();
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		this.font.draw(poseStack, "\u6DFB\u52A0\u5206\u7C7B", 93, 13, -12829636);
	}

	@Override
	public void onClose() {
		super.onClose();
		Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
	}

	@Override
	public void init() {
		super.init();
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
		ClassName = new EditBox(this.font, this.leftPos + 11, this.topPos + 34, 120, 20, Component.literal("DefaultName")) {
			{
				setSuggestion("DefaultName");
			}

			@Override
			public void insertText(String text) {
				super.insertText(text);
				if (getValue().isEmpty())
					setSuggestion("DefaultName");
				else
					setSuggestion(null);
			}

			@Override
			public void moveCursorTo(int pos) {
				super.moveCursorTo(pos);
				if (getValue().isEmpty())
					setSuggestion("DefaultName");
				else
					setSuggestion(null);
			}
		};
		guistate.put("text:ClassName", ClassName);
		ClassName.setMaxLength(32767);
		this.addWidget(this.ClassName);
		this.addRenderableWidget(new Button(this.leftPos + 135, this.topPos + 34, 61, 20, Component.literal("确认"),
				e -> {
					// 获取GUI上的数据
					List<String> items = new ArrayList<>();
					for (int i=0; i<9; i++) {
						Item item = this.getMenu().getSlot(i).getItem().getItem();
						if (item != ItemStack.EMPTY.getItem()) {
							items.add(item.getDescriptionId());
						}
					}
					// items去重
					Set<String> set = new HashSet<>(items);
					List<String> items_ = new ArrayList<>(set);
					String className = this.ClassName.getValue();
					// 发包
					ModMessages.sendToServer(new ClickedConfirmButtonC2SPacket(items_, className));
					// 关闭
					this.minecraft.player.closeContainer();
				}
		));
	}
}
