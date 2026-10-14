package tictim.paraglider.fabric.loot;
import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.*;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import tictim.paraglider.ParagliderMod;
import tictim.paraglider.contents.loot.*;
import java.util.*;import java.util.function.Consumer;
public final class ParagliderLootManager implements SimpleSynchronousResourceReloadListener {
 private record Entry(Set<Identifier> tables,LootModifier modifier){}
 private static volatile List<Entry> entries=List.of();
 private final net.minecraft.core.HolderLookup.Provider registries;
 public ParagliderLootManager(net.minecraft.core.HolderLookup.Provider registries){this.registries=registries;}
 private static final Map<LootTable,Optional<Identifier>> ids=Collections.synchronizedMap(new WeakHashMap<>());
 public Identifier getFabricId(){return Identifier.fromNamespaceAndPath("paraglider","loot_modifiers");}
 public static void register(){ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(Identifier.fromNamespaceAndPath("paraglider","loot_modifiers"),ParagliderLootManager::new);}
 public void onResourceManagerReload(ResourceManager manager){
 var loaded=new ArrayList<Entry>();
 for(var resource:manager.listResources("loot_modifiers",id->id.getNamespace().equals("paraglider")&&id.getPath().endsWith(".json")).entrySet()){
  try(var reader=resource.getValue().openAsReader()){
   var json=JsonParser.parseReader(reader).getAsJsonObject();var tables=new HashSet<Identifier>();var conditions=new JsonArray();
   for(var c:json.getAsJsonArray("conditions")){var o=c.getAsJsonObject();if(o.get("condition").getAsString().equals("neoforge:loot_table_id"))tables.add(Identifier.parse(o.get("loot_table_id").getAsString()));else conditions.add(c);}
   json.add("conditions",conditions);
   var codec=switch(json.get("type").getAsString()){
    case "paraglider:paraglider" -> ParagliderLoot.CODEC;
    case "paraglider:spirit_orb" -> SpiritOrbLoot.CODEC;
    case "paraglider:vessel" -> VesselLoot.CODEC;
    case "paraglider:configurable_spirit_orb" -> ConfigurableSpiritOrbLoot.CODEC;
    default -> throw new IllegalArgumentException("Unknown loot modifier type");};
   loaded.add(new Entry(Set.copyOf(tables),(LootModifier)codec.codec().parse(registries.createSerializationContext(JsonOps.INSTANCE),json).getOrThrow()));
  }catch(Exception e){throw new IllegalStateException("Invalid Paragliders loot modifier "+resource.getKey(),e);}
 }
 entries=List.copyOf(loaded);ids.clear();ParagliderMod.LOGGER.info("Loaded {} Fabric loot modifiers",entries.size());
 }
 public static void apply(LootTable table,LootContext context,Consumer<ItemStack> consumer){
  Optional<Identifier> id=ids.computeIfAbsent(table,t->context.getLevel().getServer().reloadableRegistries().lookup().lookupOrThrow(Registries.LOOT_TABLE).listElements().filter(h->h.value()==t).map(h->h.key().identifier()).findFirst());
  for(var e:entries){if(!e.tables.isEmpty()&&(id.isEmpty()||!e.tables.contains(id.get())))continue;
   e.modifier.apply(new ObjectArrayList<>(),context).forEach(consumer);}
 }
}
