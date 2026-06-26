package dev.zenfyr.pulsar.impl.mixins.client.particles;

import com.llamalad7.mixinextras.sugar.Local;
import dev.zenfyr.pulsar.api.client.fakelevel.BrightLightTexture;
import dev.zenfyr.pulsar.api.client.particles.ScreenParticleHelper;
import dev.zenfyr.pulsar.impl.client.particles.GuiParticleRenderer;
import dev.zenfyr.pulsar.impl.client.particles.VanillaParticleManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
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

  @Inject(at = @At(value = "TAIL"), method = "<init>")
  private void injectRenderer(CallbackInfo ci, @Local MultiBufferSource.BufferSource bufferSource) {
    VanillaParticleManager.particleRenderer = new GuiParticleRenderer(bufferSource);
  }

  @Inject(
      method = "extractGui",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/client/gui/Gui;extractDeferredSubtitles()V",
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

  @Inject(at = @At("TAIL"), method = "close")
  private void pulsar$closeBrightLightmap(CallbackInfo ci) {
    BrightLightTexture.INSTANCE.close();
  }
}
