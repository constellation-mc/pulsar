package dev.zenfyr.pulsar.impl.mixin.client.fakeworld;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.zenfyr.pulsar.client.fakelevel.FakeLevel;
import java.util.function.Consumer;
import net.minecraft.core.HolderSet;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnvironmentAttributeSystem.class)
public class EnvironmentAttributeSystemMixin {
  @WrapOperation(
      method = "addDefaultLayers",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/core/HolderSet;forEach(Ljava/util/function/Consumer;)V"))
  private static void pulsar$skipEnvLayersForFakeLevel(
      HolderSet instance, Consumer consumer, Operation<Void> original) {
    if (FakeLevel.isLoading()) {
      return;
    }
    original.call(instance, consumer);
  }
}
