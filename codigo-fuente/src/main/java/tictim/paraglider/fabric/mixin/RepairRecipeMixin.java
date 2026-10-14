package tictim.paraglider.fabric.mixin;
import net.minecraft.world.item.crafting.RepairItemRecipe;import net.minecraft.world.item.ItemStack;import tictim.paraglider.contents.item.ParagliderItem;
import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(RepairItemRecipe.class) public class RepairRecipeMixin {
 @Inject(method="canCombine",at=@At("HEAD"),cancellable=true) private static void paraglider$repair(ItemStack a,ItemStack b,CallbackInfoReturnable<Boolean> c){if(a.getItem() instanceof ParagliderItem||b.getItem() instanceof ParagliderItem)c.setReturnValue(false);}
}
