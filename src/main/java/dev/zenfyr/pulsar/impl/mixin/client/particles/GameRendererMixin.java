package dev.zenfyr.pulsar.impl.mixin.client.particles;

import com.llamalad7.mixinextras.sugar.Local;
import dev.zenfyr.pulsar.client.particles.ScreenParticleHelper;
import net.minecraft.client.DeltaTracker;
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
              target = "Lnet/minecraft/client/gui/Gui;renderDeferredSubtitles()V",
              shift = At.Shift.BEFORE))
  private void pulsar$renderScreenParticles(
      DeltaTracker deltaTracker, boolean bl, CallbackInfo ci, @Local GuiGraphics graphics) {
    graphics.nextStratum();
    ScreenParticleHelper.renderParticles(this.minecraft, graphics);
  }
}
