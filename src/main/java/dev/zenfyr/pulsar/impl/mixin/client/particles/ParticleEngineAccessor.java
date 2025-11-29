package dev.zenfyr.pulsar.impl.mixin.client.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ParticleEngine.class)
public interface ParticleEngineAccessor {

  @Invoker("makeParticle")
  <T extends ParticleOptions> Particle pulsar$createParticle(
      T parameters,
      double x,
      double y,
      double z,
      double velocityX,
      double velocityY,
      double velocityZ);
}
