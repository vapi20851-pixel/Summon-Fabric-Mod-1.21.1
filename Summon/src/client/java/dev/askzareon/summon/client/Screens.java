package dev.askzareon.summon.client;

import dev.askzareon.summon.Sessions;
import dev.askzareon.summon.State;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;

public final class Screens {
	private Screens() {
	}

	public static void hook() {
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			if (State.dead()) {
				return;
			}

			if (State.worldGone() && screen instanceof SelectWorldScreen) {
				client.setScreen(new TitleScreen());
				client.gui.setOverlayMessage(Component.translatable("summon.world.corrupted"), false);
				return;
			}

			if (screen instanceof CreateWorldScreen) {
				client.setScreen(new SelectWorldScreen(new TitleScreen()));
				return;
			}

			if (screen instanceof ChatScreen) {
				client.setScreen(null);
				return;
			}

			if (screen instanceof TitleScreen title) {
				Ui.hide(title,
						Component.translatable("menu.online"),
						Component.translatable("menu.quit"),
						Component.translatable("options.accessibility"));
			} else if (screen instanceof PauseScreen pause) {
				Ui.hideLike(pause, "disconnect", "report", "bug", "advancement", "statistic", "feedback",
						"отзыв", "ошибк", "достижен", "статистик", "выйти");
				if (client.player != null && !Sessions.get(client.player.getUUID()).canLeave) {
					Ui.hideLike(pause, "disconnect", "выйти", "title");
				}
			} else if (screen instanceof OptionsScreen opts) {
				Ui.hideLike(opts,
						"skin", "appearance", "music", "video", "controls", "chat", "resource", "accessibility",
						"telemetry", "data", "attribute", "внешн", "музык", "график", "управлен", "чат", "ресурс",
						"специальн", "телеметр", "данн", "атрибут");
			} else if (screen instanceof SelectWorldScreen select) {
				Ui.hideLike(select, "create", "delete", "edit", "recreate");
			}
		});
	}
}
