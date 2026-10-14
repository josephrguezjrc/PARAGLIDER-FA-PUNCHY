package tictim.paraglider.fabric.mixin;
import net.minecraft.client.renderer.item.properties.conditional.*;import net.minecraft.resources.Identifier;import net.minecraft.util.ExtraCodecs;import com.mojang.serialization.MapCodec;import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.gen.Accessor;
@Mixin(ConditionalItemModelProperties.class) public interface ConditionalPropertiesAccessor {
 @Accessor("ID_MAPPER") static ExtraCodecs.LateBoundIdMapper<Identifier,MapCodec<? extends ConditionalItemModelProperty>> paraglider$getMapper(){throw new AssertionError("Mixin accessor not applied");}
}
