package dev.zenfyr.pulsar.impl.mixin.client.particles;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.zenfyr.pulsar.client.particles.VanillaParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

  @ModifyExpressionValue(
      at =
          @At(
              value = "FIELD",
              target =
                  "Lnet/minecraft/client/particle/ParticleEngine;level:Lnet/minecraft/client/multiplayer/ClientLevel;"),
      method = "makeParticle")
  private ClientLevel pulsar$modifyWorld(ClientLevel value) {
    var w = VanillaParticle.LEVEL.get();
    if (w != null) return w;
    return value;
  }
}
