package dev.zenfyr.pulsar.client.particles.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.zenfyr.pulsar.client.fakelevel.BrightLightTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.state.CameraRenderState;

public class GuiParticleRenderer extends PictureInPictureRenderer<GuiParticleRenderState> {

  public GuiParticleRenderer(MultiBufferSource.BufferSource bufferSource) {
    super(bufferSource);
  }

  @Override
  public Class<GuiParticleRenderState> getRenderStateClass() {
    return GuiParticleRenderState.class;
  }

  @Override
  protected void renderToTexture(GuiParticleRenderState renderState, PoseStack poseStack) {
    BrightLightTexture.INSTANCE.turnOnLightLayer();
    // Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
    // Vector3f vector3f = renderState.translation();
    // poseStack.translate(vector3f.x, vector3f.y, vector3f.z);
    // poseStack.mulPose(renderState.rotation());
    // Quaternionf quaternionf = renderState.overrideCameraAngle();
    FeatureRenderDispatcher featureRenderDispatcher =
        Minecraft.getInstance().gameRenderer.getFeatureRenderDispatcher();
    CameraRenderState cameraRenderState = new CameraRenderState();
    // if (quaternionf != null) {
    //  cameraRenderState.orientation =
    //      quaternionf.conjugate(new Quaternionf()).rotateY((float) Math.PI);
    // }

    renderState.state().submit(featureRenderDispatcher.getSubmitNodeStorage(), cameraRenderState);
    featureRenderDispatcher.renderAllFeatures();
    BrightLightTexture.INSTANCE.turnOffLightLayer();
  }

  @Override
  protected String getTextureLabel() {
    return "particle";
  }
}
