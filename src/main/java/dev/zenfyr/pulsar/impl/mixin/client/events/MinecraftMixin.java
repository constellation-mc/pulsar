package dev.zenfyr.pulsar.impl.mixin.client.events;

import dev.zenfyr.pulsar.client.events.AfterFirstReload;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
  @Inject(method = "lambda$new$6", at = @At("TAIL"), require = 0)
  private void pulsar$init(CallbackInfo ci) {
    Minecraft.getInstance().execute(() -> {
      try {
        AfterFirstReload.EVENT.invoker().afterFirstReload();
      } catch (Throwable t) {
        CrashReport report = CrashReport.forThrowable(t, "Running event");
        Minecraft.crash(Minecraft.getInstance(), Minecraft.getInstance().gameDirectory, report, -1);
      }
    });
  }
}
