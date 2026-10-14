package tictim.paraglider.fabric;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import java.util.function.Function;
public final class AttachmentType<T> {
 private final Function<Object,T> factory;
 private final MapCodec<T> codec;
 private final boolean copyOnDeath;
 private net.fabricmc.fabric.api.attachment.v1.AttachmentType<T> nativeType;
 private AttachmentType(Function<Object,T> factory,MapCodec<T> codec,boolean copyOnDeath){this.factory=factory;this.codec=codec;this.copyOnDeath=copyOnDeath;}
 public static <T> Builder<T> builder(Function<Object,T> factory){return new Builder<>(factory);}
 public void register(Identifier id){
  AttachmentRegistry.Builder<T> b=AttachmentRegistry.builder();
  if(codec!=null)b.persistent(codec.codec());
  if(copyOnDeath)b.copyOnDeath();
  nativeType=b.buildAndRegister(id);
 }
 public T get(Object target){return ((AttachmentTarget)target).getAttachedOrCreate(nativeType,()->factory.apply(target));}
 public void set(Object target,T value){((AttachmentTarget)target).setAttached(nativeType,value);}
 public static final class Builder<T> {
  private final Function<Object,T> factory;private MapCodec<T> codec;private boolean copy;
  Builder(Function<Object,T> factory){this.factory=factory;}
  public Builder<T> serialize(MapCodec<T> codec){this.codec=codec;return this;}
  public Builder<T> copyOnDeath(){copy=true;return this;}
  public AttachmentType<T> build(){return new AttachmentType<>(factory,codec,copy);}
 }
}
