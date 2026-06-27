package dev.zenfyr.pulsar.api.codec;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import java.util.*;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * JSON data reload listener which uses a codec to parse the files.
 * @param <T> Deserialized type
 */
public abstract class JsonCodecDataLoader<T> extends SimpleJsonResourceReloadListener {

  @Contract("_, _, _ -> new")
  public static <T> @NotNull JsonCodecDataLoader<T> simple(
      ResourceLocation identifier, Codec<T> codec, BiConsumer<ResourceLocation, T> consumer) {
    return new JsonCodecDataLoader<T>(identifier, codec) {
      @Override
      protected void apply(Map<ResourceLocation, T> parsed, ResourceManager manager) {
        parsed.forEach(consumer);
      }
    };
  }

  private final Codec<T> codec;
  private final DynamicOps<JsonElement> dynamicOps;

  private Map<ResourceLocation, T> temp;

  public JsonCodecDataLoader(ResourceLocation location, Codec<T> codec) {
    this(JsonOps.INSTANCE, location, codec);
  }

  public JsonCodecDataLoader(
      DynamicOps<JsonElement> dynamicOps, ResourceLocation location, Codec<T> codec) {
    super(new Gson(), location.toString().replace(':', '/'));
    this.dynamicOps = dynamicOps;
    this.codec = codec;
  }

  @Override
  protected Map<ResourceLocation, JsonElement> prepare(
      ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    Map<ResourceLocation, T> delegate = new HashMap<>();
    Map<ResourceLocation, JsonElement> map = new MapFunction<>((location1, jsonElement) -> {
      T entry = this.codec.parse(this.dynamicOps, jsonElement).getOrThrow(false, string -> {
        throw new JsonParseException(string);
      });
      delegate.put(location1, entry);
    });
    scanDirectory(resourceManager, this.directory, this.gson, map);
    this.temp = delegate;
    return map;
  }

  @Override
  protected final void apply(
      Map<ResourceLocation, JsonElement> prepared,
      ResourceManager manager,
      ProfilerFiller profiler) {
    this.apply(this.temp, manager);
    this.temp = null;
  }

  protected abstract void apply(Map<ResourceLocation, T> parsed, ResourceManager manager);

  static final class MapFunction<K, V> extends HashMap<K, V> {

    private final BiConsumer<K, V> consumer;

    MapFunction(BiConsumer<K, V> consumer) {
      this.consumer = consumer;
    }

    @Override
    public V put(K key, V value) {
      V val = super.put(key, value);
      this.consumer.accept(key, value);
      return val;
    }
  }
}
