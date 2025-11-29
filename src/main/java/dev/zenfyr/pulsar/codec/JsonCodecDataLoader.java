package dev.zenfyr.pulsar.codec;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import java.util.Map;
import java.util.function.BiConsumer;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class JsonCodecDataLoader<T> extends SimpleJsonResourceReloadListener
    implements IdentifiableResourceReloadListener {

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

  private final ResourceLocation location;
  private final Codec<T> codec;

  public JsonCodecDataLoader(ResourceLocation location, Codec<T> codec) {
    super(new Gson(), location.toString().replace(':', '/'));
    this.location = location;
    this.codec = codec;
  }

  @Override
  public final ResourceLocation getFabricId() {
    return this.location;
  }

  @Override
  protected void apply(
      Map<ResourceLocation, JsonElement> prepared,
      ResourceManager manager,
      ProfilerFiller profiler) {
    this.apply(
        Maps.transformValues(
            prepared,
            input -> this.codec.parse(JsonOps.INSTANCE, input).getOrThrow(false, string -> {
              throw new JsonParseException(string);
            })),
        manager);
  }

  protected abstract void apply(Map<ResourceLocation, T> parsed, ResourceManager manager);
}
