package dev.zenfyr.pulsar.api.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.ApiStatus;

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

  public void bindToScreen(Screen screen) {
    this.screen = screen;
  }

  @ApiStatus.Internal
  public final void extractParticleRenderState(
      GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
    if (removed || (screen != null && client.gui.screen() != screen)) return;
    extractRenderState(graphics, mouseX, mouseY, delta);
  }

  @ApiStatus.Internal
  public final void tickParticle() {
    this.prevX = x;
    this.prevY = y;

    tick();
    age++;
    this.removed = checkRemoval();
    if (screen != null && client.gui.screen() != screen) removed = true;
  }
}
