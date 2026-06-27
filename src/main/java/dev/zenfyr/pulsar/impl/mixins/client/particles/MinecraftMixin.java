package dev.zenfyr.pulsar.impl.mixins.client.particles;

import dev.zenfyr.pulsar.impl.client.particles.ScreenParticlesDuck;
import dev.zenfyr.pulsar.impl.client.particles.ScreenParticlesImpl;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin implements ScreenParticlesDuck {

  @Unique private final ScreenParticlesImpl pulsar$screenParticles =
      new ScreenParticlesImpl((Minecraft) (Object) this);

  @Inject(
      method = "tick",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/tutorial/Tutorial;onLookAt(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/phys/HitResult;)V",
              shift = At.Shift.AFTER))
  private void pulsar$tickParticles(CallbackInfo ci) {
    this.pulsar$screenParticles.tick();
  }

  @Inject(
      method = "doWorldLoad",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/client/gui/screens/LevelLoadingScreen;tick()V"))
  private void pulsar$tickScreen(CallbackInfo ci) {
    this.pulsar$screenParticles.tick();
  }

  @Override
  public ScreenParticlesImpl pulsar$getScreenParticles() {
    return this.pulsar$screenParticles;
  }
}
