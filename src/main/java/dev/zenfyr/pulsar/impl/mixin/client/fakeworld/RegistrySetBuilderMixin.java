package dev.zenfyr.pulsar.impl.mixin.client.fakeworld;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.zenfyr.pulsar.client.fakelevel.FakeLevel;
import net.minecraft.core.RegistrySetBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RegistrySetBuilder.class)
public class RegistrySetBuilderMixin {

  @WrapWithCondition(
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/core/RegistrySetBuilder$BuildState;reportRemainingUnreferencedValues()V"),
      method =
          "build(Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/core/HolderLookup$Provider;")
  private boolean pulsar$validateRefs(RegistrySetBuilder.BuildState instance) {
    return !FakeLevel.isLoading();
  }
}
