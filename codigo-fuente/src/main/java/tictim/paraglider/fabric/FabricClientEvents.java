package tictim.paraglider.fabric;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import tictim.paraglider.event.*;
import tictim.paraglider.client.render.InGameStaminaWheelRenderer;
import tictim.paraglider.wind.Wind;
public final class FabricClientEvents {
 public static void register(){
  ClientTickEvents.START_CLIENT_TICK.register(client->WindParticleEventHandler.onClientTick());
  ClientTickEvents.END_CLIENT_TICK.register(client->ParagliderClientEventHandler.onClientTick());
  ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((client,world)->{Wind.registerLevel(world);});
  ClientPlayConnectionEvents.JOIN.register((handler,sender,client)->InGameStaminaWheelRenderer.get().reset());
  ClientPlayConnectionEvents.DISCONNECT.register((handler,client)->{if(client.level!=null)Wind.unregisterLevel(client.level);tictim.paraglider.ParagliderClientMod.instance().setSyncedStateMap(null);});
  ItemTooltipCallback.EVENT.register((stack,context,flag,lines)->ParagliderEventHandler.onAddAttributeTooltips(stack,lines));
 }
}
