package dev.zenfyr.pulsar.api.client.particles;

import dev.zenfyr.pulsar.api.util.MathUtil;
import dev.zenfyr.pulsar.impl.client.particles.VanillaParticle;
import dev.zenfyr.pulsar.impl.client.particles.VanillaParticleManager;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;

/**
 * A utility class for creating screen particles based on vanilla particle types.
 */
public class VanillaParticles {

  public static AbstractScreenParticle create(
      ParticleOptions options, double x, double y, double velX, double velY) {
    return create(VanillaParticleManager.createScreenParticle(options, x, y, velX, velY, 0));
  }

  public static AbstractScreenParticle create(
      ParticleOptions options, double x, double y, double velX, double velY, double velZ) {
    return create(VanillaParticleManager.createScreenParticle(options, x, y, velX, velY, velZ));
  }

  public static AbstractScreenParticle[] create(
      ParticleOptions options,
      double x,
      double y,
      double deltaX,
      double deltaY,
      double speed,
      int count) {
    AbstractScreenParticle[] particles = new AbstractScreenParticle[count];
    for (int i = 0; i < count; i++) {
      double offsetX = MathUtil.random().nextGaussian() * deltaX;
      double offsetY = MathUtil.random().nextGaussian() * deltaY;
      double velX = MathUtil.random().nextGaussian() * speed;
      double velY = MathUtil.random().nextGaussian() * speed;

      particles[i] = create(options, x + offsetX, y + offsetY, velX, velY, 0);
    }
    return particles;
  }

  public static AbstractScreenParticle create(Particle particle) {
    return new VanillaParticle(particle);
  }
}
