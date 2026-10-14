package tictim.paraglider.contents;
public final class ParagliderParticles {
 public static final net.minecraft.core.particles.SimpleParticleType WIND=net.fabricmc.fabric.api.particle.v1.FabricParticleTypes.simple(false);
 public static void register(){net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.PARTICLE_TYPE,tictim.paraglider.api.ParagliderAPI.id("wind"),WIND);}
}
