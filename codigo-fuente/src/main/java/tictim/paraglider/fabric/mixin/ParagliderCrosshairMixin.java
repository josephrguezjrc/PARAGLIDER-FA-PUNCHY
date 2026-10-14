package tictim.paraglider.fabric.mixin;
import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.Minecraft;import tictim.paraglider.api.movement.Movement;
@Mixin(net.minecraft.client.gui.Gui.class) public class ParagliderCrosshairMixin {
 @Inject(method="renderCrosshair",at=@At("HEAD"),cancellable=true) private void paraglider$crosshair(CallbackInfo ci){if(Minecraft.getInstance().screen instanceof tictim.paraglider.client.screen.BargainScreen)ci.cancel();}
}
