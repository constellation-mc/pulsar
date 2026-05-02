package dev.zenfyr.pulsar.client.particles.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.zenfyr.pulsar.client.fakelevel.BrightLightTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.state.CameraRenderState;

public class GuiParticleRenderer extends PictureInPictureRenderer<GuiParticleRenderState> {

  private final CameraRenderState cameraRenderState;
  public static final ThreadLocal<Boolean> RENDERING = ThreadLocal.withInitial(() -> false);

  public GuiParticleRenderer(MultiBufferSource.BufferSource bufferSource) {
    super(bufferSource);
    cameraRenderState = new CameraRenderState();
  }

  @Override
  public Class<GuiParticleRenderState> getRenderStateClass() {
    return GuiParticleRenderState.class;
  }

  @Override
  protected void renderToTexture(GuiParticleRenderState renderState, PoseStack poseStack) {
    Minecraft minecraft = Minecraft.getInstance();

    BrightLightTexture.INSTANCE.turnOnLightLayer();

    FeatureRenderDispatcher featureRenderDispatcher =
        minecraft.gameRenderer.getFeatureRenderDispatcher();

    var stack = RenderSystem.getModelViewStack();
    stack.pushMatrix();
    stack.translate(0, 0, 500f);
    stack.scale(
        24 * minecraft.getWindow().getGuiScale(), 24 * minecraft.getWindow().getGuiScale(), 1);
    stack.translate(0, minecraft.getWindow().getGuiScaledHeight() / 24f, 0);
    stack.scale(1, -1, 1);

    var collector = featureRenderDispatcher.getSubmitNodeStorage();
    renderState.state().submit(collector, this.cameraRenderState);

    try {
      RENDERING.set(true);
      featureRenderDispatcher.renderAllFeatures();
    } finally {
      RENDERING.remove();
    }

    this.bufferSource.endBatch();
    renderState.state().reset();
    stack.popMatrix();

    BrightLightTexture.INSTANCE.turnOffLightLayer();
  }

  @Override
  protected void blitTexture(GuiParticleRenderState renderState, GuiRenderState guiRenderState) {
    // we don't need to blit the texture, as particles are rendered in some special way idk
  }

  @Override
  protected String getTextureLabel() {
    return "pulsar-particles";
  }
}
