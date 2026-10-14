package tictim.paraglider.fabric.mixin;
import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.Minecraft;import tictim.paraglider.api.movement.Movement;
import net.minecraft.client.player.AbstractClientPlayer;import net.minecraft.world.InteractionHand;import net.minecraft.world.item.ItemStack;import com.mojang.blaze3d.vertex.PoseStack;import net.minecraft.client.renderer.SubmitNodeCollector;
@Mixin(net.minecraft.client.renderer.ItemInHandRenderer.class) public class ParagliderHandMixin {
 @Inject(method="renderArmWithItem",at=@At("HEAD"),cancellable=true) private void paraglider$offhand(AbstractClientPlayer p,float partial,float pitch,InteractionHand hand,float swing,ItemStack stack,float equipped,PoseStack pose,SubmitNodeCollector collector,int light,CallbackInfo ci){if(hand==InteractionHand.OFF_HAND&&Movement.get(p).state().paragliding())ci.cancel();}
}
