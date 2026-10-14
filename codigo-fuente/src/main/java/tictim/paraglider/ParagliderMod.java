package tictim.paraglider;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tictim.paraglider.api.ParagliderAPI;
import tictim.paraglider.api.bargain.BargainPreview;
import tictim.paraglider.api.bargain.BargainType;
import tictim.paraglider.bargain.BargainRecipeChecker;
import tictim.paraglider.config.*;
import tictim.paraglider.contents.BargainTypeRegistry;
import tictim.paraglider.contents.Contents;
import tictim.paraglider.contents.ParagliderVillageStructures;
import tictim.paraglider.contents.recipe.WaterBottleIngredientType;
import tictim.paraglider.impl.DefaultParagliderItemCapability;
import tictim.paraglider.impl.ParagliderCauldronInteraction;
import tictim.paraglider.impl.movement.PlayerStateConnectionMap;
import tictim.paraglider.impl.movement.PlayerStateMap;
import tictim.paraglider.impl.movement.PlayerStateMapLoader;
import tictim.paraglider.impl.stamina.StaminaEfficiencyLogicHandlerImpl;
import tictim.paraglider.impl.stamina.StaminaEfficiencyLogicLoader;
import tictim.paraglider.impl.stamina.StaminaLoader;
import tictim.paraglider.network.ParagliderNetwork;
import tictim.paraglider.network.ParagliderNetworkImpl;
import tictim.paraglider.plugin.ParagliderPluginLoader;
import tictim.paraglider.wind.WindSource;
import tictim.paraglider.wind.WindSourceRegistry;

public class ParagliderMod implements net.fabricmc.api.ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("Paraglider");

	private static ParagliderMod instance;

	public static @NotNull ParagliderMod instance() {
		if (instance == null) throw new IllegalStateException("Mod instance not ready yet");
		return instance;
	}

	{
		if (instance != null) throw new IllegalStateException("Paraglider mod instantiated twice");
		instance = this;
	}

	private final CommonConfig commonCfg = new CommonConfig();
	private final Contents contents;
	private final ServerConfig config = new ServerConfig();

	private final ParagliderNetworkImpl network;
	private final FabricParagliderPluginLoader pluginLoader = new FabricParagliderPluginLoader();

	private final PlayerStateMapConfig stateMapConfig;
	private final PlayerStateConnectionMap connectionMap;

	private final WindSourceRegistry windSourceRegistry = new WindSourceRegistry();

	private final @Nullable String staminaFactoryOrigin;

	@ApiStatus.Internal
	public @Nullable IClient client;


 public ParagliderMod() {
  this.contents=new Contents();
  ParagliderAPI.setMovementSupplier(p -> tictim.paraglider.fabric.Attachments.get(p,Contents.get().playerMovement()));
  ParagliderAPI.setStaminaSupplier(p -> tictim.paraglider.fabric.Attachments.get(p,Contents.get().playerMovement()).stamina());
  ParagliderAPI.setVesselContainerSupplier(p -> tictim.paraglider.fabric.Attachments.get(p,Contents.get().vesselContainer()));
  var states=PlayerStateMapLoader.loadStates();
  this.stateMapConfig=new PlayerStateMapConfig(states.getFirst());this.connectionMap=states.getSecond();
  var stamina=StaminaLoader.loadStaminaFactory();
  ParagliderAPI.setStaminaFactory(stamina.getFirst());this.staminaFactoryOrigin=stamina.getSecond();
  ParagliderAPI.setStaminaEfficiencyLogicHandler(new StaminaEfficiencyLogicHandlerImpl(StaminaEfficiencyLogicLoader.loadStaminaEfficiencyLogics()));
  ParagliderAPI.setDefaultParagliderItemCapability(new DefaultParagliderItemCapability());
  this.network=new ParagliderNetworkImpl();
 }
 @Override public void onInitialize(){
  this.contents.registerAll();
  tictim.paraglider.contents.ParagliderParticles.register();
  var configs=fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry.INSTANCE;
  configs.register(ParagliderAPI.MODID,ModConfig.Type.COMMON,this.commonCfg.spec);
  configs.register(ParagliderAPI.MODID,ModConfig.Type.SERVER,this.config.spec);
  configs.register(ParagliderAPI.MODID,ModConfig.Type.COMMON,this.stateMapConfig.spec,PlayerStateMapConfig.FILENAME);
  net.fabricmc.fabric.api.event.registry.DynamicRegistries.register(BargainTypeRegistry.REGISTRY_KEY,BargainType.CODEC);
  net.fabricmc.fabric.api.event.registry.DynamicRegistries.registerSynced(WindSourceRegistry.REGISTRY_KEY,WindSource.CODEC,WindSource.CODEC);
  net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry.register(EntityType.PLAYER,
   net.minecraft.world.entity.player.Player.createAttributes().add(contents.maxStamina()).add(contents.staminaEfficiency())
   .add(contents.staminaRecovery()).add(contents.paraglidingStaminaEfficiency()).add(contents.runningStaminaEfficiency()).add(contents.swimmingStaminaEfficiency()));
  var cauldron=CauldronInteraction.WATER.map();cauldron.put(contents.paraglider(),ParagliderCauldronInteraction.INSTANCE);cauldron.put(contents.dekuLeaf(),ParagliderCauldronInteraction.INSTANCE);
  tictim.paraglider.fabric.FabricEvents.register();
  tictim.paraglider.fabric.loot.ParagliderLootManager.register();
 }

	public @NotNull Cfg getConfig() {
		return this.config;
	}

	public @NotNull DebugCfg getDebugConfig() {
		return this.commonCfg;
	}

	public @NotNull FeatureCfg getFeatureConfig() {
		return this.commonCfg;
	}

	public @NotNull Contents getContents() {
		return this.contents;
	}

	public @NotNull ParagliderNetwork getNetwork() {
		return this.network;
	}

	public @NotNull ParagliderPluginLoader getPluginLoader() {
		return this.pluginLoader;
	}

	public @NotNull PlayerStateMap getPlayerStateMap() {
		if (this.client != null) {
			PlayerStateMap m = this.client.getSyncedStateMap();
			if (m != null) return m;
		}
		return getLocalPlayerStateMap();
	}

	public @NotNull PlayerStateMap getLocalPlayerStateMap() {
		return this.stateMapConfig.stateMap();
	}

	public @NotNull PlayerStateConnectionMap getPlayerConnectionMap() {
		return this.connectionMap;
	}

	public @NotNull PlayerStateMapConfig getPlayerStateMapConfig() {
		return this.stateMapConfig;
	}

	public @NotNull WindSourceRegistry windSourceRegistry() {
		return this.windSourceRegistry;
	}

	public @Nullable String staminaFactoryOrigin() {
		return this.staminaFactoryOrigin;
	}

	@ApiStatus.Internal
	public interface IClient {
		@Nullable PlayerStateMap getSyncedStateMap();
	}
}
