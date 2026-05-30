package dev.askzareon.summon;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Summon implements ModInitializer {
	public static final String ID = "summon";
	public static final String WORLD = "level.dat";
	public static final Logger LOG = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
		State.load();
		if (State.dead()) {
			return;
		}

		Sessions.load();

		// Disabled world scramble on server start — caused NPE in some environments.
		// If you want to enable this later, ensure the overworld spawn and level data
		// are ready and non-null before calling World.scramble(server.overworld()).
		// ServerLifecycleEvents.SERVER_STARTED.register(server -> {
		//     if (server.overworld() != null) {
		//         World.scramble(server.overworld());
		//     }
		// });

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (State.dead()) {
				return;
			}
			Loop.tick(server);
			Ambient.tick(server);
		});

		ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> false);
		ServerMessageEvents.ALLOW_COMMAND_MESSAGE.register((message, sender, params) -> false);
	}
}
