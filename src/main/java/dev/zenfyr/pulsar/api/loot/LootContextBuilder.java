package dev.zenfyr.pulsar.api.loot;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * A loot context builder with additional utility methods.
 */
public class LootContextBuilder {

  LootParams.Builder builder;

  private LootContextBuilder(Level world) {
    this.builder = new LootParams.Builder((ServerLevel) world);
  }

  public static LootContextBuilder create(Level level) {
    return new LootContextBuilder(level);
  }

  public static LootContext empty(Level level) {
    return empty(level, null);
  }

  public static LootContext empty(Level level, @Nullable ResourceLocation sequenceKey) {
    return build(create(level).build(LootContextParamSets.EMPTY), sequenceKey);
  }

  public static LootContext block(Level level, BuilderFunction function) {
    return block(level, function, null);
  }

  public static LootContext block(
      Level level, BuilderFunction function, @Nullable ResourceLocation sequenceKey) {
    return build(function.apply(create(level)).build(LootContextParamSets.BLOCK), sequenceKey);
  }

  public static LootContext fishing(Level level, BuilderFunction function) {
    return fishing(level, function, null);
  }

  public static LootContext fishing(
      Level level, BuilderFunction function, @Nullable ResourceLocation sequenceKey) {
    return build(function.apply(create(level)).build(LootContextParamSets.FISHING), sequenceKey);
  }

  public static LootContext command(Level level, BuilderFunction function) {
    return command(level, function, null);
  }

  public static LootContext command(
      Level level, BuilderFunction function, @Nullable ResourceLocation sequenceKey) {
    return build(function.apply(create(level)).build(LootContextParamSets.COMMAND), sequenceKey);
  }

  public static LootContext entity(Level level, BuilderFunction function) {
    return entity(level, function, null);
  }

  public static LootContext entity(
      Level level, BuilderFunction function, @Nullable ResourceLocation sequenceKey) {
    return build(function.apply(create(level)).build(LootContextParamSets.ENTITY), sequenceKey);
  }

  public static LootContext generic(Level level, BuilderFunction function) {
    return generic(level, function, null);
  }

  public static LootContext generic(
      Level level, BuilderFunction function, @Nullable ResourceLocation sequenceKey) {
    return build(function.apply(create(level)).build(LootContextParamSets.ALL_PARAMS), sequenceKey);
  }

  public static LootContext build(LootParams set) {
    return build(set, null);
  }

  public static LootContext build(LootParams set, ResourceLocation sequenceKey) {
    return new LootContext.Builder(set).create(Optional.ofNullable(sequenceKey));
  }

  public LootContextBuilder dynamicDrops(
      ResourceLocation identifier, LootParams.DynamicDrop dynamicDrop) {
    this.builder.withDynamicDrop(identifier, dynamicDrop);
    return this;
  }

  public LootContextBuilder luck(float luck) {
    this.builder.withLuck(luck);
    return this;
  }

  public LootContextBuilder origin(BlockPos pos) {
    return this.origin(Vec3.atCenterOf(pos));
  }

  public LootContextBuilder origin(Entity entity) {
    return this.origin(entity.position());
  }

  public LootContextBuilder origin(@Nullable Vec3 pos) {
    this.builder.withOptionalParameter(LootContextParams.ORIGIN, pos);
    return this;
  }

  public LootContextBuilder state(@Nullable BlockState state) {
    this.builder.withOptionalParameter(LootContextParams.BLOCK_STATE, state);
    return this;
  }

  public LootContextBuilder tool(LivingEntity player, InteractionHand hand) {
    return this.tool(player.getItemInHand(hand));
  }

  public LootContextBuilder tool(@Nullable ItemStack stack) {
    this.builder.withOptionalParameter(
        LootContextParams.TOOL, Objects.requireNonNullElse(stack, ItemStack.EMPTY));
    return this;
  }

  public LootContextBuilder thisEntity(@Nullable Entity entity) {
    this.builder.withOptionalParameter(LootContextParams.THIS_ENTITY, entity);
    return this;
  }

  public LootContextBuilder attacker(@Nullable Entity entity) {
    this.builder.withOptionalParameter(LootContextParams.ATTACKING_ENTITY, entity);
    return this;
  }

  public LootContextBuilder directAttacker(@Nullable Entity entity) {
    this.builder.withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, entity);
    return this;
  }

  public LootContextBuilder blockEntity(@Nullable BlockEntity entity) {
    this.builder.withOptionalParameter(LootContextParams.BLOCK_ENTITY, entity);
    return this;
  }

  public LootContextBuilder source(@Nullable DamageSource source) {
    this.builder.withOptionalParameter(LootContextParams.DAMAGE_SOURCE, source);
    return this;
  }

  public LootContextBuilder genericSource() {
    return this.source(builder.getLevel().damageSources().generic());
  }

  public LootContextBuilder sourceOrGeneric(@Nullable DamageSource source) {
    return source != null ? this.source(source) : this.genericSource();
  }

  public LootParams build(ContextKeySet set) {
    return this.builder.create(set);
  }

  public interface BuilderFunction {
    LootContextBuilder apply(LootContextBuilder builder);
  }
}
