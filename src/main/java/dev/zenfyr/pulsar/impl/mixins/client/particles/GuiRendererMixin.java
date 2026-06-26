package dev.zenfyr.pulsar.impl.mixins.client.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.zenfyr.pulsar.api.client.particles.ScreenParticleHelper;
import dev.zenfyr.pulsar.impl.client.particles.VanillaParticleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRenderer.class)
public abstract class GuiRendererMixin {

  @Shadow
  @Final
  private GuiRenderState renderState;

  @Shadow
  @Final
  private FeatureRenderDispatcher featureRenderDispatcher;

  @Inject(
      method = "render",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/client/gui/render/GuiRenderer;draw()V",
              shift = At.Shift.AFTER))
  private void pulsar$blitFallback(CallbackInfo ci) {
    var minecraft = Minecraft.getInstance();
    var state = ScreenParticleHelper.extractState(minecraft);
    if (state.count() <= 0) return;

    var oldSlice = RenderSystem.getProjectionMatrixBuffer();
    var oldType = RenderSystem.getProjectionType();

    this.renderState.nextStratum();
    VanillaParticleManager.particleRenderer.prepare(
        state,
        this.renderState,
        this.featureRenderDispatcher,
        minecraft.getWindow().getGuiScale());

    RenderSystem.setProjectionMatrix(oldSlice, oldType);
  }
}
