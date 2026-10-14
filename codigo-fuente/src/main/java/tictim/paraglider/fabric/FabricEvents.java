package tictim.paraglider.fabric;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import tictim.paraglider.ParagliderMod;
import tictim.paraglider.ParagliderCommands;
import tictim.paraglider.ParagliderUtils;
import tictim.paraglider.event.*;
import tictim.paraglider.wind.Wind;
import tictim.paraglider.wind.WindSourceRegistry;
public final class FabricEvents {
 public static void register(){
  ServerLifecycleEvents.SERVER_STARTING.register(server->{
   ServerLifecycle.set(server);
   var mod=ParagliderMod.instance();var config=mod.getPlayerStateMapConfig();
   config.removeCallbacks();config.reload(null);
   ParagliderUtils.printPlayerStates(config.stateMap(),mod.getPlayerConnectionMap());
   config.addCallback(state->{ParagliderUtils.printPlayerStates(state,mod.getPlayerConnectionMap());mod.getNetwork().syncStateMapToAll(state);});
  });
  ServerLifecycleEvents.SERVER_STARTED.register(server->{WindSourceRegistry.get().computeWindSource(server.registryAccess());
 tictim.paraglider.contents.ParagliderVillageStructures.addVillageStructures(server.registryAccess());
 new tictim.paraglider.bargain.BargainRecipeChecker(server.registryAccess(),server.getRecipeManager()).check();});
  ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server,resourceManager,success)->{if(success){WindSourceRegistry.get().computeWindSource(server.registryAccess());new tictim.paraglider.bargain.BargainRecipeChecker(server.registryAccess(),server.getRecipeManager()).check();}});
  ServerLifecycleEvents.SERVER_STOPPED.register(server->{ParagliderMod.instance().getPlayerStateMapConfig().removeCallbacks();ServerLifecycle.set(null);});
  net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT.register((dispatcher,registry,environment)->dispatcher.register(ParagliderCommands.register()));
  ServerTickEvents.END_SERVER_TICK.register(server->ParagliderEventHandler.afterServerTick());
  ServerWorldEvents.LOAD.register((server,level)->WindEventHandler.onLevelLoad(level));
  ServerWorldEvents.UNLOAD.register((server,level)->WindEventHandler.onLevelUnload(level));
  ServerTickEvents.END_WORLD_TICK.register(WindEventHandler::onWorldTick);
  ServerChunkEvents.CHUNK_UNLOAD.register(WindEventHandler::onChunkUnload);
  EntityTrackingEvents.START_TRACKING.register(ParagliderEventHandler::onStartTracking);
  ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player,oldWorld,newWorld)->ParagliderEventHandler.onPlayerChangedDimension(player));
  ServerPlayConnectionEvents.JOIN.register((handler,sender,server)->ParagliderEventHandler.onLogin(handler.player));
  UseBlockCallback.EVENT.register((player,world,hand,hit)->ParagliderEventHandler.cancelOffhand(player,hand)?InteractionResult.FAIL:InteractionResult.PASS);
  UseEntityCallback.EVENT.register((player,world,hand,entity,hit)->ParagliderEventHandler.cancelOffhand(player,hand)?InteractionResult.FAIL:InteractionResult.PASS);
  UseItemCallback.EVENT.register((player,world,hand)->ParagliderEventHandler.cancelOffhand(player,hand)?InteractionResult.FAIL:InteractionResult.PASS);
 }
}
