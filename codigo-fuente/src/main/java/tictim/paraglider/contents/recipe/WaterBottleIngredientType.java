package tictim.paraglider.contents.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.fabricmc.fabric.api.recipe.v1.ingredient.*;
import net.minecraft.resources.Identifier;
import net.minecraft.network.RegistryFriendlyByteBuf;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public enum WaterBottleIngredientType implements CustomIngredient {
	INSTANCE;

 private final CustomIngredientSerializer<WaterBottleIngredientType> type=new CustomIngredientSerializer<>(){
 public Identifier getIdentifier(){return Identifier.fromNamespaceAndPath("paraglider","water_bottle");}
 public MapCodec<WaterBottleIngredientType> getCodec(){return MapCodec.unit(INSTANCE);}
 public StreamCodec<RegistryFriendlyByteBuf,WaterBottleIngredientType> getPacketCodec(){return StreamCodec.unit(INSTANCE);}
 };

	@Override public boolean test(@NotNull ItemStack stack) {
		return stack.is(Items.POTION) &&
				stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER);
	}

	@Override public @NotNull Stream<Holder<Item>> getMatchingItems() {
		return Stream.of(BuiltInRegistries.ITEM.wrapAsHolder(Items.POTION));
	}

	@Override public boolean requiresTesting() {
		return true;
	}

	@Override public @NotNull CustomIngredientSerializer<?> getSerializer() {
		return this.type;
	}

	@Override public @NotNull SlotDisplay toDisplay() {
		return new SlotDisplay.ItemStackSlotDisplay(PotionContents.createItemStack(Items.POTION, Potions.WATER));
	}

	@Override public String toString() {
		return "WaterBottleIngredientType";
	}
}
