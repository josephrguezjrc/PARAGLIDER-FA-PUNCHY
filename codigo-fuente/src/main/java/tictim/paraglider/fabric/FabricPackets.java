package tictim.paraglider.fabric;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
public final class FabricPackets {
 public static void sendToPlayer(ServerPlayer player,CustomPacketPayload message){ServerPlayNetworking.send(player,message);}
 public static void sendToAllPlayers(CustomPacketPayload message){for(var player:PlayerLookup.all(ServerLifecycle.server()))sendToPlayer(player,message);}
 public static void sendToPlayersTrackingEntity(Entity entity,CustomPacketPayload message){for(var player:PlayerLookup.tracking(entity))sendToPlayer(player,message);}
 public static void sendToPlayersTrackingChunk(ServerLevel level,ChunkPos pos,CustomPacketPayload message){for(var player:PlayerLookup.tracking(level,pos))sendToPlayer(player,message);}
 public static void sendToServer(CustomPacketPayload message){ClientSender.send(message);}
 private static class ClientSender {static void send(CustomPacketPayload message){ClientPlayNetworking.send(message);}}
}
