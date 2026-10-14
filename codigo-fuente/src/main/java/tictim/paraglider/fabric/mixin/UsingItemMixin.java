package tictim.paraglider.fabric.mixin;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(LivingEntity.class)
public abstract class UsingItemMixin {
 @Inject(method="startUsingItem",at=@At("HEAD"),cancellable=true)
 private void paraglider$preventUse(InteractionHand hand,CallbackInfo ci){if((Object)this instanceof Player p && tictim.paraglider.api.movement.Movement.get(p).state().paragliding())ci.cancel();}
}
