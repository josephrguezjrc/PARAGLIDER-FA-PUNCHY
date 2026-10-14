package tictim.paraglider.fabric.mixin;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tictim.paraglider.fabric.ParagliderBrewing;
@Mixin(PotionBrewing.class) public class BrewingMixin {
 @Inject(method="isIngredient",at=@At("HEAD"),cancellable=true) private void paraglider$ingredient(ItemStack s,CallbackInfoReturnable<Boolean> c){if(ParagliderBrewing.isIngredient(s))c.setReturnValue(true);}
 @Inject(method="hasMix",at=@At("HEAD"),cancellable=true) private void paraglider$hasMix(ItemStack input,ItemStack ingredient,CallbackInfoReturnable<Boolean> c){if(!ParagliderBrewing.mix(input,ingredient).isEmpty())c.setReturnValue(true);}
 @Inject(method="mix",at=@At("HEAD"),cancellable=true) private void paraglider$mix(ItemStack ingredient,ItemStack input,CallbackInfoReturnable<ItemStack> c){var result=ParagliderBrewing.mix(input,ingredient);if(!result.isEmpty())c.setReturnValue(result);}
}
