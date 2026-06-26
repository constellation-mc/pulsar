package dev.zenfyr.pulsar.api.codec;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import dev.zenfyr.pulsar.api.util.ColorUtil;
import dev.zenfyr.pulsar.api.util.Utilities;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import net.minecraft.world.entity.ai.behavior.ShufflingList;
import net.minecraft.world.level.storage.loot.Serializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ExtraCodecs {

  /**
   * a color codec encoded as either an integer, or an array of RGB values.
   */
  public static final Codec<Integer> COLOR = either(
          Codec.INT, Codec.intRange(0, 255).listOf())
      .comapFlatMap(
          e -> e.map(DataResult::success, integers -> {
            if (integers.size() != 3)
              return DataResult.error(() -> "colors array must contain exactly 3 colors (RGB)");
            return DataResult.success(
                ColorUtil.toColor(integers.get(0), integers.get(1), integers.get(2)));
          }),
          Either::left);

  /**
   * a 'safe' either {@link Codec}, which returns errors from both codecs if they fail.
   */
  @Contract(value = "_, _ -> new", pure = true)
  public static <F, S> @NotNull Codec<Either<F, S>> either(
      final Codec<F> first, final Codec<S> second) {
    return new SafeEitherCodec<>(first, second);
  }

  /**
   * a 'safe' either {@link MapCodec}, which returns errors from both codecs if they fail.
   */
  @Contract("_, _ -> new")
  public static <F, S> @NotNull MapCodec<Either<F, S>> either(
      final MapCodec<F> first, final MapCodec<S> second) {
    return new SafeEitherMapCodec<>(first, second);
  }

  /**
   * Unlike the vanilla alternative, ({@link Codec#optionalField(String, Codec)}) this codec does not ignore exceptions.
   */
  public static <F> MapCodec<F> optional(
      final String name, final Codec<F> elementCodec, F defaultValue) {
    return optional(name, elementCodec)
        .xmap(
            f -> f.orElse(defaultValue),
            f -> Objects.equals(f, defaultValue) ? Optional.empty() : Optional.of(f));
  }

  /**
   * Unlike the vanilla alternative, ({@link Codec#optionalField(String, Codec)}) this codec does not ignore exceptions.
   */
  @Contract("_, _ -> new")
  public static <F> @NotNull MapCodec<Optional<F>> optional(
      final String name, final Codec<F> elementCodec) {
    return new SafeOptionalCodec<>(name, elementCodec);
  }

  /**
   * A list codec which accepts both lists and singular entries.
   */
  public static <T> Codec<List<T>> list(Codec<T> codec) {
    return either(codec, codec.listOf())
        .xmap(e -> e.map(ImmutableList::of, Function.identity()), Either::right);
  }

  /**
   * A shuffling list codec which accepts both lists and singular entries.
   */
  public static <T> Codec<ShufflingList<T>> weightedList(Codec<T> codec) {
    return either(codec, ShufflingList.codec(codec))
        .xmap(
            e -> e.map(
                entry -> {
                  ShufflingList<T> list = new ShufflingList<>();
                  list.add(entry, 1);
                  return list;
                },
                Function.identity()),
            Either::right);
  }

  /**
   * a {@link Codec}, which uses a bidirectional map to en/decode objects.
   */
  public static <K, V> Codec<V> mapLookup(@NotNull Codec<K> keyCodec, @NotNull BiMap<K, V> lookup) {
    return keyCodec.flatXmap(
        key -> Optional.ofNullable(lookup.get(key))
            .map(DataResult::success)
            .orElseGet(() -> DataResult.error(() -> "Unknown type: %s".formatted(key))),
        eventType -> Optional.ofNullable(lookup.inverse().get(eventType))
            .map(DataResult::success)
            .orElseGet(() -> DataResult.error(() -> "Unknown type: %s".formatted(eventType))));
  }

  /**
   * an {@link Enum} codec, which uses enum constant names to en/decode values.
   */
  @ApiStatus.Experimental
  public static <T extends Enum<T>> Codec<T> enumCodec(Class<T> cls) {
    return Codec.STRING.comapFlatMap(
        string -> {
          try {
            return DataResult.success(Enum.valueOf(cls, string.toUpperCase(Locale.ROOT)));
          } catch (IllegalArgumentException e) {
            return DataResult.error(() -> "No such enum constant %s!".formatted(string));
          }
        },
        t -> t.name().toLowerCase(Locale.ROOT));
  }

  @ApiStatus.Experimental
  public static <T, C extends JsonSerializationContext & JsonDeserializationContext> @NotNull Codec<T> fromJsonSerializer(Serializer<T> serializer, C context) {
    return fromJsonSerializer(serializer, context, context);
  }

  @ApiStatus.Experimental
  public static <T> @NotNull Codec<T> fromJsonSerializer(
      Serializer<T> serializer,
      JsonSerializationContext serializationContext,
      JsonDeserializationContext deserializationContext) {
    Codec<T> codec = net.minecraft.util.ExtraCodecs.JSON.flatXmap(
        element -> {
          if (!element.isJsonObject())
            return DataResult.error(() -> "Not a JsonObject %s".formatted(element));
          return DataResult.success(
              serializer.deserialize(element.getAsJsonObject(), deserializationContext));
        },
        t -> {
          JsonObject object = new JsonObject();
          serializer.serialize(object, t, serializationContext);
          return DataResult.success(object);
        });
    return net.minecraft.util.ExtraCodecs.catchDecoderException(codec);
  }

  public static <K, V, C extends JsonSerializationContext & JsonDeserializationContext> @NotNull Codec<V> jsonSerializerDispatch(
          final String typeKey,
          Codec<K> keyCodec,
          final Function<? super V, ? extends K> type,
          final Function<? super K, ? extends Serializer<? extends V>> codec,
          C context) {
    return jsonSerializerDispatch(typeKey, keyCodec, type, codec, context, context);
  }

  public static <K, V> @NotNull Codec<V> jsonSerializerDispatch(
      final String typeKey,
      Codec<K> keyCodec,
      final Function<? super V, ? extends K> type,
      final Function<? super K, ? extends Serializer<? extends V>> codec,
      JsonSerializationContext serializationContext,
      JsonDeserializationContext deserializationContext) {
    Codec<V> cc = net.minecraft.util.ExtraCodecs.JSON.flatXmap(
        element -> {
          if (!element.isJsonObject())
            return DataResult.error(() -> "'%s' not a JsonObject".formatted(element));
          JsonObject object = element.getAsJsonObject();
          if (object.get(typeKey) == null)
            return DataResult.error(() -> "Missing required '%s' field!".formatted(typeKey));

          var keyRes = keyCodec.parse(JsonOps.INSTANCE, object.get(typeKey));
          if (keyRes.error().isPresent()) return keyRes.map(identifier -> null);

          var decoder = codec.apply(keyRes.result().orElseThrow());
          return DataResult.success(decoder.deserialize(object, deserializationContext));
        },
        v -> {
          K key = type.apply(v);
          var kr = keyCodec.encodeStart(JsonOps.INSTANCE, key);
          if (kr.error().isPresent()) return kr.map(element -> null);

          JsonObject object = new JsonObject();
          object.add(typeKey, kr.result().orElseThrow());

          var encoder = codec.apply(key);
          encoder.serialize(object, Utilities.cast(v), serializationContext);

          return DataResult.success(object);
        });
    return net.minecraft.util.ExtraCodecs.catchDecoderException(cc);
  }

  @ApiStatus.Experimental
  public static <T> @NotNull Serializer<T> toJsonSerializer(Codec<T> codec) {
    var mcc =
        codec instanceof MapCodec.MapCodecCodec<T> glue ? glue.codec() : codec.fieldOf("value");
    return new Serializer<>() {
      @Override
      public void serialize(JsonObject json, T object, JsonSerializationContext context) {
        var s = mcc.encode(object, JsonOps.INSTANCE, JsonOps.INSTANCE.mapBuilder());
        s.build(json).getOrThrow(false, string -> {
          throw new JsonParseException(string);
        });
      }

      @Override
      public T deserialize(JsonObject json, JsonDeserializationContext context) {
        return mcc.decode(
                JsonOps.INSTANCE, JsonOps.INSTANCE.getMap(json).getOrThrow(false, string -> {
                  throw new IllegalStateException(string);
                }))
            .getOrThrow(false, string -> {
              throw new JsonParseException(string);
            });
      }
    };
  }
}
