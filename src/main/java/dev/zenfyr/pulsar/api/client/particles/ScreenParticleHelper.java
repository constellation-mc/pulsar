package dev.zenfyr.pulsar.api.client.particles;

import dev.zenfyr.pulsar.api.util.MakeSure;
import dev.zenfyr.pulsar.impl.client.particles.VanillaParticle;
import dev.zenfyr.pulsar.impl.client.particles.VanillaParticleManager;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import org.jetbrains.annotations.ApiStatus;

/**
 * <p>provides tools for creating screen particles. {@link #addScreenParticle} methods, provide a way
 * to create "screen bound" particles, meaning that they stop rendering when the screen they appeared on
 * is closed.</p>
 */
@UtilityClass
@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class ScreenParticleHelper {

  private static final Set<AbstractScreenParticle> SCREEN_PARTICLES = new LinkedHashSet<>();
  private static final Set<AbstractScreenParticle> SCREEN_PARTICLES_REMOVAL = new HashSet<>();
  private static final VanillaParticleManager PARTICLE_MANAGER =
      new VanillaParticleManager(SCREEN_PARTICLES);

  public static void addParticle(AbstractScreenParticle particle) {
    ScreenParticleHelper.addScreenParticle(null, particle);
  }

  public static void addParticle(
      ParticleOptions parameters, double x, double y, double velX, double velY) {
    ScreenParticleHelper.addScreenParticle(null, parameters, x, y, velX, velY);
  }

  public static void addParticle(
      ParticleOptions parameters, double x, double y, double velX, double velY, double velZ) {
    ScreenParticleHelper.addScreenParticle(null, parameters, x, y, velX, velY, velZ);
  }

  public static void addParticles(AbstractScreenParticle... particle) {
    ScreenParticleHelper.addScreenParticles(null, particle);
  }

  public static void addParticles(List<AbstractScreenParticle> particle) {
    ScreenParticleHelper.addScreenParticles(null, particle);
  }

  public static void addParticles(Supplier<AbstractScreenParticle> particle, int count) {
    ScreenParticleHelper.addScreenParticles(null, particle, count);
  }

  public static void addParticles(
      ParticleOptions options,
      double x,
      double y,
      double deltaX,
      double deltaY,
      double speed,
      int count) {
    ScreenParticleHelper.addScreenParticles(null, options, x, y, deltaX, deltaY, speed, count);
  }

  /////////////////////////////

  public static void addScreenParticle(AbstractScreenParticle particle) {
    ScreenParticleHelper.addScreenParticle(current(), particle);
  }

  public static void addScreenParticle(
      ParticleOptions options, double x, double y, double velX, double velY) {
    ScreenParticleHelper.addScreenParticle(current(), options, x, y, velX, velY);
  }

  public static void addScreenParticle(
      ParticleOptions options, double x, double y, double velX, double velY, double velZ) {
    ScreenParticleHelper.addScreenParticle(current(), options, x, y, velX, velY, velZ);
  }

  public static void addScreenParticles(AbstractScreenParticle... particles) {
    ScreenParticleHelper.addScreenParticles(current(), particles);
  }

  public static void addScreenParticles(List<AbstractScreenParticle> particles) {
    ScreenParticleHelper.addScreenParticles(current(), particles);
  }

  public static void addScreenParticles(Supplier<AbstractScreenParticle> particle, int count) {
    ScreenParticleHelper.addScreenParticles(current(), particle, count);
  }

  public static void addScreenParticles(
      ParticleOptions options,
      double x,
      double y,
      double deltaX,
      double deltaY,
      double speed,
      int count) {
    ScreenParticleHelper.addScreenParticles(current(), options, x, y, deltaX, deltaY, speed, count);
  }

  /////////////////////////////

  public static void addScreenParticle(Screen screen, AbstractScreenParticle particle) {
    particle.bindToScreen(screen);
    SCREEN_PARTICLES.add(particle);
  }

  public static void addScreenParticle(
      Screen screen, ParticleOptions options, double x, double y, double velX, double velY) {
    VanillaParticle particle = new VanillaParticle(options, x, y, velX, velY);
    particle.bindToScreen(screen);
    SCREEN_PARTICLES.add(particle);
  }

  public static void addScreenParticle(
      Screen screen,
      ParticleOptions options,
      double x,
      double y,
      double velX,
      double velY,
      double velZ) {
    VanillaParticle particle = new VanillaParticle(options, x, y, velX, velY, velZ);
    particle.bindToScreen(screen);
    SCREEN_PARTICLES.add(particle);
  }

  public static void addScreenParticles(Screen screen, AbstractScreenParticle... particles) {
    for (AbstractScreenParticle particle : particles) {
      particle.bindToScreen(screen);
    }
    SCREEN_PARTICLES.addAll(List.of(particles));
  }

  public static void addScreenParticles(Screen screen, List<AbstractScreenParticle> particles) {
    for (AbstractScreenParticle abstractScreenParticle : particles) {
      abstractScreenParticle.bindToScreen(screen);
    }
    SCREEN_PARTICLES.addAll(particles);
  }

  public static void addScreenParticles(
      Screen screen, Supplier<AbstractScreenParticle> particle, int count) {
    for (int i = 0; i < count; i++) {
      AbstractScreenParticle particle1 = particle.get();
      particle1.bindToScreen(screen);
      SCREEN_PARTICLES.add(particle1);
    }
  }

  public static void addScreenParticles(
      Screen screen,
      ParticleOptions options,
      double x,
      double y,
      double deltaX,
      double deltaY,
      double speed,
      int count) {
    MakeSure.isTrue(count >= 0, "Count can't be below 0!");

    for (int i = 0; i < count; i++) {
      double offsetX = random().nextGaussian() * deltaX;
      double offsetY = random().nextGaussian() * deltaY;
      double velX = random().nextGaussian() * speed;
      double velY = random().nextGaussian() * speed;

      VanillaParticle particle = new VanillaParticle(options, x + offsetX, y + offsetY, velX, velY);
      particle.bindToScreen(screen);
      SCREEN_PARTICLES.add(particle);
    }
  }

  /////////////////////////////

  public static AbstractScreenParticle ofVanilla(Particle particle) {
    return new VanillaParticle(particle);
  }

  public static Supplier<AbstractScreenParticle> ofVanilla(Supplier<Particle> supplier) {
    return () -> new VanillaParticle(supplier.get());
  }

  private static Screen current() {
    return Minecraft.getInstance().screen;
  }

  private static ThreadLocalRandom random() {
    return ThreadLocalRandom.current();
  }

  @ApiStatus.Internal
  public static void renderParticles(Minecraft client, GuiGraphics context) {
    if (SCREEN_PARTICLES.isEmpty()) return;

    int i = (int) (client.mouseHandler.xpos()
        * (double) client.getWindow().getGuiScaledWidth()
        / (double) client.getWindow().getScreenWidth());
    int j = (int) (client.mouseHandler.ypos()
        * (double) client.getWindow().getGuiScaledHeight()
        / (double) client.getWindow().getScreenHeight());

    PARTICLE_MANAGER.render(context);

    for (AbstractScreenParticle particle : SCREEN_PARTICLES) {
      particle.renderInternal(context, i, j, client.getFrameTime());
    }
  }

  @ApiStatus.Internal
  public static void tickParticles() {
    if (SCREEN_PARTICLES.isEmpty()) return;

    for (AbstractScreenParticle particle : SCREEN_PARTICLES) {
      particle.tickInternal();
      if (particle.removed) SCREEN_PARTICLES_REMOVAL.add(particle);
    }

    SCREEN_PARTICLES.removeIf(SCREEN_PARTICLES_REMOVAL::contains);
    SCREEN_PARTICLES_REMOVAL.clear();
  }
}
