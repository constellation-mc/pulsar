package dev.zenfyr.pulsar.impl.client.particles;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Maps;
import dev.zenfyr.pulsar.api.client.fakelevel.FakeLevel;
import dev.zenfyr.pulsar.impl.mixins.client.particles.ParticleEngineAccessor;
import java.util.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.ParticlesRenderState;
import net.minecraft.core.particles.ParticleOptions;
import org.joml.Matrix4f;

// 16384 is max particles from evicting queue
public class VanillaParticleManager {

  public static final ThreadLocal<ClientLevel> LEVEL = ThreadLocal.withInitial(() -> null);
  public static GuiParticleRenderer particleRenderer;

  private static final Camera CAMERA = new Camera();
  private static final Frustum FRUSTUM = new Frustum(new Matrix4f(), new Matrix4f()) {
    @Override
    public boolean pointInFrustum(double d, double e, double f) {
      return true;
    }
  };

  private final Map<ParticleRenderType, GroupWrapper> particleGroups = Maps.newIdentityHashMap();

  public void tick() {
    this.particleGroups.values().forEach(GroupWrapper::tickParticles);
  }

  public void addParticle(VanillaParticle particle) {
    var pe = Minecraft.getInstance().particleEngine;

    this.particleGroups
        .computeIfAbsent(particle.particle.getGroup(), type -> {
          var group = ((ParticleEngineAccessor) pe).pulsar$createParticleGroup(type);
          return new GroupWrapper(group);
        })
        .addParticle(particle);
  }

  public int extract(ParticlesRenderState particlesRenderState, float f) {
    int count = 0;
    for (ParticleRenderType type : ParticleEngineAccessor.pulsar$getRenderOrder()) {
      var wrapper = this.particleGroups.get(type);
      if (wrapper != null) {
        wrapper.checkRemoval();
        if (!wrapper.particles.isEmpty()) {
          count += wrapper.particles.size();
          particlesRenderState.add(wrapper.group.extractRenderState(FRUSTUM, CAMERA, f));
        }
      }
    }
    return count;
  }

  private static class GroupWrapper {
    private final ParticleGroup<?> group;
    private final Map<Particle, VanillaParticle> particles = new IdentityHashMap<>();
    private final EvictingQueue<Particle> evictionMirror;

    private GroupWrapper(ParticleGroup<?> group) {
      this.group = group;
      this.evictionMirror = EvictingQueue.create(16384);
    }

    private void tickParticles() {
      if (this.group.getAll().isEmpty()) return;

      for (Particle particle : this.group.getAll()) {
        var vp = this.particles.get(particle);
        vp.tick();
      }

      this.checkRemoval();
    }

    private void checkRemoval() {
      Iterator<? extends Particle> itr = this.group.getAll().iterator();
      while (itr.hasNext()) {
        var particle = itr.next();
        var vp = this.particles.get(particle);
        if (vp == null || vp.checkRemoval()) {
          itr.remove();
          this.particles.remove(particle);
          evictionMirror.remove(particle);
        }
      }
    }

    private void addParticle(VanillaParticle particle) {
      if (evictionMirror.remainingCapacity() == 0) {
        Particle evicted = evictionMirror.peek();
        if (evicted != null) particles.remove(evicted);
      }

      group.add(particle.particle);
      evictionMirror.add(particle.particle);
      particles.put(particle.particle, particle);
    }
  }

  public static <T extends ParticleOptions> Particle createScreenParticle(
      T options, double x, double y, double velocityX, double velocityY, double velocityZ) {
    Particle particle;
    var window = Minecraft.getInstance().getWindow();
    try {
      LEVEL.set(FakeLevel.INSTANCE.get());
      particle = ((ParticleEngineAccessor) Minecraft.getInstance().particleEngine)
          .pulsar$createParticle(
              options,
              x / 24,
              (window.getGuiScaledHeight() - y) / 24,
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
