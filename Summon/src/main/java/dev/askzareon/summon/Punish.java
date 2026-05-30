package dev.askzareon.summon;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public final class Punish {
	private static final Random RNG = new Random();

	public static void hit(ServerPlayer p) {
		ServerLevel lvl = p.serverLevel();
		switch (RNG.nextInt(6)) {
			case 0 -> {
				BlockPos pos = p.blockPosition().above(8);
				FallingBlockEntity f = FallingBlockEntity.fall(lvl, pos, Blocks.ANVIL.defaultBlockState());
				if (f != null) {
					f.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				}
			}
			case 1 -> EntityType.WARDEN.spawn(lvl, p.blockPosition().offset(RNG.nextInt(5) - 2, 0, RNG.nextInt(5) - 2), net.minecraft.world.entity.MobSpawnType.EVENT);
			case 2 -> {
				EntityType<?>[] m = {EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER, EntityType.SPIDER, EntityType.ENDERMAN};
				m[RNG.nextInt(m.length)].spawn(lvl, p.blockPosition().offset(RNG.nextInt(8) - 4, 0, RNG.nextInt(8) - 4), net.minecraft.world.entity.MobSpawnType.EVENT);
			}
			case 3 -> {
				BlockPos pos = p.blockPosition().offset(RNG.nextInt(3) - 1, 0, RNG.nextInt(3) - 1);
				lvl.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3f, net.minecraft.world.level.Level.ExplosionInteraction.TNT);
			}
			case 4 -> {
				LightningBolt b = EntityType.LIGHTNING_BOLT.create(lvl);
				if (b != null) {
					b.moveTo(Vec3.atBottomCenterOf(p.blockPosition()));
					lvl.addFreshEntity(b);
				}
			}
			default -> {
				BlockPos c = p.blockPosition().below();
				for (int dx = -2; dx <= 2; dx++) {
					for (int dz = -2; dz <= 2; dz++) {
						for (int dy = 0; dy < 3; dy++) {
							lvl.setBlock(c.offset(dx, -dy, dz), Blocks.AIR.defaultBlockState(), 3);
						}
					}
				}
			}
		}
	}
}
