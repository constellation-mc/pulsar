package dev.zenfyr.pulsar.impl.client.fakelevel;

import com.google.common.base.Suppliers;
import dev.zenfyr.pulsar.api.client.events.AfterFirstReload;
import java.util.function.Supplier;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import sun.misc.Unsafe;

public class FakeLevelImpl extends ClientLevel {

  public static final Supplier<ClientLevel> INSTANCE = Suppliers.memoize(() -> {
    try {
      var field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      Unsafe unsafe = (Unsafe) field.get(null);

      var level = (FakeLevelImpl) unsafe.allocateInstance(FakeLevelImpl.class);
      level.initHook();
      return level;

    } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
      throw new RuntimeException("Failed to init FakeLevelImpl!", e);
    }
  });

  public FakeLevelImpl(
      ClientPacketListener clientPacketListener,
      ClientLevelData clientLevelData,
      ResourceKey<Level> resourceKey,
      Holder<DimensionType> holder,
      int i,
      int j,
      LevelRenderer levelRenderer,
      boolean bl,
      long l,
      int k) {
    super(
        clientPacketListener, clientLevelData, resourceKey, holder, i, j, levelRenderer, bl, l, k);
  }

  public static void init() {
    AfterFirstReload.EVENT.listen(INSTANCE::get);
  }

  // since we can't call the ctx, we init fields like this.
  public void initHook() {}

  @Override
  public void addParticle(
      ParticleOptions particleData,
      double x,
      double y,
      double z,
      double xSpeed,
      double ySpeed,
      double zSpeed) {}

  @Override
  public void addParticle(
      ParticleOptions particle,
      boolean overrideLimiter,
      boolean alwaysShow,
      double x,
      double y,
      double z,
      double xSpeed,
      double ySpeed,
      double zSpeed) {}

  @Override
  public void addAlwaysVisibleParticle(
      ParticleOptions particleData,
      double x,
      double y,
      double z,
      double xSpeed,
      double ySpeed,
      double zSpeed) {}

  @Override
  public void addAlwaysVisibleParticle(
      ParticleOptions particleData,
      boolean ignoreRange,
      double x,
      double y,
      double z,
      double xSpeed,
      double ySpeed,
      double zSpeed) {}

  @Override
  public BlockState getBlockState(BlockPos blockPos) {
    return Blocks.AIR.defaultBlockState();
  }

  @Override
  public FluidState getFluidState(BlockPos blockPos) {
    return Fluids.EMPTY.defaultFluidState();
  }

  @Override
  public LevelLightEngine getLightEngine() {
    return LevelLightEngine.EMPTY;
  }
}
