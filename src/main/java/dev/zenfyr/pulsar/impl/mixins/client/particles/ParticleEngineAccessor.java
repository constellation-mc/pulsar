package dev.zenfyr.pulsar.impl.mixins.client.particles;

import java.util.List;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ParticleEngine.class)
public interface ParticleEngineAccessor {

  @Accessor("RENDER_ORDER")
  static List<ParticleRenderType> pulsar$getRenderOrder() {
    throw new AssertionError();
  }

  @Invoker("makeParticle")
  <T extends ParticleOptions> Particle pulsar$createParticle(
      T parameters,
      double x,
      double y,
      double z,
      double velocityX,
      double velocityY,
      double velocityZ);

  @Invoker("createParticleGroup")
  ParticleGroup<?> pulsar$createParticleGroup(ParticleRenderType particleRenderType);
}
