package dev.zenfyr.pulsar.api.client.particles;

import dev.zenfyr.pulsar.impl.client.particles.ScreenParticlesDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>provides tools for creating screen particles. {@link #addParticle} methods accept a
 * {@code screen} parameter, which binds that particle to that screen,
 * meaning that they stop rendering when the screen they appeared on is closed.</p>
 */
@ApiStatus.NonExtendable
public interface ScreenParticles {

  static ScreenParticles get(Minecraft client) {
    return ((ScreenParticlesDuck) client).pulsar$getScreenParticles();
  }

  default void addParticle(AbstractScreenParticle particle) {
    this.addParticle(null, particle);
  }

  void addParticle(@Nullable Screen screen, @NotNull AbstractScreenParticle particle);

  default void addParticles(AbstractScreenParticle... particles) {
    this.addParticles(null, particles);
  }

  default void addParticles(Screen screen, AbstractScreenParticle... particles) {
    for (AbstractScreenParticle particle : particles) {
      this.addParticle(screen, particle);
    }
  }
}
