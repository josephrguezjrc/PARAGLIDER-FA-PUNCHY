package tictim.paraglider.contents;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.resource.conditions.v1.*;
import net.minecraft.resources.RegistryOps;
import tictim.paraglider.api.ParagliderAPI;
import tictim.paraglider.fabric.registry.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import tictim.paraglider.config.FeatureCfg;

import java.util.Locale;

public enum ParagliderConfigCondition implements ResourceCondition {
	HEART_CONTAINER_ENABLED,
	STAMINA_VESSEL_ENABLED;

	private final MapCodec<ParagliderConfigCondition> codec = MapCodec.unit(this);
 private final ResourceConditionType<ParagliderConfigCondition> type = ResourceConditionType.create(ParagliderAPI.id(name().toLowerCase(Locale.ROOT)),codec);

	@Override public ResourceConditionType<?> getType() {
		return type;
	}

	@Override public boolean test(@NotNull RegistryOps.RegistryInfoLookup context) {
		return switch (ParagliderConfigCondition.this) {
			case HEART_CONTAINER_ENABLED -> FeatureCfg.get().enableHeartContainers();
			case STAMINA_VESSEL_ENABLED -> FeatureCfg.get().enableStaminaVessels();
		};
	}

 public static void register(){for(var c:values())ResourceConditions.register(c.type);}
}
