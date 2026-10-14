package tictim.paraglider.fabric.mixin;
import net.minecraft.server.network.*;import net.minecraft.server.level.ServerLevel;import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.wind.*;import tictim.paraglider.fabric.FabricPackets;
@Mixin(PlayerChunkSender.class) public class WindChunkMixin {
 @Inject(method="sendChunk",at=@At("TAIL")) private static void paraglider$wind(ServerGamePacketListenerImpl connection,ServerLevel level,LevelChunk chunk,CallbackInfo ci){
 var wind=Wind.of(level);if(wind!=null){var data=wind.getChunk(chunk.getPos());if(data!=null&&!data.isEmpty())tictim.paraglider.event.WindEventHandler.onChunkWatch(level,chunk.getPos());}}
}
