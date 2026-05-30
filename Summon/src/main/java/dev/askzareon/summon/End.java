package dev.askzareon.summon;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Random;

public final class End {
	private static final Random RNG = new Random();
	private static final String GARBAGE = "!@#$%^&*()_+-=[]{}|;':\",./<>?~`\\";

	public static int loot(ServerPlayer p) {
		int n = 0;
		for (int i = 0; i < p.getInventory().getContainerSize(); i++) {
			ItemStack s = p.getInventory().getItem(i);
			if (s.isEmpty()) {
				continue;
			}
			int v = 0;
			if (s.is(Items.IRON_INGOT)) v = 1;
			else if (s.is(Items.GOLD_INGOT)) v = 2;
			else if (s.is(Items.DIAMOND)) v = 5;
			else if (s.is(Items.NETHERITE_SCRAP)) v = 10;
			else if (s.is(Items.NETHERITE_INGOT)) v = 20;
			n += v * s.getCount();
		}
		return n;
	}

	public static void spam(ServerPlayer p) {
		Sessions.Run run = Sessions.get(p.getUUID());
		if (!run.bad || run.canLeave) {
			return;
		}
		if (p.getServer().getTickCount() % 20 != 0) {
			return;
		}

		run.spam++;
		var sb = new StringBuilder();
		for (int i = 0; i < 80; i++) {
			sb.append(GARBAGE.charAt(RNG.nextInt(GARBAGE.length())));
		}

		p.sendSystemMessage(Component.literal("[???] ").withStyle(ChatFormatting.DARK_RED)
				.append(Component.literal(sb.toString()).withStyle(ChatFormatting.DARK_RED)));

		if (run.spam >= 20) {
			State.kill();
			p.connection.disconnect(Component.literal("..."));
			throw new RuntimeException("no");
		}
		Sessions.save();
	}
}
