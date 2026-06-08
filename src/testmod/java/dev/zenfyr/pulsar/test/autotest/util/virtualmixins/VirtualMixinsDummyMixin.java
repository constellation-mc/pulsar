package dev.zenfyr.pulsar.test.autotest.util.virtualmixins;

import dev.zenfyr.pulsar.test.autotest.util.VirtualMixinsDummyTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VirtualMixinsDummyTarget.class)
public class VirtualMixinsDummyMixin {

  @Inject(
      at = @At("RETURN"),
      method = "test()Ljava/lang/String;",
      cancellable = true,
      remap = false)
  private static void pulsar$testEdit(CallbackInfoReturnable<String> cir) {
    cir.setReturnValue("mod");
  }
}
