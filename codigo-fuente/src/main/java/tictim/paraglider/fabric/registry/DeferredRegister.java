package tictim.paraglider.fabric.registry;
import net.minecraft.core.*;import net.minecraft.core.registries.*;import net.minecraft.resources.*;import net.minecraft.world.item.Item;import net.minecraft.world.level.block.Block;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import java.util.*;import java.util.function.*;
public class DeferredRegister<R> {
 final Registry<R> registry;final String namespace;final List<DeferredHolder<R,?>> entries=new ArrayList<>();
 protected DeferredRegister(Registry<R> registry,String namespace){this.registry=registry;this.namespace=namespace;}
 public static <R> DeferredRegister<R> create(Registry<R> registry,String namespace){return new DeferredRegister<>(registry,namespace);}
 @SuppressWarnings("unchecked") public static <R> DeferredRegister<R> create(ResourceKey<? extends Registry<R>> key,String namespace){
  Registry<R> registry=(Registry<R>)BuiltInRegistries.REGISTRY.getValue(key.identifier());
  if(registry==null)registry=FabricRegistryBuilder.<R>createSimple((ResourceKey<Registry<R>>)(Object)key).attribute(net.fabricmc.fabric.api.event.registry.RegistryAttribute.SYNCED).buildAndRegister();
  return create(registry,namespace);
 }
 public static Blocks createBlocks(String namespace){return new Blocks(namespace);}
 public static Items createItems(String namespace){return new Items(namespace);}
 public <T extends R> DeferredHolder<R,T> register(String name,Supplier<T> supplier){
  var h=new DeferredHolder<R,T>(registry,ResourceKey.create(registry.key(),Identifier.fromNamespaceAndPath(namespace,name)),supplier);entries.add(h);return h;
 }
 public <T extends R> DeferredHolder<R,T> register(String name,Function<ResourceKey<R>,T> factory){
  var key=ResourceKey.create(registry.key(),Identifier.fromNamespaceAndPath(namespace,name));return register(name,()->factory.apply(key));
 }
 public void register(){entries.forEach(DeferredHolder::get);}
 public void register(Object ignored){register();}
 public static final class Blocks extends DeferredRegister<Block> {
  Blocks(String namespace){super(BuiltInRegistries.BLOCK,namespace);}
  @Override public <T extends Block> DeferredBlock<T> register(String name,Function<ResourceKey<Block>,T> factory){
   var key=ResourceKey.create(registry.key(),Identifier.fromNamespaceAndPath(namespace,name));var h=new DeferredBlock<T>(registry,key,()->factory.apply(key));entries.add(h);return h;
  }
 }
 public static final class Items extends DeferredRegister<Item> {
  Items(String namespace){super(BuiltInRegistries.ITEM,namespace);}
  @Override public <T extends Item> DeferredItem<T> register(String name,Function<ResourceKey<Item>,T> factory){
   var key=ResourceKey.create(registry.key(),Identifier.fromNamespaceAndPath(namespace,name));var h=new DeferredItem<T>(registry,key,()->factory.apply(key));entries.add(h);return h;
  }
 }
}
