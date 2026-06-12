package dev.zenfyr.pulsar.client.fakelevel;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

class FakeLevelImpl extends ClientLevel {

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

  // since we can't call the ctx, we init fields like this.
  public void initHook() {}

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
