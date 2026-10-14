package tictim.paraglider.event;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import tictim.paraglider.ParagliderMod;
import tictim.paraglider.api.movement.Movement;
import tictim.paraglider.api.stamina.Stamina;
import tictim.paraglider.bargain.BargainHandler;
import tictim.paraglider.contents.item.consumeeffect.GiveExtraStaminaConsumeEffect;
import tictim.paraglider.contents.item.consumeeffect.GiveStaminaEfficiencyConsumeEffect;
import tictim.paraglider.contents.item.consumeeffect.RestoreStaminaConsumeEffect;
import tictim.paraglider.contents.mobeffect.StaminaEfficiencyMobEffect;
import tictim.paraglider.impl.movement.PlayerMovement;
import tictim.paraglider.impl.movement.ServerPlayerMovement;
import tictim.paraglider.network.ParagliderNetwork;

import java.text.DecimalFormat;

import static tictim.paraglider.api.ParagliderAPI.MODID;

public final class ParagliderEventHandler {
	private ParagliderEventHandler() {}

 public static boolean cancelOffhand(Player player,InteractionHand hand){return hand==InteractionHand.OFF_HAND && Movement.get(player).state().paragliding();}

	public static void afterPlayerTick(Player player) {
		if (Movement.get(player) instanceof PlayerMovement playerMovement)
			{playerMovement.update();if(playerMovement.state().paragliding()&&player.isUsingItem())player.stopUsingItem();}
	}

	public static void onStartTracking(net.minecraft.world.entity.Entity target,ServerPlayer player) {
		if (!(target instanceof Player tracking))
			return;
		ParagliderNetwork.get().syncRemoteMovement(tracking, player, Movement.get(tracking).state().id());
	}

	public static void onPlayerChangedDimension(Player player) {
		if (Movement.get(player) instanceof ServerPlayerMovement serverPlayerMovement) {
			serverPlayerMovement.markForSync();
		}
	}

	public static void afterServerTick() {
		BargainHandler.update();
	}

	public static void onLogin(ServerPlayer player) {
		{
			ParagliderNetwork.get().syncStateMap(player, ParagliderMod.instance().getLocalPlayerStateMap());
			if (Movement.get(player) instanceof ServerPlayerMovement serverPlayerMovement) {
				serverPlayerMovement.markForSync();
			}
		}
	}

	private static final DecimalFormat D0 = new DecimalFormat("0.#");
	private static final DecimalFormat PCT = new DecimalFormat("+0%");

	public static void onAddAttributeTooltips(net.minecraft.world.item.ItemStack stack,java.util.List<Component> lines) {
		Consumable consumable = stack.get(DataComponents.CONSUMABLE);
		if (consumable == null) return;
		for (ConsumeEffect effect : consumable.onConsumeEffects()) {
			switch (effect) {
				case GiveExtraStaminaConsumeEffect(double amount) ->
						lines.add(Component.translatable("tooltip.paraglider.give_extra_stamina",
								Component.literal(D0.format(amount / Stamina.STAMINA_PER_WHEEL))
										.withStyle(ChatFormatting.YELLOW)
						).withStyle(ChatFormatting.GREEN));
				case RestoreStaminaConsumeEffect(double amount) ->
						lines.add(Component.translatable("tooltip.paraglider.restore_stamina",
								Component.literal(D0.format(amount / Stamina.STAMINA_PER_WHEEL))
										.withStyle(ChatFormatting.YELLOW)
						).withStyle(ChatFormatting.GREEN));
				case GiveStaminaEfficiencyConsumeEffect(int level, int duration) ->
						lines.add(Component.translatable("tooltip.paraglider.give_stamina_efficiency",
								Component.literal(PCT.format(
										StaminaEfficiencyMobEffect.EFFICIENCY_PER_LEVEL * (level + 1)
								)).withStyle(ChatFormatting.YELLOW),
								Component.literal(D0.format(duration / 20.0)).withStyle(ChatFormatting.YELLOW)
						).withStyle(ChatFormatting.GREEN));
				default -> {}
			}
		}
	}
}
