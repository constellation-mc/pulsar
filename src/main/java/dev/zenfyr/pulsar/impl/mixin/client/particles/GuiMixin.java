package dev.zenfyr.pulsar.impl.mixin.client.particles;

import com.llamalad7.mixinextras.sugar.Local;
import dev.zenfyr.pulsar.client.particles.ScreenParticleHelper;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

  @Shadow
  @Final
  private Minecraft minecraft;

  @Inject(
      method = "extractRenderState",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/client/gui/Hud;extractDeferredSubtitles()V",
              shift = At.Shift.BEFORE))
  private void pulsar$renderScreenParticles(
      DeltaTracker deltaTracker,
      boolean shouldRenderLevel,
      boolean resourcesLoaded,
      CallbackInfo ci,
      @Local GuiGraphicsExtractor graphics) {
    // graphics.nextStratum();
    ScreenParticleHelper.extractParticleRenderState(this.minecraft, graphics);
  }
}
