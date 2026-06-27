package dev.zenfyr.pulsar.impl.client.particles;

import dev.zenfyr.pulsar.api.client.particles.AbstractScreenParticle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.particle.Particle;

/**
 * Inspired by the removed {@code gesundheit} module of <a href="https://git.sleeping.town/unascribed-mods/Lib39">Lib39</a>
 */
public class VanillaParticle extends AbstractScreenParticle {

  final Particle particle;

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
    // NOOP, rendered by the manager lol
  }

  @Override
  protected boolean checkRemoval() {
    return !particle.isAlive() || (screen != null && client.screen != screen);
  }
}
