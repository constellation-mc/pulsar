package dev.zenfyr.pulsar.impl.client.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;

public class GuiParticleRenderer extends PictureInPictureRenderer<GuiParticleRenderState> {

  private final CameraRenderState cameraRenderState;
  public static final ThreadLocal<Boolean> RENDERING = ThreadLocal.withInitial(() -> false);

  public GuiParticleRenderer() {
    cameraRenderState = new CameraRenderState();
  }

  @Override
  public Class<GuiParticleRenderState> getRenderStateClass() {
    return GuiParticleRenderState.class;
  }

  @Override
  protected void renderToTexture(
      GuiParticleRenderState renderState, PoseStack poseStack, SubmitNodeCollector collector1) {
    if (renderState.count() <= 0) return;
    Minecraft minecraft = Minecraft.getInstance();

    FeatureRenderDispatcher featureRenderDispatcher =
        minecraft.gameRenderer.featureRenderDispatcher();

    var stack = RenderSystem.getModelViewStack();
    stack.pushMatrix();
    stack.translate(0, 0, 500f);
    stack.scale(
        24 * minecraft.getWindow().getGuiScale(), 24 * minecraft.getWindow().getGuiScale(), 1);
    stack.translate(0, minecraft.getWindow().getGuiScaledHeight() / 24f, 0);
    stack.scale(1, -1, 1);

    var collector = new SubmitNodeStorage();
    renderState.state().submit(collector, this.cameraRenderState);

    try {
      RENDERING.set(true);
      featureRenderDispatcher.renderAllFeatures(collector);
    } finally {
      RENDERING.remove();
    }

    renderState.state().reset();
    stack.popMatrix();
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
