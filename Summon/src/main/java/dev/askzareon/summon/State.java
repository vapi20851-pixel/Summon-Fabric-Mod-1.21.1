package dev.askzareon.summon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class State {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("summon_state.json");

	private static boolean dead;
	private static boolean worldGone;
	private static boolean won;
	private static boolean warned;

	public static void load() {
		if (!Files.exists(FILE)) {
			return;
		}
		try {
			Data d = GSON.fromJson(Files.readString(FILE), Data.class);
			if (d != null) {
				dead = d.dead;
				worldGone = d.worldGone;
				won = d.won;
				warned = d.warned;
			}
		} catch (IOException ignored) {
		}
	}

	public static void save() {
		try {
			Files.createDirectories(FILE.getParent());
			Data d = new Data();
			d.dead = dead;
			d.worldGone = worldGone;
			d.won = won;
			d.warned = warned;
			Files.writeString(FILE, GSON.toJson(d));
		} catch (IOException ignored) {
		}
	}

	public static boolean dead() {
		return dead;
	}

	public static void kill() {
		dead = true;
		save();
	}

	public static boolean worldGone() {
		return worldGone;
	}

	public static void breakWorld() {
		worldGone = true;
		save();
	}

	public static void win() {
		won = true;
		save();
	}

	public static boolean warned() {
		return warned;
	}

	public static void acceptWarn() {
		warned = true;
		save();
	}

	public static String brokenName() {
		return dead ? "\u00A7k\u0421\u0443\u043C\u043C\u043E\u043D\u00A7r \u00A74\u25A0\u00A7kERR\u00A7r" : "Summon";
	}

	public static String brokenDesc() {
		return dead ? "\u00A7k\u043D\u0435 \u0437\u0430\u0445\u043E\u0434\u0438 \u00A7r\u00A74\u25A0 \u00A7k\u043D\u0435 \u043E\u0442\u043A\u0440\u044B\u0432\u0430\u0439 \u00A7r\u00A78[\u041E\u0428\u0418\u0411\u041A\u0410]" : "Do not enter.";
	}

	private static final class Data {
		boolean dead;
		boolean worldGone;
		boolean won;
		boolean warned;
	}
}
