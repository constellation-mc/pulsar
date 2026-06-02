package dev.zenfyr.pulsar.impl.mixin.client.particles;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.textures.GpuTextureView;
import dev.zenfyr.pulsar.client.fakelevel.BrightLightTexture;
import dev.zenfyr.pulsar.client.particles.impl.GuiParticleRenderer;
import net.minecraft.client.renderer.feature.ParticleFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ParticleFeatureRenderer.class)
public class ParticleFeatureRendererMixin {

  @ModifyExpressionValue(
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/renderer/LevelRenderer;getParticlesTarget()Lcom/mojang/blaze3d/pipeline/RenderTarget;"),
      method = "render")
  private RenderTarget pulsar$removeTargetForParticles(RenderTarget original) {
    return GuiParticleRenderer.RENDERING.get() ? null : original;
  }

  @ModifyExpressionValue(
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/renderer/LightTexture;getTextureView()Lcom/mojang/blaze3d/textures/GpuTextureView;"),
      method = "prepareRenderPass")
  private GpuTextureView pulsar$setLightTextureForParticles(GpuTextureView original) {
    return GuiParticleRenderer.RENDERING.get()
        ? BrightLightTexture.INSTANCE.getTextureView()
        : original;
  }
}
