package tictim.paraglider;

import com.google.common.reflect.TypeToken;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import tictim.paraglider.api.ParagliderAPI;
import tictim.paraglider.client.ParagliderGuiLayers;
import tictim.paraglider.client.ParaglidingArmPose;
import tictim.paraglider.client.ParaglidingItemProperty;
import tictim.paraglider.client.WindParticleProvider;
import tictim.paraglider.client.settings.ParagliderClientSettings;
import tictim.paraglider.client.settings.ParagliderClientSettingsIO;
import tictim.paraglider.contents.ParagliderTags;
import tictim.paraglider.impl.movement.PlayerStateMap;
import tictim.paraglider.impl.stamina.StaminaLoader;

import java.util.Objects;

public class ParagliderClientMod implements ParagliderMod.IClient, net.fabricmc.api.ClientModInitializer {
	private static ParagliderClientMod instance;

	public static @NotNull ParagliderClientMod instance() {
		if (instance == null) throw new IllegalStateException("No client");
		return instance;
	}

	{
		if (instance != null) throw new IllegalStateException("Paraglider mod instantiated twice");
		instance = this;
	}

	private final boolean removeStaminaWheel;
	private final @Nullable String staminaWheelRemoverId;
	private @Nullable ParagliderClientSettings clientSettings;
	private @Nullable PlayerStateMap syncedStateMap;
	private @Nullable KeyMapping paragliderSettingsKey;


 public ParagliderClientMod(){
  var pair=StaminaLoader.loadStaminaWheelRemoverId();this.removeStaminaWheel=pair.getFirst();this.staminaWheelRemoverId=pair.getSecond();
 }
 @Override public void onInitializeClient(){
  ParagliderMod.instance().client=this;
  tictim.paraglider.fabric.mixin.ConditionalPropertiesAccessor.paraglider$getMapper().put(ParagliderAPI.id("paragliding"),ParaglidingItemProperty.CODEC);
  this.paragliderSettingsKey=net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding(new KeyMapping("key.paraglider.paragliderSettings",InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_P,KeyMapping.Category.MISC));
  net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry.addLast(ParagliderAPI.id("stamina_wheel"),ParagliderGuiLayers::renderStaminaWheel);
  net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry.addLast(ParagliderAPI.id("movement_debug"),ParagliderGuiLayers::renderMovementDebug);
  net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry.getInstance().register(WindParticleProvider.PARTICLE_TYPE,WindParticleProvider::new);
  tictim.paraglider.fabric.FabricClientEvents.register();
  tictim.paraglider.client.EmfCompat.init();
  ParagliderClientSettingsIO.load(null);
 }

	public @NotNull ParagliderClientSettings getSettings() {
		return Objects.requireNonNullElse(this.clientSettings, ParagliderClientSettings.DEFAULT);
	}

	public void setSettings(@Nullable ParagliderClientSettings settings) {
		this.clientSettings = settings;
	}

	public @NotNull KeyMapping getParagliderSettingsKey() {
		if (this.paragliderSettingsKey == null)
			throw new IllegalStateException("paragliderSettingsKey is not available yet");
		return this.paragliderSettingsKey;
	}

	@Override public @Nullable PlayerStateMap getSyncedStateMap() {
		return this.syncedStateMap;
	}
	public void setSyncedStateMap(@Nullable PlayerStateMap stateMap) {
		this.syncedStateMap = stateMap;
	}

	public boolean removeStaminaWheel() {
		return this.removeStaminaWheel;
	}

	public @Nullable String staminaWheelRemoverId() {
		return this.staminaWheelRemoverId;
	}
}
