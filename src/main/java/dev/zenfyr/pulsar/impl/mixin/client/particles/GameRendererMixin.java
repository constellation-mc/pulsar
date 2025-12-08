package dev.zenfyr.pulsar.impl.mixin.client.particles;

import com.llamalad7.mixinextras.sugar.Local;
import dev.zenfyr.pulsar.client.particles.ScreenParticleHelper;
import dev.zenfyr.pulsar.client.particles.impl.GuiParticleRenderer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
  @Shadow
  @Final
  private Minecraft minecraft;

  @ModifyArg(
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/gui/render/GuiRenderer;<init>(Lnet/minecraft/client/gui/render/state/GuiRenderState;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher;Ljava/util/List;)V"),
      index = 4,
      method = "<init>")
  private List<PictureInPictureRenderer<?>> injectRenderer(
      List<PictureInPictureRenderer<?>> list, @Local MultiBufferSource.BufferSource bufferSource) {
    List<PictureInPictureRenderer<?>> mutable = new ArrayList<>(list);
    mutable.add(new GuiParticleRenderer(bufferSource));
    return mutable;
  }

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
