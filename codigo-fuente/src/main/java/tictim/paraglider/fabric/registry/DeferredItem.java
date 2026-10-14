package tictim.paraglider.fabric.registry;
import net.minecraft.core.Registry;import net.minecraft.resources.ResourceKey;import net.minecraft.world.item.*;import net.minecraft.world.level.ItemLike;import java.util.function.Supplier;
public final class DeferredItem<T extends Item> extends DeferredHolder<Item,T> implements ItemLike {
 DeferredItem(Registry<Item> registry,ResourceKey<Item> key,Supplier<T> factory){super(registry,key,factory);}
 public Item asItem(){return get();}
}
