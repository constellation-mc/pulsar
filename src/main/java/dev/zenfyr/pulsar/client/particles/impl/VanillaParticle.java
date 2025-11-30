package dev.zenfyr.pulsar.client.particles.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import dev.zenfyr.pulsar.client.fakeworld.BrightLightTexture;
import dev.zenfyr.pulsar.client.fakeworld.FakeLevel;
import dev.zenfyr.pulsar.client.particles.AbstractScreenParticle;
import dev.zenfyr.pulsar.client.particles.ScreenParticleHelper;
import dev.zenfyr.pulsar.impl.mixin.client.particles.ParticleEngineAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.particles.ParticleOptions;
import org.jetbrains.annotations.ApiStatus;

/**
 * Render vanilla particle types on screen! Please use the {@link ScreenParticleHelper} methods instead of this class!
 * <p>
 * Inspired by the removed {@code gesundheit} module of <a href="https://git.sleeping.town/unascribed-mods/Lib39">Lib39</a>
 */
@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class VanillaParticle extends AbstractScreenParticle {

  public static final ThreadLocal<ClientLevel> LEVEL = ThreadLocal.withInitial(() -> null);
  private static final Camera CAMERA = new Camera();
  private final Particle particle;

  public VanillaParticle(
      ParticleOptions options, double x, double y, double velX, double velY, double velZ) {
    super(0, 0, 0, 0);

    this.particle = createScreenParticle(options, x, y, velX, velY, velZ);
    if (this.particle != null) {
      this.particle.hasPhysics = false;
    } else {
      this.removed = true;
    }
  }

  public VanillaParticle(ParticleOptions options, double x, double y, double velX, double velY) {
    this(options, x, y, velX, velY, 0);
  }

  public VanillaParticle(Particle particle) {
    super(0, 0, 0, 0);

    this.particle = particle;
    if (this.particle != null) {
      this.particle.hasPhysics = false;
    } else {
      this.removed = true;
    }
  }

  @Override
  protected void tick() {
    particle.tick();
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
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

    BrightLightTexture.INSTANCE.turnOnLightLayer();
    Tesselator tessellator = Tesselator.getInstance();
    BufferBuilder bufferBuilder = tessellator.getBuilder();

    RenderSystem.setShader(GameRenderer::getParticleShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    particle.getRenderType().begin(bufferBuilder, client.getTextureManager());

    try {
      particle.render(bufferBuilder, CAMERA, client.getFrameTime());
    } catch (Throwable var17) {
      CrashReport crashReport =
          CrashReport.forThrowable(var17, "[Pulsar] Rendering Particle On Screen");
      CrashReportCategory crashReportSection =
          crashReport.addCategory("Particle being rendered on screen");
      crashReportSection.setDetail("Particle", particle::toString);
      crashReportSection.setDetail("Particle Type", particle.getRenderType()::toString);
      throw new ReportedException(crashReport);
    }

    particle.getRenderType().end(tessellator);

    BrightLightTexture.INSTANCE.turnOffLightLayer();
    poseStack.popPose();
    RenderSystem.applyModelViewMatrix();
    pose.popPose();

    RenderSystem.depthMask(true);
    RenderSystem.enableCull();
    RenderSystem.disableBlend();
  }

  @Override
  protected boolean checkRemoval() {
    return !particle.isAlive();
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
