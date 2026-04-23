package dev.zenfyr.pulsar.impl.mixin.client.particles;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.zenfyr.pulsar.client.particles.ScreenParticleHelper;
import dev.zenfyr.pulsar.client.particles.impl.VanillaParticleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.state.GuiRenderState;
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

  @Inject(
      method = "render",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/gui/render/GuiRenderer;draw(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V",
              shift = At.Shift.AFTER))
  private void pulsar$blitFallback(GpuBufferSlice fogUniforms, CallbackInfo ci) {
    var minecraft = Minecraft.getInstance();
    var state = ScreenParticleHelper.extractState(minecraft);

    var oldSlice = RenderSystem.getProjectionMatrixBuffer();
    var oldType = RenderSystem.getProjectionType();

    this.renderState.nextStratum();
    VanillaParticleManager.particleRenderer.prepare(
        state, this.renderState, minecraft.getWindow().getGuiScale());

    RenderSystem.setProjectionMatrix(oldSlice, oldType);
  }
}
