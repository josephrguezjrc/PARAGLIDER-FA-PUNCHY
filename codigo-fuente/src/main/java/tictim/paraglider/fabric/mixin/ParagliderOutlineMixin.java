package tictim.paraglider.fabric.mixin;
import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.Minecraft;import tictim.paraglider.api.movement.Movement;
@Mixin(net.minecraft.client.renderer.LevelRenderer.class) public class ParagliderOutlineMixin {
 @Inject(method="extractBlockOutline",at=@At("HEAD"),cancellable=true) private void paraglider$outline(CallbackInfo ci){var p=Minecraft.getInstance().player;if(p!=null&&Movement.get(p).state().paragliding())ci.cancel();}
}
