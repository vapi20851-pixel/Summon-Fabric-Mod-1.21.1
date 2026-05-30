package dev.askzareon.summon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

import java.util.List;
import java.util.Random;

public final class Ambient {
	private static final Random RNG = new Random();
	private static int wait;

	public static void tick(MinecraftServer server) {
		if (++wait % 600 != 0) {
			return;
		}
		ServerLevel lvl = server.overworld();
		if (lvl == null) {
			return;
		}
		List<ServerPlayer> ps = server.getPlayerList().getPlayers();
		if (ps.isEmpty()) {
			return;
		}
		ServerPlayer p = ps.get(RNG.nextInt(ps.size()));
		switch (RNG.nextInt(3)) {
			case 0 -> chest(lvl, p);
			case 1 -> rain(lvl, p);
			default -> hurt(p);
		}
	}

	private static void chest(ServerLevel lvl, ServerPlayer p) {
		BlockPos pos = lvl.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE,
				p.blockPosition().offset(RNG.nextInt(20) - 10, 0, RNG.nextInt(20) - 10));
		lvl.setBlock(pos, Blocks.CHEST.defaultBlockState(), 3);
		BlockEntity be = lvl.getBlockEntity(pos);
		if (be instanceof RandomizableContainerBlockEntity c) {
			if (RNG.nextBoolean()) {
				c.setItem(0, new ItemStack(Items.POISONOUS_POTATO, 16));
				c.setItem(1, new ItemStack(Items.TNT, 2));
			} else {
				c.setItem(0, new ItemStack(Items.IRON_INGOT, RNG.nextInt(8) + 1));
				c.setItem(1, new ItemStack(Items.GOLD_INGOT, RNG.nextInt(4) + 1));
			}
		}
	}

	private static void rain(ServerLevel lvl, ServerPlayer p) {
		if (RNG.nextBoolean()) {
			for (int i = 0; i < 30; i++) {
				ItemStack s = RNG.nextBoolean() ? new ItemStack(Items.POTATO) : new ItemStack(Items.COD);
				lvl.addFreshEntity(new ItemEntity(lvl, p.getX() + RNG.nextInt(10) - 5, p.getY() + 10, p.getZ() + RNG.nextInt(10) - 5, s));
			}
		} else {
			for (int i = 0; i < 5; i++) {
				EntityType.PUFFERFISH.spawn(lvl, p.blockPosition().offset(RNG.nextInt(10) - 5, 1, RNG.nextInt(10) - 5), net.minecraft.world.entity.MobSpawnType.EVENT);
			}
		}
	}

	private static void hurt(ServerPlayer p) {
		Holder<MobEffect>[] fx = new Holder[]{
				MobEffects.DARKNESS, MobEffects.BLINDNESS, MobEffects.WITHER,
				MobEffects.MOVEMENT_SLOWDOWN, MobEffects.WEAKNESS
		};
		p.addEffect(new MobEffectInstance(fx[RNG.nextInt(fx.length)], 100 + RNG.nextInt(200), RNG.nextInt(2)));
	}
}
