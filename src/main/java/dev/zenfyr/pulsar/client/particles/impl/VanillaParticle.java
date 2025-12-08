package dev.zenfyr.pulsar.client.particles.impl;

import dev.zenfyr.pulsar.client.fakelevel.FakeLevel;
import dev.zenfyr.pulsar.client.particles.AbstractScreenParticle;
import dev.zenfyr.pulsar.client.particles.ScreenParticleHelper;
import dev.zenfyr.pulsar.impl.mixin.client.particles.ParticleEngineAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.renderer.culling.Frustum;
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

    var w = Minecraft.getInstance().getWindow();
    var state = this.group.extractRenderState(FRUSTUM, CAMERA, delta);

    graphics.guiRenderState.submitPicturesInPictureState(new GuiParticleRenderState(
        state, 0, 0, w.getGuiScaledWidth(), w.getGuiScaledHeight(), graphics.scissorStack.peek()));
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
