package dev.zenfyr.pulsar.client.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import dev.zenfyr.pulsar.client.fakeworld.AlwaysBrightLightmapTextureManager;
import dev.zenfyr.pulsar.client.fakeworld.FakeWorld;
import dev.zenfyr.pulsar.impl.mixin.client.particles.ParticleManagerAccessor;
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

/**
 * Render vanilla particle types on screen!
 * <p>
 * Inspired by the removed {@code gesundheit} module of <a href="https://git.sleeping.town/unascribed-mods/Lib39">Lib39</a>
 */
@Environment(EnvType.CLIENT)
public class VanillaParticle extends AbstractScreenParticle {

  public static final ThreadLocal<ClientLevel> WORLD = ThreadLocal.withInitial(() -> null);
  private static final Camera CAMERA = new Camera();
  private final Particle particle;

  public VanillaParticle(
      ParticleOptions parameters, double x, double y, double velX, double velY, double velZ) {
    super(0, 0, 0, 0);

    this.particle = createScreenParticle(parameters, x, y, velX, velY, velZ);
    if (this.particle != null) {
      this.particle.hasPhysics = false;
    } else {
      this.removed = true;
    }
  }

  public VanillaParticle(ParticleOptions parameters, double x, double y, double velX, double velY) {
    this(parameters, x, y, velX, velY, 0);
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
  public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
    PoseStack matrices = context.pose();
    RenderSystem.disableCull();
    RenderSystem.enableDepthTest();

    matrices.pushPose();
    PoseStack matrixStack = RenderSystem.getModelViewStack();
    matrixStack.pushPose();
    matrixStack.translate(0, 0, 500);
    matrixStack.scale(24, 24, 1);
    matrixStack.translate(0, client.getWindow().getGuiScaledHeight() / 24f, 0);
    matrixStack.scale(1, -1, 1);
    matrixStack.mulPoseMatrix(matrices.last().pose());
    RenderSystem.applyModelViewMatrix();

    AlwaysBrightLightmapTextureManager.INSTANCE.turnOnLightLayer();
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

    AlwaysBrightLightmapTextureManager.INSTANCE.turnOffLightLayer();
    matrixStack.popPose();
    RenderSystem.applyModelViewMatrix();
    matrices.popPose();

    RenderSystem.depthMask(true);
    RenderSystem.enableCull();
    RenderSystem.disableBlend();
  }

  @Override
  protected boolean checkRemoval() {
    return !particle.isAlive();
  }

  public static <T extends ParticleOptions> Particle createScreenParticle(
      T parameters, double x, double y, double velocityX, double velocityY, double velocityZ) {
    Particle particle;
    try {
      WORLD.set(FakeWorld.INSTANCE.get());
      particle = ((ParticleManagerAccessor) Minecraft.getInstance().particleEngine)
          .pulsar$createParticle(
              parameters,
              x / 24,
              (Minecraft.getInstance().getWindow().getGuiScaledHeight() - y) / 24,
              0,
              velocityX,
              velocityY,
              velocityZ);
    } finally {
      WORLD.remove();
    }
    return particle;
  }
}
