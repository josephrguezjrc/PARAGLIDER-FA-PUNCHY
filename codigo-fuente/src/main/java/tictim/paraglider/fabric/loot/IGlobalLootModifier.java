package tictim.paraglider.fabric.loot;
import com.mojang.serialization.MapCodec;
public interface IGlobalLootModifier { MapCodec<? extends IGlobalLootModifier> codec(); }
