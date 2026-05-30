package dev.askzareon.summon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class World {
	private static final Random RNG = new Random();
	private static boolean done;

	public static void scramble(ServerLevel lvl) {
		if (State.worldGone() || done) {
			return;
		}
		done = true;

		List<Block> blocks = new ArrayList<>();
		for (Block b : BuiltInRegistries.BLOCK) {
			if (b != Blocks.AIR && b != Blocks.VOID_AIR && b != Blocks.CAVE_AIR) {
				blocks.add(b);
			}
		}

		BlockPos c = lvl.getSharedSpawnPos();
		int r = 64;
		for (int x = -r; x <= r; x++) {
			for (int z = -r; z <= r; z++) {
				for (int y = lvl.getMinBuildHeight(); y < lvl.getMaxBuildHeight(); y++) {
					lvl.setBlock(c.offset(x, y, z), blocks.get(RNG.nextInt(blocks.size())).defaultBlockState(), 2);
				}
			}
		}
		lvl.setDayTime(18000);
	}
}
