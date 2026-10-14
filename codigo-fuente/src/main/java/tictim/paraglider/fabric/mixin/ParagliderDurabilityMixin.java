package tictim.paraglider.fabric.mixin;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tictim.paraglider.contents.item.ParagliderItem;
import tictim.paraglider.config.Cfg;
@Mixin(ItemStack.class) public class ParagliderDurabilityMixin {
 @Inject(method="getMaxDamage",at=@At("HEAD"),cancellable=true)
 private void paraglider$maxDamage(CallbackInfoReturnable<Integer> cir){if(((ItemStack)(Object)this).getItem() instanceof ParagliderItem)cir.setReturnValue(Cfg.get().paragliderDurability());}
 @Inject(method="isDamageableItem",at=@At("HEAD"),cancellable=true)
 private void paraglider$unlimited(CallbackInfoReturnable<Boolean> cir){if(((ItemStack)(Object)this).getItem() instanceof ParagliderItem && Cfg.get().paragliderDurability()<=0)cir.setReturnValue(false);}
}
