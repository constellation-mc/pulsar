package dev.zenfyr.pulsar.impl.client.particles;

import com.google.common.collect.EvictingQueue;
import dev.zenfyr.pulsar.api.client.particles.AbstractScreenParticle;
import dev.zenfyr.pulsar.api.client.particles.ScreenParticles;
import java.util.Queue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

public class ScreenParticlesImpl implements ScreenParticles {

  private final Queue<AbstractScreenParticle> particles;
  private final VanillaParticleManager vanillaManager;
  private final Minecraft client;

  public ScreenParticlesImpl(Minecraft client) {
    this.client = client;
    this.particles = EvictingQueue.create(16384);
    this.vanillaManager = new VanillaParticleManager(client, this.particles);
  }

  @Override
  public void addParticle(Screen screen, @NotNull AbstractScreenParticle particle) {
    if (screen != null) particle.bindToScreen(screen);
    this.particles.add(particle);
  }

  public void render(GuiGraphics graphics) {
    if (this.particles.isEmpty()) return;

    int i = (int) (this.client.mouseHandler.xpos()
        * (double) this.client.getWindow().getGuiScaledWidth()
        / (double) this.client.getWindow().getScreenWidth());
    int j = (int) (this.client.mouseHandler.ypos()
        * (double) this.client.getWindow().getGuiScaledHeight()
        / (double) this.client.getWindow().getScreenHeight());

    this.vanillaManager.render(graphics);
    for (AbstractScreenParticle particle : this.particles) {
      particle.renderParticle(graphics, i, j, client.getFrameTime());
    }
  }

  public void tick() {
    if (this.particles.isEmpty()) return;

    var itr = this.particles.iterator();
    while (itr.hasNext()) {
      AbstractScreenParticle particle = itr.next();
      particle.tickParticle();
      if (particle.removed) itr.remove();
    }
  }
}
