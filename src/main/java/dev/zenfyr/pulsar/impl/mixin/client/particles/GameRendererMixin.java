package dev.zenfyr.pulsar.impl.mixin.client.particles;

import dev.zenfyr.pulsar.client.fakelevel.BrightLightTexture;
import dev.zenfyr.pulsar.client.particles.impl.GuiParticleRenderer;
import dev.zenfyr.pulsar.client.particles.impl.VanillaParticleManager;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

  @Inject(at = @At(value = "TAIL"), method = "<init>")
  private void injectRenderer(CallbackInfo ci) {
    VanillaParticleManager.particleRenderer = new GuiParticleRenderer();
  }

  @Inject(at = @At("TAIL"), method = "close")
  private void pulsar$closeBrightLightmap(CallbackInfo ci) {
    BrightLightTexture.INSTANCE.close();
  }
}
