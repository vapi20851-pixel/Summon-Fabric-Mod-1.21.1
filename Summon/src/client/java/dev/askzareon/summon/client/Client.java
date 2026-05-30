package dev.askzareon.summon.client;

import dev.askzareon.summon.State;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screens.TitleScreen;

public class Client implements ClientModInitializer {
	private static boolean showedDead;

	@Override
	public void onInitializeClient() {
		State.load();

		if (State.dead()) {
			ClientTickEvents.END_CLIENT_TICK.register(mc -> {
				if (!showedDead && mc.screen == null) {
					showedDead = true;
					mc.setScreen(new DeadScreen());
				}
			});
			return;
		}

		// TODO: Screens.hook() causes infinite reload loop
		// Screens.hook();

		ClientTickEvents.END_CLIENT_TICK.register(mc -> {
			if (mc.level != null) {
				mc.level.setDayTime(18000);
			}
			if (mc.screen instanceof TitleScreen) {
				mc.getMusicManager().stopPlaying();
			}
		});
	}
}
