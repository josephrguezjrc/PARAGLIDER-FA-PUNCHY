package tictim.paraglider.fabric.mixin;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tictim.paraglider.fabric.ParagliderBrewing;
@Mixin(targets="net.minecraft.world.inventory.BrewingStandMenu$PotionSlot") public class BrewingSlotMixin {
 @Inject(method="mayPlaceItem",at=@At("HEAD"),cancellable=true) private static void paraglider$elixir(ItemStack s,CallbackInfoReturnable<Boolean> c){if(ParagliderBrewing.isElixir(s))c.setReturnValue(true);}
}
