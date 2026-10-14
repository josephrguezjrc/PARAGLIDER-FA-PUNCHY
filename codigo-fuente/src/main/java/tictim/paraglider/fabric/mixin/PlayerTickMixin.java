package tictim.paraglider.fabric.mixin;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(Player.class)
public abstract class PlayerTickMixin {
 @Inject(method="tick",at=@At("TAIL")) private void paraglider$tick(CallbackInfo ci){tictim.paraglider.event.ParagliderEventHandler.afterPlayerTick((Player)(Object)this);}
}
