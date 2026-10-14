package tictim.paraglider.fabric.mixin;
import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.Minecraft;import tictim.paraglider.api.movement.Movement;
@Mixin(Minecraft.class) public class ParagliderInteractionMixin {
 private static boolean paraglider$gliding(){var p=Minecraft.getInstance().player;return p!=null&&Movement.get(p).state().paragliding();}
 @Inject(method="startAttack",at=@At("HEAD"),cancellable=true) private void paraglider$attack(CallbackInfoReturnable<Boolean> ci){if(paraglider$gliding())ci.setReturnValue(false);}
 @Inject(method="continueAttack",at=@At("HEAD"),cancellable=true) private void paraglider$continue(boolean down,CallbackInfo ci){if(paraglider$gliding())ci.cancel();}
}
