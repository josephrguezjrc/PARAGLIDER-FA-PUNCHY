package tictim.paraglider.fabric;
import net.minecraft.server.MinecraftServer;
public final class ServerLifecycle {
 private static MinecraftServer server;
 public static void set(MinecraftServer value){server=value;}
 public static MinecraftServer server(){if(server==null)throw new IllegalStateException("Paraglider server not running");return server;}
}
