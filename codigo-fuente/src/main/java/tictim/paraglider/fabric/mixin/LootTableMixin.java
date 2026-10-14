package tictim.paraglider.fabric.mixin;
import net.minecraft.world.level.storage.loot.*;import net.minecraft.world.item.ItemStack;import java.util.function.Consumer;
import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(LootTable.class) public class LootTableMixin {
 @Inject(method="getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V",at=@At("TAIL"))
 private void paraglider$loot(LootContext context,Consumer<ItemStack> consumer,CallbackInfo ci){tictim.paraglider.fabric.loot.ParagliderLootManager.apply((LootTable)(Object)this,context,consumer);}
}
