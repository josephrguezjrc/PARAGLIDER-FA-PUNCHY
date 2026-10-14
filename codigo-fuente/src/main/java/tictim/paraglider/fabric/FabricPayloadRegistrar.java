package tictim.paraglider.fabric;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import java.util.function.BiConsumer;

public final class FabricPayloadRegistrar {
 public <T extends CustomPacketPayload> void playToServer(CustomPacketPayload.Type<T> type,StreamCodec<? super RegistryFriendlyByteBuf,T> codec,BiConsumer<T,PayloadContext> receiver) {
  PayloadTypeRegistry.playC2S().register(type,codec);
  ServerPlayNetworking.registerGlobalReceiver(type,(payload,context)->context.server().execute(()->receiver.accept(payload,new PayloadContext(context.player()))));
 }
 public <T extends CustomPacketPayload> void playToClient(CustomPacketPayload.Type<T> type,StreamCodec<? super RegistryFriendlyByteBuf,T> codec,BiConsumer<T,Object> receiver) {
  PayloadTypeRegistry.playS2C().register(type,codec);
  if(FabricLoader.getInstance().getEnvironmentType()==EnvType.CLIENT) ClientReceivers.register(type,receiver);
 }
 public <T extends CustomPacketPayload> void playBidirectional(CustomPacketPayload.Type<T> type,StreamCodec<? super RegistryFriendlyByteBuf,T> codec,BiConsumer<T,PayloadContext> server,BiConsumer<T,Object> client) {
  playToServer(type,codec,server);playToClient(type,codec,client);
 }
 private static final class ClientReceivers {
  static <T extends CustomPacketPayload> void register(CustomPacketPayload.Type<T> type,BiConsumer<T,Object> receiver){
   ClientPlayNetworking.registerGlobalReceiver(type,(payload,context)->context.client().execute(()->receiver.accept(payload,context)));
  }
 }
}
