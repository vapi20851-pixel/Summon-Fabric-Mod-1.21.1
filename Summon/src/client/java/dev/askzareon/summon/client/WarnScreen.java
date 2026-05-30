package dev.askzareon.summon.client;

import dev.askzareon.summon.State;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

public class WarnScreen extends Screen {
	private final Screen next;

	public WarnScreen(Screen next) {
		super(Component.translatable("summon.warning.title"));
		this.next = next;
	}

	@Override
	protected void init() {
		int cx = width / 2;
		int cy = height / 2;
		addRenderableWidget(Button.builder(Component.translatable("summon.warning.accept"), b -> {
			State.acceptWarn();
			minecraft.setScreen(next);
		}).bounds(cx - 100, cy + 60, 200, 20).build());
		addRenderableWidget(Button.builder(Component.translatable("summon.warning.cancel"), b -> {
			minecraft.setScreen(new TitleScreen());
		}).bounds(cx - 100, cy + 90, 200, 20).build());
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float d) {
		renderBackground(g, mx, my, d);
		g.drawCenteredString(font, title, width / 2, height / 2 - 60, 0xFF5555);
		g.drawCenteredString(font, Component.translatable("summon.warning.line1"), width / 2, height / 2 - 30, 0xFFFFFF);
		g.drawCenteredString(font, Component.translatable("summon.warning.line2"), width / 2, height / 2 - 15, 0xFFFFFF);
		g.drawCenteredString(font, Component.translatable("summon.warning.line3"), width / 2, height / 2, 0xAAAAAA);
		g.drawCenteredString(font, Component.translatable("summon.warning.wip"), width / 2, height / 2 + 18, 0x888888);
		super.render(g, mx, my, d);
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
}
