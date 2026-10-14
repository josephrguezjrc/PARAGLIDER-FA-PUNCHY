package tictim.paraglider.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import tictim.paraglider.network.ParagliderNetwork;
import tictim.paraglider.wind.Wind;
import tictim.paraglider.wind.WindChunk;
import tictim.paraglider.wind.WindUtils;

import static tictim.paraglider.api.ParagliderAPI.MODID;

public final class WindEventHandler {
	private WindEventHandler() {}

	public static void onLevelLoad(Level level) {
		Wind.registerLevel(level);
	}

	public static void onLevelUnload(Level level) {
		Wind.unregisterLevel(level);
	}

	public static void onWorldTick(Level level) {
		if (level.isClientSide()) return;
		WindUtils.updateWind(level);
	}

	public static void onChunkUnload(Level level,net.minecraft.world.level.chunk.LevelChunk chunk) {
		Wind wind = Wind.of(level);
		if (wind != null) wind.remove(chunk.getPos());
	}

	public static void onChunkWatch(ServerLevel level,ChunkPos pos) {
		Wind wind = Wind.of(level);
		if (wind == null) return;
		WindChunk windChunk = wind.getChunk(pos);
		if (windChunk == null || windChunk.isEmpty()) return;
		ParagliderNetwork.get().syncWind(level.getServer(), level.getChunk(pos.x, pos.z), windChunk);
	}
}
