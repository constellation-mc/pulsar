package dev.zenfyr.pulsar.client.particles;

import dev.zenfyr.pulsar.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;

/**
 * Example screen particle which renders an item with basic physics simulation.
 */
@Environment(EnvType.CLIENT)
public class ItemStackParticle extends AbstractScreenParticle {

  public final ItemStack stack;

  public ItemStackParticle(double x, double y, double velX, double velY, ItemStack stack) {
    super(x, y, velX, velY);
    this.stack = stack;
  }

  @Override
  public void extractRenderState(
      GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
    Matrix3x2fStack pose = graphics.pose();
    float x = (float) Mth.lerp(delta, this.prevX, this.x);
    float y = (float) Mth.lerp(delta, this.prevY, this.y);
    pose.pushMatrix();
    pose.translate(x, y);
    pose.rotate((float) (Math.atan2(velY, velX) * 0.5));
    graphics.item(stack, -8, -8);
    pose.popMatrix();
  }

  @Override
  protected void tick() {
    x += velX *= 0.99;
    y += velY * 0.99;
    velY += MathUtil.nextDouble(0.4, 0.9);
  }

  @Override
  protected boolean checkRemoval() {
    int w = client.getWindow().getGuiScaledWidth();
    int h = client.getWindow().getGuiScaledWidth();
    return ((x > w + 80) || (x < -80)) || ((y > h + 80) || (y < -80));
  }
}
