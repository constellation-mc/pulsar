package dev.zenfyr.pulsar.client.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.zenfyr.pulsar.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class ItemStackParticle extends AbstractScreenParticle {

  public final ItemStack stack;

  public ItemStackParticle(double x, double y, double velX, double velY, ItemStack stack) {
    super(x, y, velX, velY);
    this.stack = stack;
  }

  @Override
  public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
    PoseStack matrices = context.pose();
    float x = (float) Mth.lerp(delta, this.prevX, this.x);
    float y = (float) Mth.lerp(delta, this.prevY, this.y);
    matrices.pushPose();
    matrices.translate(x, y, 500);
    float angle = (float) Math.toDegrees(Math.atan2(velY, velX) * 0.5);
    matrices.mulPose(Axis.ZP.rotationDegrees(angle));
    context.renderItem(stack, -8, -8);
    matrices.popPose();
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
