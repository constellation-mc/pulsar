package dev.zenfyr.pulsar.impl.mixin.client.fakeworld;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.zenfyr.pulsar.client.fakeworld.FakeLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Level.class)
public class LevelMixin {

  @WrapOperation(
      at =
          @At(
              value = "NEW",
              target =
                  "(Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/damagesource/DamageSources;"),
      method = "<init>")
  private DamageSources pulsar$ignoreDamageSources(
      RegistryAccess registryManager, Operation<DamageSources> original) {
    if (FakeLevel.isLoading()) {
      return null;
    }
    return original.call(registryManager);
  }
}
