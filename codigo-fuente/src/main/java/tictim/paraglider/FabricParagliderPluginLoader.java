package tictim.paraglider;
import java.util.List;
import java.util.ArrayList;
import net.fabricmc.loader.api.FabricLoader;
import tictim.paraglider.plugin.ParagliderPluginLoader;
import tictim.paraglider.api.plugin.*;
import tictim.paraglider.api.movement.MovementPlugin;
import tictim.paraglider.api.stamina.StaminaPlugin;
public final class FabricParagliderPluginLoader implements ParagliderPluginLoader {
 private List<PluginInstance<StaminaPlugin>> stamina;
 private List<PluginInstance<MovementPlugin>> movement;
 private void initialize(){
  if(stamina!=null)return;
  var s=new ArrayList<PluginInstance<StaminaPlugin>>();var m=new ArrayList<PluginInstance<MovementPlugin>>();
  for(var entry:FabricLoader.getInstance().getEntrypointContainers("paraglider-plugin",ParagliderPluginBase.class)){
   var plugin=entry.getEntrypoint();String mod=entry.getProvider().getMetadata().getId();
   if(plugin instanceof StaminaPlugin p)s.add(new PluginInstance<>(p,mod));
   if(plugin instanceof MovementPlugin p)m.add(new PluginInstance<>(p,mod));
  }
  stamina=List.copyOf(s);movement=List.copyOf(m);
 }
 public List<PluginInstance<StaminaPlugin>> getStaminaPlugins(){initialize();return stamina;}
 public List<PluginInstance<MovementPlugin>> getMovementPlugins(){initialize();return movement;}
}
