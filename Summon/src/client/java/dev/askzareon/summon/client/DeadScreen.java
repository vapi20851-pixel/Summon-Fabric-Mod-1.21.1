package dev.askzareon.summon.client;

import dev.askzareon.summon.State;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DeadScreen extends Screen {
	public DeadScreen() {
		super(Component.literal(State.brokenName()));
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float d) {
		renderBackground(g, mx, my, d);
		g.drawCenteredString(font, State.brokenName(), width / 2, height / 2 - 20, 0xFF0000);
		g.drawCenteredString(font, State.brokenDesc(), width / 2, height / 2, 0x888888);
		g.drawCenteredString(font, Component.translatable("summon.broken.author", "askzAreon"), width / 2, height / 2 + 20, 0x555555);
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
}
