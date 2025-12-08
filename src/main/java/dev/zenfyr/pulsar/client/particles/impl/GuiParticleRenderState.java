package dev.zenfyr.pulsar.client.particles.impl;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.state.ParticleGroupRenderState;
import org.jetbrains.annotations.Nullable;

public record GuiParticleRenderState(
    ParticleGroupRenderState state,
    int x0,
    int y0,
    int x1,
    int y1,
    @Nullable ScreenRectangle scissorArea,
    @Nullable ScreenRectangle bounds)
    implements PictureInPictureRenderState {

  public GuiParticleRenderState(
      ParticleGroupRenderState state, int x0, int y0, int x1, int y1, ScreenRectangle scissorArea) {
    this(
        state,
        x0,
        y0,
        x1,
        y1,
        scissorArea,
        PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
  }

  @Override
  public float scale() {
    return 1;
  }
}
