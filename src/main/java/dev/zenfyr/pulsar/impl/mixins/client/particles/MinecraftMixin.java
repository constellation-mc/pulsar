package dev.zenfyr.pulsar.impl.mixins.client.particles;

import dev.zenfyr.pulsar.api.client.particles.ScreenParticleHelper;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

  @Inject(
      method = "tick",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/tutorial/Tutorial;onLookAt(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/phys/HitResult;)V",
              shift = At.Shift.AFTER))
  private void pulsar$tickParticles(CallbackInfo ci) {
    ScreenParticleHelper.tickParticles();
  }

  @Inject(
      method = "doWorldLoad",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/client/gui/screens/LevelLoadingScreen;tick()V"))
  private void pulsar$tickScreen(CallbackInfo ci) {
    ScreenParticleHelper.tickParticles();
  }
}
