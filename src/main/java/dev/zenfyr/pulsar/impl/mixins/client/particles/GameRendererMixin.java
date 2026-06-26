package dev.zenfyr.pulsar.impl.mixins.client.particles;

import com.llamalad7.mixinextras.sugar.Local;
import dev.zenfyr.pulsar.api.client.particles.ScreenParticleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
  @Shadow
  @Final
  private Minecraft minecraft;

  @Inject(
      method = "render",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V",
              ordinal = 1,
              shift = At.Shift.BEFORE))
  private void pulsar$renderScreenParticles(
      float tickDelta, long startTime, boolean tick, CallbackInfo ci, @Local GuiGraphics graphics) {
    this.minecraft.getProfiler().push("pulsar_particles");
    ScreenParticleHelper.renderParticles(this.minecraft, graphics);
    this.minecraft.getProfiler().pop();
  }
}
