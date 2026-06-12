package dev.zenfyr.pulsar.codec;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class JsonCodecDataLoader<T> extends SimpleJsonResourceReloadListener<T> {

  @Contract("_, _, _ -> new")
  public static <T> @NotNull JsonCodecDataLoader<T> simple(
      Identifier identifier, Codec<T> codec, BiConsumer<Identifier, T> consumer) {
    return new JsonCodecDataLoader<T>(identifier, codec) {
      @Override
      protected void apply(Map<Identifier, T> parsed, ResourceManager manager) {
        parsed.forEach(consumer);
      }
    };
  }

  public JsonCodecDataLoader(Identifier identifier, Codec<T> codec) {
    this(JsonOps.INSTANCE, identifier, codec);
  }

  public JsonCodecDataLoader(
      DynamicOps<JsonElement> dynamicOps, Identifier identifier, Codec<T> codec) {
    super(dynamicOps, codec, FileToIdConverter.json(identifier.toString().replace(':', '/')));
  }

  @Override
  protected final void apply(
      Map<Identifier, T> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    this.apply(object, resourceManager);
  }

  protected abstract void apply(Map<Identifier, T> parsed, ResourceManager manager);
}
