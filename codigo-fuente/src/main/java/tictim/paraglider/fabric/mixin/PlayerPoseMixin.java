package tictim.paraglider.fabric.mixin;
import net.minecraft.client.model.HumanoidModel;import net.minecraft.client.renderer.entity.state.*;import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(HumanoidModel.class) public abstract class PlayerPoseMixin {
 @Inject(method="setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",at=@At("TAIL"))
 private void paraglider$pose(HumanoidRenderState state,CallbackInfo ci){
  if(state instanceof AvatarRenderState && state.getMainHandItemStack().is(tictim.paraglider.contents.ParagliderTags.PARAGLIDERS) && tictim.paraglider.ParagliderUtils.getCaps(state.getMainHandItemStack()).isParagliding(state.getMainHandItemStack()))tictim.paraglider.client.ParaglidingArmPose.applyTransform((HumanoidModel<?>)(Object)this,state,state.mainArm);
 }
}
