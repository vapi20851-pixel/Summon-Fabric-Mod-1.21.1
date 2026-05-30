package dev.askzareon.summon.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class Ui {
	public static void hide(Screen s, Component... labels) {
		for (GuiEventListener w : s.children()) {
			if (w instanceof Button b) {
				for (Component l : labels) {
					if (b.getMessage().getString().equals(l.getString())) {
						b.visible = false;
						b.active = false;
					}
				}
			}
		}
	}

	public static void hideLike(Screen s, String... bits) {
		for (GuiEventListener w : s.children()) {
			if (w instanceof Button b) {
				String t = b.getMessage().getString().toLowerCase();
				for (String bit : bits) {
					if (t.contains(bit.toLowerCase())) {
						b.visible = false;
						b.active = false;
					}
				}
			}
		}
	}
}
