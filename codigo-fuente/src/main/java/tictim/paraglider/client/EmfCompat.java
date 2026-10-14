package tictim.paraglider.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.player.Player;
import tictim.paraglider.api.movement.Movement;

/**
 * Pauses EMF (Entity Model Features) custom animations while the player is paragliding.
 * Without this, resource packs such as Fresh Animations' FA+Player override the arm pose
 * that ParaglidingArmPose applies in PlayerPoseMixin.
 */
public final class EmfCompat {
	private EmfCompat() {}

	private static final String EMF_MOD_ID = "entity_model_features";

	public static void init() {
		if (!FabricLoader.getInstance().isModLoaded(EMF_MOD_ID)) return;
		Impl.register();
	}

	// Kept in a separate class so the EMF classes are only loaded when EMF is present.
	private static final class Impl {
		static void register() {
			traben.entity_model_features.EMFAnimationApi.registerPauseCondition(emfEntity ->
					emfEntity instanceof Player player && isParagliding(player));
		}

		private static boolean isParagliding(Player player) {
			try {
				return Movement.get(player).state().paragliding();
			} catch (RuntimeException e) {
				return false;
			}
		}
	}
}
