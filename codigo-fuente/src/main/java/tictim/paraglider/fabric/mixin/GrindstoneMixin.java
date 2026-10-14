package tictim.paraglider.fabric.mixin;
import net.minecraft.world.inventory.GrindstoneMenu;import net.minecraft.world.item.ItemStack;import tictim.paraglider.contents.item.ParagliderItem;
import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(GrindstoneMenu.class) public class GrindstoneMixin {
 @Inject(method="computeResult",at=@At("HEAD"),cancellable=true) private void paraglider$repair(ItemStack a,ItemStack b,CallbackInfoReturnable<ItemStack> c){if(a.getItem() instanceof ParagliderItem||b.getItem() instanceof ParagliderItem)c.setReturnValue(ItemStack.EMPTY);}
}
