package dev.zenfyr.pulsar.impl.client.particles;

import dev.zenfyr.pulsar.api.client.particles.AbstractScreenParticle;
import dev.zenfyr.pulsar.api.client.particles.ScreenParticleHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.particle.Particle;
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

  final Particle particle;

  public VanillaParticle(
      ParticleOptions options, double x, double y, double velX, double velY, double velZ) {
    this(VanillaParticleManager.createScreenParticle(options, x, y, velX, velY, velZ));
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
    // NOOP, rendered by the manager
  }

  @Override
  protected boolean checkRemoval() {
    return !particle.isAlive() || (screen != null && client.screen != screen);
  }
}
