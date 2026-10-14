package tictim.paraglider.fabric.registry;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.tags.TagKey;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;
import com.mojang.datafixers.util.Either;
public class DeferredHolder<R,T extends R> implements Supplier<T>,Holder<R> {
 final Registry<R> registry;final ResourceKey<R> key;final Supplier<T> factory;private T value;private Holder<R> holder;
 DeferredHolder(Registry<R> registry,ResourceKey<R> key,Supplier<T> factory){this.registry=registry;this.key=key;this.factory=factory;}
 public T get(){
  if(value==null){value=factory.get();holder=Registry.registerForHolder(registry,key,value);if(value instanceof tictim.paraglider.fabric.AttachmentType<?> a)a.register(key.identifier());}
  return value;
 }
 private Holder<R> holder(){get();return holder;}
 public T value(){return get();}
 public Identifier getId(){return key.identifier();}
 public boolean isBound(){return value!=null;}
 public boolean is(Identifier id){return key.identifier().equals(id);}
 public boolean is(ResourceKey<R> other){return key.equals(other);}
 public boolean is(Predicate<ResourceKey<R>> predicate){return predicate.test(key);}
 public boolean is(TagKey<R> tag){return holder().is(tag);}
 public boolean is(Holder<R> other){return holder().is(other);}
 public Stream<TagKey<R>> tags(){return holder().tags();}
 public Either<ResourceKey<R>,R> unwrap(){return Either.left(key);}
 public Optional<ResourceKey<R>> unwrapKey(){return Optional.of(key);}
 public Kind kind(){return Kind.REFERENCE;}
 public boolean canSerializeIn(HolderOwner<R> owner){return holder().canSerializeIn(owner);}
}
