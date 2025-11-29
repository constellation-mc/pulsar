package dev.zenfyr.pulsar.client.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.zenfyr.pulsar.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class Particle extends AbstractScreenParticle {
  public final int color;
  public double wind = 0.05;

  public Particle(double x, double y, double velX, double velY, int color) {
    super(x, y, velX, velY);
    this.color = color;
    this.deathAge += MathUtil.threadRandom().nextInt(120);
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
    PoseStack matrices = graphics.pose();
    matrices.pushPose();
    float x = (float) Mth.lerp(delta, prevX, this.x);
    float y = (float) Mth.lerp(delta, prevY, this.y);
    graphics.fillGradient((int) x, (int) y, (int) (x + 3), (int) (y + 3), 500, color, color);
    matrices.popPose();
  }

  @Override
  protected void tick() {
    x += velX;
    y += velY;
    velX += wind * MathUtil.nextDouble(-0.5, 1);
  }
}
