package tictim.paraglider.fabric.loot;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.Arrays;
public abstract class LootModifier implements IGlobalLootModifier {
 public static final Codec<LootItemCondition[]> CONDITIONS=LootItemCondition.DIRECT_CODEC.listOf().xmap(l->l.toArray(LootItemCondition[]::new),Arrays::asList);
 protected final LootItemCondition[] conditions;
 protected LootModifier(LootItemCondition[] conditions){this.conditions=conditions;}
 public ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> loot,LootContext context){for(var c:conditions)if(!c.test(context))return loot;return doApply(loot,context);}
 protected abstract ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> loot,LootContext context);
}
