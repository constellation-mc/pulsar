package dev.zenfyr.pulsar.impl.client.fakelevel;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.extract.LevelExtractor;
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

public class FakeLevelImpl extends ClientLevel {

  public FakeLevelImpl(
      ClientPacketListener connection,
      ClientLevelData levelData,
      ResourceKey<Level> dimension,
      Holder<DimensionType> dimensionType,
      int serverChunkRadius,
      int serverSimulationDistance,
      LevelExtractor levelExtractor,
      boolean isDebug,
      long biomeZoomSeed,
      int seaLevel) {
    super(
        connection,
        levelData,
        dimension,
        dimensionType,
        serverChunkRadius,
        serverSimulationDistance,
        levelExtractor,
        isDebug,
        biomeZoomSeed,
        seaLevel);
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
