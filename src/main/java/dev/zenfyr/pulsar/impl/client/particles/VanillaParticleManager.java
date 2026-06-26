package dev.zenfyr.pulsar.impl.client.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import dev.zenfyr.pulsar.api.client.fakelevel.BrightLightTexture;
import dev.zenfyr.pulsar.api.client.fakelevel.FakeLevel;
import dev.zenfyr.pulsar.api.client.particles.AbstractScreenParticle;
import dev.zenfyr.pulsar.impl.mixins.client.particles.ParticleEngineAccessor;
import java.util.*;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.particles.ParticleOptions;

/**
 * state manager for {@link VanillaParticle}, used to batch rendering operations.
 */
public class VanillaParticleManager {

  public static final ThreadLocal<ClientLevel> LEVEL = ThreadLocal.withInitial(() -> null);
  private static final Camera CAMERA = new Camera();
  private final Set<AbstractScreenParticle> screenParticles;

  public VanillaParticleManager(Set<AbstractScreenParticle> screenParticles) {
    this.screenParticles = screenParticles;
  }

  // this method is an almost direct copy of the one in the particle engine
  public void render(GuiGraphics graphics) {
    Minecraft client = Minecraft.getInstance();

    Map<ParticleRenderType, List<VanillaParticle>> particles = new IdentityHashMap<>();
    this.screenParticles.forEach(particle1 -> {
      if (particle1 instanceof VanillaParticle vp && vp.particle != null && !vp.checkRemoval()) {
        particles
            .computeIfAbsent(vp.particle.getRenderType(), t -> new ArrayList<>())
            .add(vp);
      }
    });

    PoseStack pose = graphics.pose();
    RenderSystem.disableCull();
    RenderSystem.enableDepthTest();

    pose.pushPose();
    PoseStack poseStack = RenderSystem.getModelViewStack();
    poseStack.pushPose();
    poseStack.translate(0, 0, 500);
    poseStack.scale(24, 24, 1);
    poseStack.translate(0, client.getWindow().getGuiScaledHeight() / 24f, 0);
    poseStack.scale(1, -1, 1);
    poseStack.mulPoseMatrix(pose.last().pose());
    RenderSystem.applyModelViewMatrix();

    // without this, the particles will use the world's light texture,
    // which in turn makes them appear dark at night.
    BrightLightTexture.INSTANCE.turnOnLightLayer();
    Tesselator tessellator = Tesselator.getInstance();
    BufferBuilder bufferBuilder = tessellator.getBuilder();

    RenderSystem.setShader(GameRenderer::getParticleShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    particles.forEach((type, particles1) -> {
      type.begin(bufferBuilder, client.getTextureManager());

      for (VanillaParticle vp : particles1) {
        try {
          vp.particle.render(bufferBuilder, CAMERA, client.getFrameTime());
        } catch (Throwable var17) {
          CrashReport crashReport =
              CrashReport.forThrowable(var17, "[Pulsar] Rendering Particle On Screen");
          CrashReportCategory crashReportSection =
              crashReport.addCategory("Particle being rendered on screen");
          crashReportSection.setDetail("Particle", vp.particle::toString);
          crashReportSection.setDetail("Particle Type", vp.particle.getRenderType()::toString);
          throw new ReportedException(crashReport);
        }
      }

      type.end(tessellator);
    });

    BrightLightTexture.INSTANCE.turnOffLightLayer();
    poseStack.popPose();
    RenderSystem.applyModelViewMatrix();
    pose.popPose();

    RenderSystem.depthMask(true);
    RenderSystem.enableCull();
    RenderSystem.disableBlend();
  }

  public static <T extends ParticleOptions> Particle createScreenParticle(
      T options, double x, double y, double velocityX, double velocityY, double velocityZ) {
    Particle particle;
    try {
      LEVEL.set(FakeLevel.INSTANCE.get());
      particle = ((ParticleEngineAccessor) Minecraft.getInstance().particleEngine)
          .pulsar$createParticle(
              options,
              x / 24,
              (Minecraft.getInstance().getWindow().getGuiScaledHeight() - y) / 24,
              0,
              velocityX,
              velocityY,
              velocityZ);
    } finally {
      LEVEL.remove();
    }
    return particle;
  }
}
