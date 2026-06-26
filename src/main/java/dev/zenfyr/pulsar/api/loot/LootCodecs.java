package dev.zenfyr.pulsar.api.loot;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProviders;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProvider;
import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProviders;

public class LootCodecs {

  public static final Codec<LootItemCondition> ITEM_CONDITION_CODEC =
      LootItemCondition.DIRECT_CODEC;

  public static final Codec<LootItemFunction> ITEM_FUNCTION_CODEC = LootItemFunctions.ROOT_CODEC;

  public static final Codec<NbtProvider> NBT_PROVIDER_CODEC = NbtProviders.CODEC;

  public static final Codec<NumberProvider> NUMBER_PROVIDER_CODEC = NumberProviders.CODEC;

  public static final Codec<LootPoolEntryContainer> POOL_ENTRY_CODEC = LootPoolEntries.CODEC;

  public static final Codec<ScoreboardNameProvider> SCORE_PROVIDER_CODEC =
      ScoreboardNameProviders.CODEC;
}
