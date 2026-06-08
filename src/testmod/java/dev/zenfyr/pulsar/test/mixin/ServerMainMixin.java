package dev.zenfyr.pulsar.test.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.zenfyr.pulsar.test.util.Utils;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Main.class)
public class ServerMainMixin {
  @ModifyExpressionValue(
      method = "main",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/server/Eula;hasAgreedToEULA()Z"))
  private static boolean isEulaAgreedTo(boolean original) {
    return original || Utils.ENABLED;
  }
}
