
package top.yougi.classification.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import top.yougi.classification.client.ClientLevelData;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AddClassScreen extends AbstractContainerScreen<AddClassMenu> {
	private final static HashMap<String, Object> guistate = AddClassMenu.guistate;
	private final Level level;
	private final BlockPos pos;
	private final Player entity;
	EditBox ClassAdded;

	public AddClassScreen(AddClassMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.level = container.level;
		this.pos = container.pos;
		this.entity = container.entity;
		this.imageWidth = 170;
		this.imageHeight = 80;
	}

	private static final ResourceLocation texture = new ResourceLocation("classification:textures/screens/add_class.png");

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderTooltip(ms, mouseX, mouseY);
		ClassAdded.render(ms, mouseX, mouseY, partialTicks);
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
		if (ClassAdded.isFocused())
			return ClassAdded.keyPressed(key, b, c);
		return super.keyPressed(key, b, c);
	}

	@Override
	public void containerTick() {
		super.containerTick();
		ClassAdded.tick();
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		this.font.draw(poseStack, "设置分类", 65, 8, -12829636);
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
		this.addRenderableWidget(new Button(this.leftPos + 24, this.topPos + 51, 35, 20, Component.literal("验证"), e -> {
			String className = this.ClassAdded.getValue();
			if (isValid(className)) {
				this.ClassAdded.setTextColor(0x00ff00);
			}
			else {
				this.ClassAdded.setTextColor(0xff0000);
			}
		}));
		this.addRenderableWidget(new Button(this.leftPos + 108, this.topPos + 51, 35, 20, Component.literal("确认"), e -> {
		}));

		ClassAdded = new EditBox(this.font, this.leftPos + 24, this.topPos + 23, 120, 20, Component.literal("DefaulClass")) {
			{
				setSuggestion("DefaultClass");
			}

			@Override
			public void insertText(String text) {
				super.insertText(text);
				if (getValue().isEmpty())
					setSuggestion("DefaultClass");
				else
					setSuggestion(null);
			}

			@Override
			public void moveCursorTo(int pos) {
				super.moveCursorTo(pos);
				if (getValue().isEmpty())
					setSuggestion("DefaultClass");
				else
					setSuggestion(null);
			}
		};
		guistate.put("text:ClassAdded", ClassAdded);
		ClassAdded.setMaxLength(32767);
		this.addWidget(this.ClassAdded);
	}

	private boolean isValid(String className) {
		List<String> classNames = ClientLevelData.getClassNames();
		boolean valid = false;
		for (String name: classNames) {
			if (Objects.equals(name, className)) {
				valid = true;
				break;
			}
		}
		System.out.println(valid);
		return valid;
	}
}
