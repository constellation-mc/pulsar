package dev.zenfyr.pulsar.api.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.ApiStatus;

/**
 * Base screen particle class.
 */
@Environment(EnvType.CLIENT)
public abstract class AbstractScreenParticle {
  public double x, y, velX, velY;
  public double prevX, prevY;
  public int age = 0, deathAge = 200;
  protected Minecraft client;
  public boolean removed = false;
  protected Screen screen;

  public AbstractScreenParticle(double x, double y, double velX, double velY) {
    this.x = x;
    this.y = y;
    this.velX = velX;
    this.velY = velY;
    this.prevX = x - velX;
    this.prevY = y - velY;
    this.client = Minecraft.getInstance();
  }

  @ApiStatus.OverrideOnly
  public abstract void extractRenderState(
      GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta);

  @ApiStatus.OverrideOnly
  protected abstract void tick();

  @ApiStatus.OverrideOnly
  protected boolean checkRemoval() {
    return age >= deathAge;
  }

  /**
   * 'Binds' a particle to a specific screen, if the screen is closed,
   * the particle will be removed.
   * @param screen the screen to bind to.
   */
  public void bindToScreen(Screen screen) {
    this.screen = screen;
  }

  /**
   * Internal method. Do Not Call
   */
  @ApiStatus.Internal
  public final void extractParticleRenderState(
      GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
    if (removed || (screen != null && client.screen != screen)) return;
    extractRenderState(graphics, mouseX, mouseY, delta);
  }

  /**
   * Internal method. Do Not Call
   */
  @ApiStatus.Internal
  public final void tickParticle() {
    this.prevX = x;
    this.prevY = y;

    tick();
    age++;
    this.removed = checkRemoval();
    if (screen != null && client.screen != screen) removed = true;
  }
}
