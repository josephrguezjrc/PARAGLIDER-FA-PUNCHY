package tictim.paraglider.contents.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import tictim.paraglider.api.ParagliderAPI;
import tictim.paraglider.api.movement.Movement;
import tictim.paraglider.config.Cfg;
import tictim.paraglider.impl.movement.ClientPlayerMovement;
import tictim.paraglider.impl.movement.PlayerMovementValues;

import java.util.Optional;
import java.util.function.Consumer;

public class ParagliderItem extends Item {
	public ParagliderItem(Properties p) {
		super(p
				.durability(100)
				.repairable(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM,net.minecraft.resources.Identifier.fromNamespaceAndPath("c","leathers")))
				.component(DataComponents.USE_COOLDOWN,
						new UseCooldown(PlayerMovementValues.PARAGLIDER_ITEM_COOLDOWN / 20.f,
								Optional.of(ParagliderAPI.PARAGLIDER_COOLDOWN_GROUP))
				)
		);
	}

	@Override public @NonNull InteractionResult use(
			@NonNull Level level,
			@NonNull Player player,
			@NonNull InteractionHand hand) {
		if (hand == InteractionHand.OFF_HAND) return InteractionResult.PASS;

		if (level.isClientSide()) {
			if (Movement.get(player) instanceof ClientPlayerMovement m) {
				m.setAutoParagliding(false);
				if (m.clientParagliding()) m.stopUsingParaglider();
				else m.useParaglider();
			}
		}

		return InteractionResult.CONSUME;
	}

	@SuppressWarnings("deprecation")
	@Override public void appendHoverText(
			@NotNull ItemStack stack, @NotNull TooltipContext context,
			@NotNull TooltipDisplay tooltipDisplay, @NotNull Consumer<Component> tooltipAdder,
			@NotNull TooltipFlag flag) {
		if (stack.isDamaged() && stack.getMaxDamage() <= stack.getDamageValue()) {
			tooltipAdder.accept(Component.translatable("tooltip.paraglider.paraglider_broken")
					.setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
		}
	}

}
