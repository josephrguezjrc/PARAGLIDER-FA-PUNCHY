package tictim.paraglider.fabric.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.api.movement.Movement;

/**
 * While paragliding, draws both bare arms in first person and lets vanilla draw the
 * paraglider itself on top of the main hand, instead of cancelling the off hand.
 * When not paragliding nothing changes: the held item is drawn normally by one hand.
 */
@Mixin(net.minecraft.client.renderer.ItemInHandRenderer.class)
public abstract class ParagliderHandMixin {
	@Shadow
	protected abstract void renderPlayerArm(PoseStack pose, SubmitNodeCollector collector, int light,
			float equipProgress, float swingProgress, HumanoidArm arm);

	@Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
	private void paraglider$bothArms(AbstractClientPlayer player, float partialTick, float pitch,
			InteractionHand hand, float swingProgress, ItemStack stack, float equipProgress,
			PoseStack pose, SubmitNodeCollector collector, int light, CallbackInfo ci) {
		if (!Movement.get(player).state().paragliding()) return;

		HumanoidArm arm = hand == InteractionHand.MAIN_HAND
				? player.getMainArm()
				: player.getMainArm().getOpposite();

		pose.pushPose();
		// Nudge the arm up towards the canopy. Tune these by testing in game:
		// pose.translate(0f, 0.2f, 0f);
		// pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-20f));
		renderPlayerArm(pose, collector, light, 0f, 0f, arm);
		pose.popPose();

		// The off hand only shows the arm; the main hand keeps going so vanilla draws the glider.
		if (hand == InteractionHand.OFF_HAND) ci.cancel();
	}
}
