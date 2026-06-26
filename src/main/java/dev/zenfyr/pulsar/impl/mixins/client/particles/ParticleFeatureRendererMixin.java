package dev.zenfyr.pulsar.impl.mixins.client.particles;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.pipeline.RenderTarget;
import dev.zenfyr.pulsar.api.client.fakelevel.BrightLightTexture;
import dev.zenfyr.pulsar.impl.client.particles.GuiParticleRenderer;
import net.minecraft.client.renderer.LightTexture;
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
                  "Lnet/minecraft/client/renderer/GameRenderer;lightTexture()Lnet/minecraft/client/renderer/LightTexture;"),
      method = "prepareRenderPass")
  private LightTexture pulsar$setLightTextureForParticles(LightTexture original) {
    return GuiParticleRenderer.RENDERING.get() ? BrightLightTexture.INSTANCE : original;
  }
}
