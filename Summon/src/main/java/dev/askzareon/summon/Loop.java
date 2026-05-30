package dev.askzareon.summon;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Random;

public final class Loop {
	private static final Random RNG = new Random();
	private static final int TPS = 20;

	public static void tick(MinecraftServer server) {
		for (ServerPlayer p : server.getPlayerList().getPlayers()) {
			Sessions.Run run = Sessions.get(p.getUUID());
			int tick = server.getTickCount();

			if (run.start < 0) {
				run.start = tick;
				run.waveAt = tick + 1200 + RNG.nextInt(2400);
				Books.drop(p, 1);
				run.book1 = true;
				Sessions.save();
			}

			long t = tick - run.start;

			once(p, run, t, 60 * TPS, "hello", "msg.hello", ChatFormatting.GREEN, p.getName().getString());
			once(p, run, t, 90 * TPS, "world", "msg.world", ChatFormatting.GREEN);
			once(p, run, t, 170 * TPS, "why", "msg.why", ChatFormatting.GREEN);

			if (!run.waved && tick >= run.waveAt) {
				say(p, "msg.wave", ChatFormatting.GREEN, p.getName().getString());
				run.waved = true;
				Sessions.save();
			}

			once(p, run, t, 300 * TPS, "guests", "msg.guests", ChatFormatting.GREEN);
			once(p, run, t, 360 * TPS, "time", "msg.time", ChatFormatting.YELLOW);
			once(p, run, t, 420 * TPS, "test", "msg.dont_test", ChatFormatting.YELLOW);
			once(p, run, t, 480 * TPS, "stay", "msg.overstayed", ChatFormatting.YELLOW);
			once(p, run, t, 600 * TPS, "leave", "msg.leave", ChatFormatting.RED);

			if (!run.said("last") && t >= 720 * TPS) {
				say(p, "msg.last_chance", ChatFormatting.RED);
				run.mark("last");
				finish(p, run);
				Sessions.save();
			}

			if (!run.book2 && t >= 120 * TPS) {
				Books.drop(p, 2);
				run.book2 = true;
				Sessions.save();
			}
			if (!run.book3 && t >= 300 * TPS) {
				Books.drop(p, 3);
				p.getInventory().add(new ItemStack(Items.DIAMOND, 3));
				run.book3 = true;
				Sessions.save();
			}
			if (!run.book4 && t >= 600 * TPS) {
				Books.drop(p, 4);
				run.book4 = true;
				Sessions.save();
			}

			if (t >= 360 * TPS) {
				long n = (t - 360 * TPS) / (40 * TPS) + 1;
				if (n > run.punishCount) {
					run.punishCount = n;
					Sessions.save();
					Punish.hit(p);
				}
			}

			if (run.bad) {
				End.spam(p);
			}
		}
	}

	private static void once(ServerPlayer p, Sessions.Run run, long t, long at, String key, String msg, ChatFormatting c, Object... args) {
		if (t < at || t >= at + TPS || run.said(key)) {
			return;
		}
		say(p, msg, c, args);
		run.mark(key);
		Sessions.save();
	}

	static void say(ServerPlayer p, String key, ChatFormatting c, Object... args) {
		Component body = Component.translatable(key, args);
		p.sendSystemMessage(Component.literal("[???] ").withStyle(c).append(body.copy().withStyle(ChatFormatting.WHITE)));
	}

	private static void finish(ServerPlayer p, Sessions.Run run) {
		if (run.done) {
			return;
		}
		run.done = true;
		if (End.loot(p) > 200) {
			run.canLeave = true;
			State.win();
			State.breakWorld();
			say(p, "msg.entity_leaves", ChatFormatting.GREEN);
			say(p, "msg.good_ending", ChatFormatting.GREEN);
		} else {
			run.bad = true;
		}
	}
}
