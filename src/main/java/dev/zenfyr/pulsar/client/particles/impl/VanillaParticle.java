package dev.zenfyr.pulsar.client.particles.impl;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.zenfyr.pulsar.client.fakelevel.FakeLevel;
import dev.zenfyr.pulsar.client.particles.AbstractScreenParticle;
import dev.zenfyr.pulsar.client.particles.ScreenParticleHelper;
import dev.zenfyr.pulsar.impl.mixin.client.particles.ParticleEngineAccessor;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.feature.ParticleFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.particles.ParticleOptions;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;

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
  private static final CameraRenderState CAMERA_STATE = new CameraRenderState();
  private static final Frustum FRUSTUM = new Frustum(new Matrix4f(), new Matrix4f()) {
    @Override
    public boolean pointInFrustum(double d, double e, double f) {
      return true;
    }
  };

  private final Particle particle;
  private final ParticleGroup<?> group;

  public VanillaParticle(
      ParticleOptions options, double x, double y, double velX, double velY, double velZ) {
    this(createScreenParticle(options, x, y, velX, velY, velZ));
  }

  public VanillaParticle(ParticleOptions options, double x, double y, double velX, double velY) {
    this(options, x, y, velX, velY, 0);
  }

  public VanillaParticle(Particle particle) {
    super(0, 0, 0, 0);

    this.particle = particle;
    if (this.particle != null) {
      this.particle.hasPhysics = false;
      this.group = ((ParticleEngineAccessor) client.particleEngine)
          .pulsar$createParticleGroup(this.particle.getGroup());
      this.group.add(this.particle);
    } else {
      this.removed = true;
      this.group = null;
    }
  }

  @Override
  protected void tick() {
    particle.tick();
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
    if (this.group == null) return;

    try {
      var poseStack = graphics.pose();
      poseStack.pushMatrix();
      poseStack.translate(0, 0);
      poseStack.scale(24, 24);
      poseStack.translate(0, client.getWindow().getGuiScaledHeight() / 24f);
      poseStack.scale(1, -1);

      var collector = new SubmitNodeStorage();
      var bufferCache = new ParticleFeatureRenderer.ParticleBufferCache();

      var state = this.group.extractRenderState(FRUSTUM, CAMERA, delta);
      state.submit(collector, CAMERA_STATE);
      var renderer = collector.order(0).getParticleGroupRenderers().get(0);
      var prepared = renderer.prepare(bufferCache);

      RenderTarget renderTarget = client.getMainRenderTarget();
      try (var pass = RenderSystem.getDevice()
          .createCommandEncoder()
          .createRenderPass(
              () -> "Particles - GUI (Pulsar)",
              renderTarget.getColorTextureView(),
              OptionalInt.empty(),
              renderTarget.getDepthTextureView(),
              OptionalDouble.of(1.0))) {
        renderer.render(prepared, bufferCache, pass, client.getTextureManager(), false);
        renderer.render(prepared, bufferCache, pass, client.getTextureManager(), true);
      }
      bufferCache.close();
      state.clear();

      poseStack.popMatrix();
    } catch (Throwable var17) {
      CrashReport crashReport =
          CrashReport.forThrowable(var17, "[Pulsar] Rendering Particle On Screen");
      CrashReportCategory crashReportSection =
          crashReport.addCategory("Particle being rendered on screen");
      crashReportSection.setDetail("Particle", particle::toString);
      crashReportSection.setDetail("Particle Type", this.particle.getGroup()::toString);
      throw new ReportedException(crashReport);
    }
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
