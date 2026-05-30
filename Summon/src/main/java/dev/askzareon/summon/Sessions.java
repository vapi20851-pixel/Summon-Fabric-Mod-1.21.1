package dev.askzareon.summon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class Sessions {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("summon_sessions.json");
	private static final Map<UUID, Run> runs = new HashMap<>();

	public static void load() {
		if (!Files.exists(FILE)) {
			return;
		}
		try {
			Store s = GSON.fromJson(Files.readString(FILE), Store.class);
			if (s != null && s.runs != null) {
				runs.clear();
				runs.putAll(s.runs);
			}
		} catch (IOException ignored) {
		}
	}

	public static void save() {
		try {
			Files.createDirectories(FILE.getParent());
			Store s = new Store();
			s.runs = runs;
			Files.writeString(FILE, GSON.toJson(s));
		} catch (IOException ignored) {
		}
	}

	public static Run get(UUID id) {
		return runs.computeIfAbsent(id, k -> new Run());
	}

	public static final class Run {
		public long start = -1;
		public boolean book1;
		public boolean book2;
		public boolean book3;
		public boolean book4;
		public boolean waved;
		public long waveAt = -1;
		public boolean done;
		public boolean bad;
		public boolean canLeave;
		public long punishCount;
		public int spam;
		public Set<String> said = new HashSet<>();

		public boolean said(String k) {
			return said.contains(k);
		}

		public void mark(String k) {
			said.add(k);
		}
	}

	private static final class Store {
		Map<UUID, Run> runs = new HashMap<>();
	}
}
