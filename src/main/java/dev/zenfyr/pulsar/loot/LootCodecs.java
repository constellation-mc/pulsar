package dev.zenfyr.pulsar.loot;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import dev.zenfyr.pulsar.codec.ExtraCodecs;
import java.lang.reflect.Type;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.SerializerType;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProvider;

/**
 * Minecraft's loot json features wrapped in codecs.
 */
public class LootCodecs {

  private static final GsonContextImpl lootContext =
      new GsonContextImpl(Deserializers.createLootTableSerializer().create());

  public static final Codec<LootItemCondition> ITEM_CONDITION_CODEC =
      ExtraCodecs.jsonSerializerDispatch(
          "condition",
          BuiltInRegistries.LOOT_CONDITION_TYPE.byNameCodec(),
          LootItemCondition::getType,
          SerializerType::getSerializer,
          lootContext);

  public static final Codec<LootItemFunction> ITEM_FUNCTION_CODEC =
      ExtraCodecs.jsonSerializerDispatch(
          "function",
          BuiltInRegistries.LOOT_FUNCTION_TYPE.byNameCodec(),
          LootItemFunction::getType,
          SerializerType::getSerializer,
          lootContext);

  public static final Codec<NbtProvider> NBT_PROVIDER_CODEC = ExtraCodecs.jsonSerializerDispatch(
      "type",
      BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE.byNameCodec(),
      NbtProvider::getType,
      SerializerType::getSerializer,
      lootContext);

  public static final Codec<NumberProvider> NUMBER_PROVIDER_CODEC =
      ExtraCodecs.jsonSerializerDispatch(
          "type",
          BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE.byNameCodec(),
          NumberProvider::getType,
          SerializerType::getSerializer,
          lootContext);

  public static final Codec<LootPoolEntryContainer> POOL_ENTRY_CODEC =
      ExtraCodecs.jsonSerializerDispatch(
          "type",
          BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.byNameCodec(),
          LootPoolEntryContainer::getType,
          SerializerType::getSerializer,
          lootContext);

  public static final Codec<ScoreboardNameProvider> SCORE_PROVIDER_CODEC =
      ExtraCodecs.jsonSerializerDispatch(
          "function",
          BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE.byNameCodec(),
          ScoreboardNameProvider::getType,
          SerializerType::getSerializer,
          lootContext);

  private record GsonContextImpl(Gson gson)
      implements JsonSerializationContext, JsonDeserializationContext {

    @Override
    public JsonElement serialize(Object src) {
      return gson.toJsonTree(src);
    }

    @Override
    public JsonElement serialize(Object src, Type typeOfSrc) {
      return gson.toJsonTree(src, typeOfSrc);
    }

    @Override
    public <R> R deserialize(JsonElement json, Type typeOfT) throws JsonParseException {
      return gson.fromJson(json, typeOfT);
    }
  }
}
