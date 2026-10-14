package tictim.paraglider.fabric.registry;
import net.minecraft.core.Registry;import net.minecraft.resources.ResourceKey;import net.minecraft.world.item.Item;import net.minecraft.world.level.block.Block;import net.minecraft.world.level.ItemLike;import java.util.function.Supplier;
public final class DeferredBlock<T extends Block> extends DeferredHolder<Block,T> implements ItemLike {
 DeferredBlock(Registry<Block> registry,ResourceKey<Block> key,Supplier<T> factory){super(registry,key,factory);}
 public Item asItem(){return get().asItem();}
}
