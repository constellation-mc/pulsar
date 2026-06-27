package dev.zenfyr.pulsar.impl.mixins.client.particles;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.textures.GpuTextureView;
import dev.zenfyr.pulsar.api.client.fakelevel.BrightLightTexture;
import dev.zenfyr.pulsar.impl.client.particles.GuiParticleRenderer;
import net.minecraft.client.renderer.feature.QuadParticleFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(QuadParticleFeatureRenderer.class)
public class ParticleFeatureRendererMixin {

  @ModifyExpressionValue(
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/renderer/LevelRenderer;particlesTarget()Lcom/mojang/blaze3d/pipeline/RenderTarget;"),
      method = "executeGroup")
  private RenderTarget pulsar$removeTargetForParticles(RenderTarget original) {
    return GuiParticleRenderer.RENDERING.get() ? null : original;
  }

  @ModifyExpressionValue(
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/renderer/feature/FeatureFrameContext;lightmap()Lcom/mojang/blaze3d/textures/GpuTextureView;"),
      method = "executeGroup")
  private GpuTextureView pulsar$setLightTextureForParticles(GpuTextureView original) {
    return GuiParticleRenderer.RENDERING.get()
        ? BrightLightTexture.getInstance().getTextureView()
        : original;
  }
}
